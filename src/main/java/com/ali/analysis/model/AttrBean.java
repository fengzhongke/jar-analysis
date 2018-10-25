package com.ali.analysis.model;

import com.ali.analysis.consts.AttrModifier;
import com.ali.analysis.consts.AttrType;

import lombok.Data;

@Data
public class AttrBean {

	private final AttrType attrType;
	private final String name;
	private final Class<?> type;
	private final AttrModifier modifier;

	public AttrBean(AttrType attrType, String name, Class<?> type, AttrModifier modifier) {
		this.attrType = attrType;
		this.name = name;
		this.type = type;
		this.modifier = modifier;
	}

}
