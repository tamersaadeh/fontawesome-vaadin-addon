package it.saadeh.fontawesome;

public enum FontAwesome implements FontAwesomeCore {
	DUMMY_ENUM_CONSTANT("");

	protected String clazz;

	FontAwesome(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public String getClazz() {
		return clazz;
	}
}
