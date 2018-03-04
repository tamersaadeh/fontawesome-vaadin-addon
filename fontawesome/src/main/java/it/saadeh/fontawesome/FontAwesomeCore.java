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


	public enum IconType {
		SOLID("fas"), REGULAR("far"), LIGHT("fal"), BRANDS("fab");

		private final String type;

		IconType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	}

	public enum Style {
		XSMALL("fa-xs"), SMALL("fa-sm"), LARGE("fa-lg"), TIMES_2("fa-2x"),
		TIMES_3("fa-3x"), TIMES_4("fa-4x"), TIMES_5("fa-5x"), TIMES_6("fa-6x"),
		TIMES_7("fa-7x"), TIMES_8("fa-8x"), TIMES_9("fa-9x"), TIMES_10("fa-10x"),
		FIXED_WIDTH("fa-fw"), BORDER("fa-border"), PULL_RIGHT("fa-pull-right"),
		PULL_LEFT("fa-pull-left"), SPIN("fa-spin"), PULSE("fa-pulse");

		private final String style;

		Style(String style) {
			this.style = style;
		}

		@Override
		public String toString() {
			return style;
		}
	}

	public enum PowerTransform {
		SHRINK("shrink-", TransformArg.DOUBLE), GROW("grow-", TransformArg.DOUBLE), UP("up-", TransformArg.DOUBLE),
		DOWN("down-", TransformArg.DOUBLE), LEFT("left-", TransformArg.DOUBLE), RIGHT("right-", TransformArg.DOUBLE),
		ROTATE("rotate-", TransformArg.INT), FLIP("flip-", TransformArg.VHCHAR);

		private final TransformArg arg;
		private boolean called = false;
		private String transform;

		PowerTransform(String transform, TransformArg arg) {
			this.transform = transform;
			this.arg = arg;
		}

		public PowerTransform apply(double amount) {
			if (arg != TransformArg.INT)
				throw new UnsupportedOperationException("This transform accepts only double!");
			this.transform += amount;
			this.called = true;
			return this;
		}

		public PowerTransform apply(int amount) {
			if (arg != TransformArg.INT)
				throw new UnsupportedOperationException("This transform accepts only integer!");
			this.transform += amount;
			this.called = true;
			return this;
		}

		public PowerTransform apply(char type) {
			if (arg != TransformArg.VHCHAR)
				throw new UnsupportedOperationException("This transform accepts only char!");

			if (type != 'v' && type != 'V' && type != 'h' && type != 'H')
				throw new UnsupportedOperationException("This transform accepts only char type={v,h}!");

			this.transform += type;
			this.called = true;
			return this;
		}

		@Override
		public String toString() {
			if (!called)
				throw new UnsupportedOperationException("You must apply a value to the transform!");
			return transform;
		}

		private enum TransformArg {
			INT, DOUBLE, VHCHAR
		}
	}

}
