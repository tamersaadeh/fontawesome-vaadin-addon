package it.saadeh.fontawesome;

import java.util.ArrayList;

public enum FontAwesome implements FontAwesomeCore {
	DUMMY_ENUM_CONSTANT(IconType.SOLID, "");

	private final IconType type;
	private final String icon;


	private String clazz;
	private ArrayList<PowerTransform> transforms = new ArrayList<>();
	private ArrayList<FontAwesomeCore> masks = new ArrayList<>();
	private ArrayList<Style> styles = new ArrayList<>();

	FontAwesome(IconType type, String icon) {
		this.type = type;
		this.icon = icon;
		clazz = type + " " + icon;
	}

	@Override
	public String toString() {
		if (!transforms.isEmpty() && !masks.isEmpty())
			return serialise(applyTransforms(transforms), applyMasks(masks));
		if (!transforms.isEmpty())
			return serialise(applyTransforms(transforms));
		if (!masks.isEmpty())
			return serialise(null, applyMasks(masks));
		return serialise();
	}

	@Override
	public String getClazz() {
		return clazz;
	}

	@Override
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public FontAwesomeCore add(PowerTransform transform) {
		transforms.add(transform);
		return this;
	}

	@Override
	public FontAwesomeCore add(FontAwesomeCore mask) {
		masks.add(mask);
		return this;
	}

	@Override
	public FontAwesomeCore add(Style style) {
		styles.add(style);
		return this;
	}
}
