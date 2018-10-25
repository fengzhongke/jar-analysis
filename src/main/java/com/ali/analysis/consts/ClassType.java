package com.ali.analysis.consts;

import java.lang.reflect.Modifier;

public enum ClassType {
	CLASS("class", "类"), ABSTRACT("abstract", "虚类"), INTERFACE("interface", "接口"), ANNOTATION("annotation", "注解");

	private final String name;
	private final String type;

	ClassType(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	public static ClassType getType(Class<?> clasz) {
		return clasz.isAnnotation() ? ClassType.ANNOTATION
				: clasz.isInterface() ? ClassType.INTERFACE
						: Modifier.isAbstract(clasz.getModifiers()) ? ClassType.ABSTRACT : ClassType.CLASS;

	}

}
