package com.ali.analysis.model.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassVO {

	private String name;
	private String className;
	private int x;
	private int y;
	private String color;
	private List<AttrVO> attrs;
}
