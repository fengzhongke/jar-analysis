package com.ali.analysis.util;

public class NameUtil {
	/**
	 * 如果class名太长，就把它折行
	 * 
	 * @param clasz
	 *            对应的class类 .
	 * @return
	 */
	public static String getName(String className) {
		StringBuilder name = new StringBuilder();
		int count = 0;
		int length = className.length();
		for (int i = 0; i < length; i++, count++) {
			if (count < 10) {
				continue;
			}
			if (className.charAt(i) < 'a') {
				name.append(className.substring(i - count, i));
				name.append((char) 92);
				name.append((char) 'n');
				count = 0;
			}
		}
		if (count > 0) {
			name.append(className.substring(length - count, length));
		}
		return name.toString();
	}
}
