package it.saadeh.fontawesome;

import com.vaadin.server.FontIcon;

/*
 * License: UNKONWN
 * See https://github.com/bdunn44/FontAwesome-Vaadin for license
 */
public enum FontAwesome implements FontIcon {
	DUMMY_ENUM_CONSTANT("", 0);

	private static final String fontFamily = "FontAwesomeAddon";
	private int codepoint;
	protected String clazz;

	FontAwesome(String clazz, int codepoint) {
		this.codepoint = codepoint;
		this.clazz = clazz;
	}

	public String getClazz() {
		return clazz;
	}

	public FontAwesomeLabel getLabel() {
		return new FontAwesomeLabel(this);
	}

	@Override
	public String getMIMEType() {
		throw new UnsupportedOperationException(
				FontIcon.class.getSimpleName() + " should not be used where a MIME type is needed.");
	}

	@Override
	public String getFontFamily() {
		return fontFamily;
	}

	@Override
	public int getCodepoint() {
		return codepoint;
	}

	@Override
	public String getHtml() {
		return "<i class=\"v-icon " + clazz + "\"></i>";
	}
}
