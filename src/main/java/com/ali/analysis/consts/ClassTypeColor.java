package com.ali.analysis.consts;

public enum ClassTypeColor {

	CLASS_COLOR(ClassType.CLASS, "#06F"), 
	CLASS_ABSTRACT(ClassType.ABSTRACT,
			"#0CF"), CLASS_INTERFACE(ClassType.INTERFACE, "#9F6"), CLASS_ANNOTATION(ClassType.ANNOTATION, "#F3F");
	
	
	private final ClassType type;
	private final String color;

	ClassTypeColor(ClassType type, String color) {
		this.type = type;
		this.color = color;
	}

	public static ClassTypeColor getColor(ClassType type) {
		for (ClassTypeColor value : values()) {
			if (value.type == type) {
				return value;
			}
		}
		return null;
	}

	public ClassType getType() {
		return type;
	}

	public String getColor() {
		return color;
	}
	
}
