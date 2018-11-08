package com.ali.analysis.support;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ali.analysis.consts.AttrModifier;
import com.ali.analysis.consts.AttrType;
import com.ali.analysis.consts.ClassType;
import com.ali.analysis.model.AttrBean;
import com.ali.analysis.model.ClassBean;
import com.ali.analysis.model.FileBean;
import com.ali.analysis.util.ClassUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JarClassLoader extends ClassLoader {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private ClassContainer container = new ClassContainer();

	public JarClassLoader() {
		super(JarClassLoader.class.getClassLoader().getParent());
	}

	/**
	 * load class from file inputStream
	 * 
	 * @param fileName
	 * @param in
	 */
	public void loadSource(String fileName, InputStream in) {
		try (JarInputStream jarInput = new JarInputStream(in)) {
			Set<String> newClassNames = new HashSet<String>();
			JarEntry entry = null;
			while ((entry = jarInput.getNextJarEntry()) != null) {
				String entryName = entry.getName();
				if (entryName.endsWith(".class")) {
					String className = entryName.replace(".class", "").replace("/", ".");

					if (!container.getClassBeans().containsKey(className)) {
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						int chunk = 0;
						byte[] data = new byte[256];
						while (-1 != (chunk = jarInput.read(data))) {
							bytes.write(data, 0, chunk);
						}
						ClassBean classBean = new ClassBean(fileName, className, bytes.toByteArray(), this);
						container.getClassBeans().put(className, classBean);

						FileBean fileBean = container.getFileBeans().get(fileName);
						if (fileBean == null) {
							container.getFileBeans().put(fileName, fileBean = new FileBean());
						}
						fileBean.addFailBean(classBean);

						newClassNames.add(className);
					} else {
						LOGGER.info("class already exists[" + className + "]");
					}
				}
			}
			for (String className : newClassNames) {
				try {
					loadClass(className);
				} catch (Throwable e) {
					LOGGER.error("load class[" + className + "] error", e);
				}
			}

			fillGetter();
		} catch (Throwable e) {
			LOGGER.error("load jar file[" + fileName + "] error", e);
		}
	}

	protected Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = findLoadedClass(name);
		if (clazz == null) {
			ClassBean classBean = container.getByCName(name);
			byte[] bytes = null;
			if (classBean == null || (bytes = classBean.getBytes()) == null) {
				throw new ClassNotFoundException("class not found : " + name);
			}
			clazz = this.defineClass(name, bytes, 0, bytes.length);
			classBean.setClazz(clazz);
			classBean.setType(ClassType.getType(clazz));
			FileBean fileBean = container.getByFName(classBean.getFName());
			fileBean.addSuccBean(classBean);
		}
		return clazz;
	}

	private void fillGetter() {
		for (ClassBean classBean : container.getClassBeans().values()) {
			Class<?> clazz = classBean.getClazz();
			try {
				Field[] fields = clazz.getDeclaredFields();
				if (fields != null) {
					for (Field field : fields) {
						String fieldClazz = field.getType().getName();

						ClassBean fieldBean = container.getByCName(fieldClazz);
						if (fieldBean != null) {
							classBean.addGetter(fieldBean);
						}
						classBean.addAttr(new AttrBean(AttrType.FIELD, field.getName(), field.getType(),
								AttrModifier.getModifier(field.getModifiers())));
					}
				}
			} catch (Throwable e) {
			}
			try {
				Method[] methods = clazz.getDeclaredMethods();
				if (methods != null) {
					for (Method method : methods) {
						String fieldClazz = method.getReturnType().getName();
						ClassBean fieldBean = container.getByCName(fieldClazz);
						if (fieldBean != null) {
							classBean.addGetter(fieldBean);
						}
						
						classBean.addAttr(new AttrBean(AttrType.METHOD, ClassUtil.getShortName(method), method.getReturnType(),
								AttrModifier.getModifier(method.getModifiers())));
					}
				}
			} catch (Throwable e) {
			}
		}
	}
}
