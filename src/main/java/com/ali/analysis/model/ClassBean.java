package com.ali.analysis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ali.analysis.consts.ClassType;

import lombok.Data;

@Data
public class ClassBean {
	private String fName;
	private String cName;
	private byte[] bytes;
	private ClassLoader loader;
	private Class<?> clazz;
	private ClassType type;
	private final List<AttrBean> ATTRS = new ArrayList<AttrBean>();

	private final Map<String, ClassBean> GETTER = new HashMap<String, ClassBean>();
	private final Map<String, ClassBean> TO_GETTER = new HashMap<String, ClassBean>();

	public ClassBean(String fName, String cName, byte[] bytes, ClassLoader loader) {
		this.fName = fName;
		this.cName = cName;
		this.bytes = bytes;
		this.loader = loader;
	}

	public void addGetter(ClassBean classBean) {
		GETTER.put(classBean.getCName(), classBean);
		classBean.addToGetter(this);
	}

	public void addToGetter(ClassBean classBean) {
		TO_GETTER.put(classBean.getCName(), classBean);
	}

	public void addAttr(AttrBean attrBean) {
		ATTRS.add(attrBean);
	}
}
