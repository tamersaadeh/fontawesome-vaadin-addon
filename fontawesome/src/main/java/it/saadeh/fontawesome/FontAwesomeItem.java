package it.saadeh.fontawesome;

import com.vaadin.ui.Label;

public class FontAwesomeItem extends Label {
	private FontAwesomeCore icon;
	private String text;

	private StringBuilder sb = new StringBuilder();

	public FontAwesomeItem(FontAwesomeCore icon, String text) {
		this.icon = icon;
		this.text = text;
	}

	@Override
	public String getValue() {
		sb.append("<li><span class=\"fa-li\">");
		sb.append(icon);
		sb.append("</span>");
		sb.append(text);
		sb.append("</li>");
		return sb.toString();
	}

}
