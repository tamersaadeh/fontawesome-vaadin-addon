package it.saadeh.fontawesome;

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
