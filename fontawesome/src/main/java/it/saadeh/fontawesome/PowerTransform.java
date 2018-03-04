package it.saadeh.fontawesome;

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
