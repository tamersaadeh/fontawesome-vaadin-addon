package it.saadeh.fontawesome.generator;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * License: MIT
 * @author Tamer Saadeh
 */
public class GenerateEnum {
	private static final Logger log = LoggerFactory.getLogger(GenerateEnum.class);;

	private static final String VERSION = "5.0.6";

	private static final String ZIP_SOURCE_URL = "http://use.fontawesome.com/releases/v" + VERSION
			+ "/fontawesome-free-" + VERSION + ".zip";

	private static final int BUFFER = 1024 * 10; // Max buffer size, 10K
	private static final long ZIP_SIZE_LIMIT = 0x1E00000; // Max size of zip 30MB

	// XXX: use File.pathSeparator properly
	private static final String ENUM_PATH = "../fontawesome/src/main/java/it/saadeh/fontawesome";
	private static final String ENUM_FILE = ENUM_PATH + "/FontAwesome.java";
	private static final String WEB_CONTENT_DIR = "../fontawesome/src/main/resources/VAADIN/addons/fontawesome";

	private static final String FONT_AWESOME_BASE_PATH = "/fontawesome-free-" + VERSION;
	private static final String META_JSON_PATH = FONT_AWESOME_BASE_PATH + "/advanced-options/metadata/icons.json";
	private static final String STYLES_PATH = FONT_AWESOME_BASE_PATH + "/web-fonts-with-css";
	private static final String CSS_PATH = STYLES_PATH + "/css/fontawesome-all.min.css";
	private static final String WEB_FONTS_PATH = STYLES_PATH + "/webfonts";

	private static final String TOKEN = "DUMMY_ENUM_CONSTANT(\"\", 0);";

	private ArrayList<String> enumTypes = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		log.info("Updating " + ENUM_FILE + " and " + WEB_CONTENT_DIR);
		GenerateEnum x = new GenerateEnum();
		String downloadDir = x.download();
		x.scan(downloadDir);
		x.update();
		x.copy(downloadDir);
		log.info("Finished updating icons!");
	}

	private String download() throws IOException {
		URL url = null;
		try {
			url = new URL(GenerateEnum.ZIP_SOURCE_URL);
		} catch (MalformedURLException e) {
			throw e;
		}
		try {
			url.openConnection();
		} catch (IOException e) {
			throw e;
		}

		long total = 0;
		Path tmpZip = Files.createTempFile("font-awesome", ".zip");

		try (InputStream in = url.openStream(); OutputStream out = Files.newOutputStream(tmpZip)) {
			byte[] buffer = new byte[BUFFER];
			int count = 0;
			while (total + BUFFER <= ZIP_SIZE_LIMIT && (count = in.read(buffer, 0, BUFFER)) != -1) {
				out.write(buffer, 0, count);
				total += count;
			}
		}

		Path tmpDir = Files.createTempDirectory("font-awesome");

		ZipFile zip = new ZipFile(tmpZip.toFile());
		Enumeration<ZipArchiveEntry> entries = zip.getEntries();
		ZipArchiveEntry entry;
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();

			String basePath = tmpDir.toFile().getAbsolutePath() + File.separator;
			String name = entry.getName();
			String path = basePath + name;

			if (entry.isDirectory()) {
				log.debug("   creating: " + path);
				new File(path).mkdir();
				continue;
			} else if (!new File(path).getParentFile().exists() && name.contains(File.separator)) {
				String[] dirs = name.split(File.separator);
				String x = basePath;
				for (int i = 0; i < dirs.length - 1; i++) {
					x += dirs[i] + File.separator;
					log.debug("   creating: " + x);
					new File(x).mkdir();
				}
				continue;
			}

			try (InputStream in = zip.getInputStream(entry);
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path))) {
				log.debug(" extracting: " + path);
				byte[] buffer = new byte[BUFFER];
				int count = 0;
				while (total + BUFFER <= ZIP_SIZE_LIMIT && (count = in.read(buffer, 0, BUFFER)) != -1) {
					out.write(buffer, 0, count);
					total += count;
				}
				out.flush();
			}
		}

		zip.close();

		// System.out.println(tmpZip);
		// Files.delete(tmpZip);
		return tmpDir.toFile().getAbsolutePath();
	}

	private void scan(String base) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(new File(base + META_JSON_PATH));

		// lets find out what fields it has
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String name = fieldNames.next();
			JsonNode obj = node.get(name);

			String codeStr = obj.get("unicode").asText();
			int code = Integer.parseInt(codeStr, 16);

			for (JsonNode style : obj.get("styles")) {
				String styleStr = style.asText();
				Style x = Style.valueOf(styleStr.toUpperCase());
				emit(name, code, x);
			}
		}
	}

	private void emit(String name, int code, Style style) {
		String emittedName = name;
		if (Character.isDigit(emittedName.charAt(0)))
			emittedName = "_" + emittedName;
		String emittedClass = style.getClazz() + " fa-" + name.toLowerCase();

		// this provides compatibility with old Vaadin FontAwesome Enum, but adds the
		// deprecation to it
		String emittedEnum;
		if (style == Style.SOLID) {
			emittedEnum = emittedName.replaceAll("-", "_").toUpperCase();
			String toEmit = "@Deprecated " + emittedEnum + "(\"" + emittedClass + "\", 0x" + Integer.toHexString(code)
					+ ")";

			log.debug(toEmit);
			enumTypes.add(toEmit);
		}
		emittedEnum = emittedName.replaceAll("-", "_").toUpperCase() + "_" + style.getClazz().toUpperCase().charAt(2);

		String toEmit = emittedEnum + "(\"" + emittedClass + "\", 0x" + Integer.toHexString(code) + ")";

		log.debug(toEmit);
		enumTypes.add(toEmit);
	}

	private void copy(String base) throws IOException {
		new File(WEB_CONTENT_DIR + "/css").mkdirs();

		Path cssSrc = new File(base + CSS_PATH).toPath();
		Path cssTarget = new File(WEB_CONTENT_DIR + "/css/fontawesome.min.css").toPath();
		Files.move(cssSrc, cssTarget, StandardCopyOption.REPLACE_EXISTING);

		Path webFontsSrc = new File(base + WEB_FONTS_PATH).toPath();
		Path webFontsTarget = new File(WEB_CONTENT_DIR + "/webfonts").toPath();

		File fonts = new File(WEB_CONTENT_DIR + "/webfonts");
		if (fonts.exists())
			fonts.delete();
		Files.move(webFontsSrc, webFontsTarget);
	}

	private void update() throws IOException {
		StringBuffer sb = new StringBuffer(BUFFER);
		for (int i = 0; i < enumTypes.size(); i++) {
			sb.append(enumTypes.get(i));
			if (i < enumTypes.size() - 1)
				sb.append(",\n\t");
		}
		sb.append(";\n");

		String result = null;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(ENUM_FILE)))) {
			StringBuffer sb2 = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb2.append(line);
				sb2.append('\n');
			}
			int idx = sb2.indexOf(TOKEN);
			sb2.replace(idx, idx + TOKEN.length(), sb.toString());
			result = sb2.toString();
		}
		if (result == null)
			return;

		new File(ENUM_PATH).mkdirs();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ENUM_FILE)))) {
			bw.write(result);
			bw.flush();
		}

	}

	private enum Style {
		SOLID("fas"), REGULAR("far"), LIGHT("fal"), BRANDS("fab");

		private String clazz;

		private Style(String clazz) {
			this.clazz = clazz;
		}

		public String getClazz() {
			return clazz;
		}
	}
}
