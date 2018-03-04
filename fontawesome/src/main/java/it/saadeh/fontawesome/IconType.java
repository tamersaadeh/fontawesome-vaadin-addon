package it.saadeh.fontawesome;

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
