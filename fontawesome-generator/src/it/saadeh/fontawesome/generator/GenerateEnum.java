package it.saadeh.fontawesome.generator;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * License: MIT
 * @author Tamer Saadeh
 */
public class GenerateEnum {
	private static final Logger log = LoggerFactory.getLogger(GenerateEnum.class);

	private static final String VERSION = "5.0.8";

	private static final String ZIP_SOURCE_URL = "http://use.fontawesome.com/releases/v" + VERSION
			+ "/fontawesome-free-" + VERSION + ".zip";

	// TODO: check if using File.pathSeparator needed
	private static final String ENUM_PATH = "fontawesome/src/main/java/it/saadeh/fontawesome";
	private static final String ENUM_FILE = ENUM_PATH + "/FontAwesome.java";
	private static final String RESOURCES_DIR = "fontawesome/src/main/resources/VAADIN";
	private static final String WEB_CONTENT_DIR = RESOURCES_DIR + "/addons/fontawesome";

	private static final String FONT_AWESOME_BASE_PATH = "/fontawesome-free-" + VERSION;
	private static final String META_JSON_PATH = FONT_AWESOME_BASE_PATH + "/advanced-options/metadata/icons.json";
	private static final String STYLES_PATH = FONT_AWESOME_BASE_PATH + "/web-fonts-with-css";
	private static final String CSS_PATH = STYLES_PATH + "/scss";
	private static final String WEB_FONTS_PATH = STYLES_PATH + "/webfonts";

	private static final String TOKEN = "\tDUMMY_ENUM_CONSTANT(\"\", 0);";

	private final ArrayList<String> enumTypes = new ArrayList<>();
	private int enumSize = 0;

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
		URL url = new URL(GenerateEnum.ZIP_SOURCE_URL);
		File tmpZip = File.createTempFile("font-awesome", ".zip");
		File tmpDir = Files.createTempDirectory("font-awesome").toFile();

		FileUtils.copyURLToFile(url, tmpZip);

		ZipFile zip = new ZipFile(tmpZip);
		Enumeration<ZipArchiveEntry> entries = zip.getEntries();
		ZipArchiveEntry entry;
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();

			String basePath = tmpDir.getAbsolutePath() + File.separator;
			String name = entry.getName();
			File path = new File(basePath + name);

			if (entry.isDirectory()) {
				FileUtils.forceMkdir(path);
				continue;
			} else if (!path.getParentFile().exists() && name.contains(File.separator)) {
				String[] dirs = name.split(File.separator);
				String x = basePath;
				for (int i = 0; i < dirs.length - 1; i++) {
					x += dirs[i] + File.separator;
					FileUtils.forceMkdir(new File(x));
				}
				continue;
			}

			FileUtils.copyToFile(zip.getInputStream(entry), path);
		}

		zip.close();

		FileUtils.deleteQuietly(tmpZip);
		return tmpDir.getAbsolutePath();
	}

	private void scan(String base) throws IOException {
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

		// this provides compatibility with old Vaadin FontAwesome Enum, but adds the @Deprecated annotation to it to it
		// TODO: use the table on https://fontawesome.com/how-to-use/upgrading-from-4 for better migration handling
		String emittedEnum;
		if (style == Style.SOLID) {
			emittedEnum = emittedName.replaceAll("-", "_").toUpperCase();
			String toEmit = "\n\t/**\n\t * Use {@link #" + emittedEnum + "_S " + emittedEnum + "_S} variant instead\n\t */\n\t@Deprecated\n\t"
					+ emittedEnum + "(\"" + emittedClass + "\", 0x" + Integer.toHexString(code) + ")";

			log.debug(toEmit);
			enumTypes.add(toEmit);
			enumSize += toEmit.length();
		}
		emittedEnum = emittedName.replaceAll("-", "_").toUpperCase() + "_" + style.getClazz().toUpperCase().charAt(2);

		String toEmit = "\t" + emittedEnum + "(\"" + emittedClass + "\", 0x" + Integer.toHexString(code) + ")";

		log.debug(toEmit);
		enumTypes.add(toEmit);
		enumSize += toEmit.length();
	}

	private void copy(String base) throws IOException {
		// replace all files
		FileUtils.deleteDirectory(new File(RESOURCES_DIR));

		File src = new File(base + CSS_PATH);
		File dst = new File(WEB_CONTENT_DIR + "/scss");

		FileUtils.copyDirectory(src, dst);

		src = new File(base + WEB_FONTS_PATH);
		dst = new File(WEB_CONTENT_DIR + "/webfonts");

		FileUtils.copyDirectory(src, dst);

		FileUtils.deleteDirectory(new File(base));
	}

	private void update() throws IOException {
		StringBuilder sb = new StringBuilder(enumSize);
		for (int i = 0; i < enumTypes.size(); i++) {
			sb.append(enumTypes.get(i));
			if (i < enumTypes.size() - 1)
				sb.append(",\n");
		}
		sb.append(";\n");

		String result;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(ENUM_FILE)))) {
			StringBuilder sb2 = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb2.append(line);
				sb2.append('\n');
			}
			int idx = sb2.indexOf(TOKEN);
			sb2.replace(idx, idx + TOKEN.length(), sb.toString());
			result = sb2.toString();
		}

		FileUtils.forceMkdir(new File(ENUM_PATH));

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ENUM_FILE)))) {
			bw.write(result);
			bw.flush();
		}

	}

	private enum Style {
		SOLID("fas"), REGULAR("far"), LIGHT("fal"), BRANDS("fab");

		private final String clazz;

		Style(String clazz) {
			this.clazz = clazz;
		}

		String getClazz() {
			return clazz;
		}
	}
}
