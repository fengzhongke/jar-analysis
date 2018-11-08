package com.ali.analysis.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class InvokeBean {
	private long id;
	private String mName;
	private String cName;

	List<Long> invokeIds = new ArrayList<Long>();

	public InvokeBean(long id, String mName, String cName) {
		this.id = id;
		this.mName = mName;
		this.cName = cName;
	}

	public void addSon(long id) {
		invokeIds.add(id);
	}
}
