package com.ali.analysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassTableVO {
	private String className;
	private int getters;
	private int toGetters;
	private String type;

}
