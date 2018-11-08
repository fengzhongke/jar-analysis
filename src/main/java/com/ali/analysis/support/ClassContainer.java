package com.ali.analysis.support;

import java.util.HashMap;
import java.util.Map;

import com.ali.analysis.model.ClassBean;
import com.ali.analysis.model.FileBean;

import lombok.Data;

@Data
public class ClassContainer implements Cloneable {

	protected Map<String, ClassBean> classBeans = new HashMap<String, ClassBean>();
	protected Map<String, FileBean> fileBeans = new HashMap<String, FileBean>();

	public ClassBean getByCName(String className) {
		return classBeans.get(className);
	}
	
	public FileBean getByFName(String fileName){
		return fileBeans.get(fileName);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ClassContainer obj =  (ClassContainer)super.clone();
		obj.classBeans.putAll(classBeans);
		obj.fileBeans.putAll(fileBeans);
		return obj;
	}
}
