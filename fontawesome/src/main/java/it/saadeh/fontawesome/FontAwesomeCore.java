package it.saadeh.fontawesome;

import java.util.ArrayList;

interface FontAwesomeCore {

	String getClazz();

	void setClazz(String clazz);

	FontAwesomeCore add(PowerTransform transform);

	FontAwesomeCore add(FontAwesomeCore mask);

	FontAwesomeCore add(Style style);

	default String apply(String clazz, Style s) {
		return clazz + s;
	}

	default String serialise(String transform) {
		return serialise(transform, null);
	}

	default String serialise() {
		return serialise(null, null);
	}

	default String serialise(String transform, String mask) {
		StringBuilder sb = new StringBuilder();
		sb.append("<i class\"");

		sb.append(getClazz());
		sb.append('\"');

		if (transform != null) {
			sb.append(" ");
			sb.append(transform);
		}

		if (mask != null) {
			sb.append(" ");
			sb.append(mask);
		}

		sb.append("/>");

		return sb.toString();
	}

	default String applyTransform(PowerTransform transform) {
		return "data-fa-transform=\"" + transform + "\"";
	}

	default String applyTransforms(ArrayList<PowerTransform> transforms) {
		StringBuilder sb = new StringBuilder();

		sb.append("data-fa-transform=\"");

		for (PowerTransform transform : transforms) {
			sb.append(transform);
			sb.append(" ");
		}

		sb.append('\"');

		return sb.toString();
	}

	default String applyMask(FontAwesomeCore icon) {
		return "data-fa-mask=\"" + icon.getClazz() + "\"";
	}

	default String applyMasks(ArrayList<FontAwesomeCore> icons) {
		StringBuilder sb = new StringBuilder();

		sb.append("data-fa-mask=\"");

		for (FontAwesomeCore icon : icons) {
			sb.append(icon.getClazz());
			sb.append(" ");
		}

		sb.append('\"');

		return sb.toString();
	}

	default void applyStyle(Style style) {
		setClazz(getClazz() + " " + style);
	}

	default void applyStyles(ArrayList<Style> styles) {
		StringBuilder sb = new StringBuilder(getClazz());

		for (Style style : styles) {
			sb.append(" ");
			sb.append(style);
		}

		setClazz(sb.toString());
	}

}
