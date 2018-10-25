package com.ali.analysis.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.ali.analysis.consts.ClassType;

import lombok.Data;

@Data
public class FileBean {
	private Map<String, ClassBean> succBeans = new LinkedHashMap<String, ClassBean>();
	private Map<String, ClassBean> failBeans = new LinkedHashMap<String, ClassBean>();
	private Map<ClassType, Integer> typeCnt = new HashMap<ClassType, Integer>();

	public void addFailBean(ClassBean classBean) {
		String className = classBean.getCName();
		succBeans.remove(className);
		failBeans.put(className, classBean);
	}

	public void addSuccBean(ClassBean classBean) {
		String className = classBean.getCName();
		failBeans.remove(className);
		succBeans.put(className, classBean);
		ClassType classType = classBean.getType();
		Integer cnt = typeCnt.get(classType);
		if (cnt == null) {
			typeCnt.put(classType, 1);
		} else {
			typeCnt.put(classType, cnt + 1);
		}
	}

	public int getTypeCnt(ClassType classType) {
		Integer cnt = typeCnt.get(classType);
		if (cnt == null) {
			return 0;
		}
		return cnt;
	}
}
