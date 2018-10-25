package com.ali.analysis.consts;

public enum AttrType {
	FIELD("属性"), METHOD("方法");

	private final String name;

	AttrType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
