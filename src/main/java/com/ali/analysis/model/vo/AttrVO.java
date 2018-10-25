package com.ali.analysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttrVO {

	private String name;
	private String type;
	private String color;
	private boolean contains;
	private String className;
}
