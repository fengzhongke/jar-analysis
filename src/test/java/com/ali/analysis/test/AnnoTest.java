package com.ali.analysis.test;

import java.lang.reflect.AnnotatedElement;

import com.ali.analysis.web.ChartController;

public class AnnoTest {

	public static void main(String[] args) {
		ChartController c = new ChartController();
		System.out.println(c instanceof AnnotatedElement);

	}

}
