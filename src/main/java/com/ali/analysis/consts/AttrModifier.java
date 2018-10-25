package com.ali.analysis.consts;

import java.lang.reflect.Modifier;

public enum AttrModifier {
	PUBLIC("public", "公有"), PRIVATE("private", "私有"), DEFAULT("default", "默认"), PROTECTED("protected", "保护");

	private final String name;
	private final String type;

	AttrModifier(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static AttrModifier getModifier(int mod) {
		return Modifier.isPublic(mod) ? PUBLIC
				: Modifier.isPrivate(mod) ? PRIVATE : Modifier.isProtected(mod) ? PROTECTED : DEFAULT;
	}

}
