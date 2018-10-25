package com.ali.analysis.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author hanlang.hl
 *
 */
public class ClassUtil {
	/**
	 * 获取当前的类的class以对象 .
	 */
	public static final Class<?> getClasz() {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	/**
	 * 判断clasz 与clazes中的其中一个类的关系 .
	 * 
	 * @param clasz
	 *            原始类
	 * @param clazes
	 *            任何一个类
	 * @param asc
	 *            比较方式
	 * @return
	 */
	public static boolean isAssFrom(Class<?> clasz, Class<?>[] clazes, boolean asc) {
		for (Class<?> claz : clazes) {
			if (asc && claz.isAssignableFrom(clasz)) {
				return true;
			}
			if (!asc && clasz.isAssignableFrom(claz)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getShortName(Method method){
		Class<?>[] clazzs = method.getParameterTypes();
		StringBuilder sb = new StringBuilder(method.getName()).append("~");
		for(Class<?> clazz : clazzs){
			String name = clazz.getName();
			sb.append(name.charAt(0));
		}
		return sb.toString();
	}

	private static final Pattern PATTERN = Pattern.compile("(\\([\\[a-zA-Z0-9/;$]*\\))([\\[a-zA-Z0-9/;$]+)");

	public static List<Class<?>> getParams(String description) throws ClassNotFoundException {
		List<Class<?>> params = new ArrayList<Class<?>>();
		Matcher matcher = PATTERN.matcher(description);
		if (matcher.find()) {
			String paramStr = matcher.group(1);
			int length = paramStr.length() - 1;
			int idx = 0;
			Map<Integer, Class<?>> classes = new LinkedHashMap<Integer, Class<?>>();
			while (idx < length) {
				idx = getClass(description, idx, classes);
			}
			params.addAll(classes.values());
		}
		return params;
	}

	public static int getClass(String text, int idx, Map<Integer, Class<?>> classes) throws ClassNotFoundException {
		int lastIdx = idx + 1;
		Class<?> clazz = null;
		switch (text.charAt(idx)) {
		case 'Z':
			clazz = boolean.class;
			break;
		case 'B':
			clazz = byte.class;
			break;
		case 'C':
			clazz = char.class;
			break;
		case 'S':
			clazz = short.class;
			break;
		case 'I':
			clazz = int.class;
			break;
		case 'J':
			clazz = long.class;
			break;
		case 'F':
			clazz = float.class;
			break;
		case 'D':
			clazz = double.class;
			break;
		case 'L':
			lastIdx = text.indexOf(';', idx);
			clazz = Class.forName(text.substring(idx + 1, lastIdx).replace("/", "."));
			break;
		case '[':
			int length = text.length() - 2;
			char c = ' ';
			while (lastIdx < length && (c = text.charAt(lastIdx++)) == '[') {
			}
			if (c == 'L') {
				lastIdx = text.indexOf(';', lastIdx) + 1;
			}
			clazz = Class.forName(text.substring(idx, lastIdx).replace("/", "."));
			break;
		default:
			clazz = void.class;
		}
		classes.put(idx, clazz);
		return lastIdx;
	}

	public static Class<?> getReturn(String description) throws ClassNotFoundException {
		Class<?> clazz = null;
		Matcher matcher = PATTERN.matcher(description);
		if (matcher.find()) {
			Map<Integer, Class<?>> classes = new HashMap<Integer, Class<?>>();
			getClass(matcher.group(2), 0, classes);
			clazz = classes.get(0);
		}
		return clazz;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		// System.out.println(getClass("Z[[[BCSIJFD", 1));
		Map<Integer, Class<?>> classes = new LinkedHashMap<Integer, Class<?>>();
		System.out.println(getClass("Z[Lorg/springframework/context/ApplicationContext;BCSIJFD", 50, classes));
		System.out.println(classes);
		System.out.println(getParams("(Z[Lorg/springframework/context/ApplicationContext;BCSIJFD)V"));
		System.out.println(getReturn("(Z[Lorg/springframework/context/ApplicationContext;BCSIJFD)V"));
		System.out.println(Modifier.isAbstract(ClassPathXmlApplicationContext.class.getModifiers()));
	}
}
