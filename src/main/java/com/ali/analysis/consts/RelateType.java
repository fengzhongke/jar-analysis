package com.ali.analysis.consts;

import java.lang.annotation.Annotation;

public enum RelateType {
	EXTEND("继承"), IMPLEMENT("实现"), ANNOTATE("注解");

	private final String name;

	RelateType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public static RelateType getRelate(Class<?> from, Class<?> to) {
		try {
			if (from.isAssignableFrom(to)) {
				return from.isInterface() && !to.isInterface() ? RelateType.IMPLEMENT : RelateType.EXTEND;
			} else if (from.isAnnotation() && to.isAnnotationPresent((Class<? extends Annotation>) from)) {
				return RelateType.ANNOTATE;
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

}
