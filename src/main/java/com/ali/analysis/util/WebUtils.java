package com.ali.analysis.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ali.analysis.support.FileClassLoader;
import com.ali.analysis.support.SessionClassService;

public class WebUtils {

	private static final String LOADER = "loader";
	private static final String SERVICE = "service";

	private static HttpSession getSession() {
		RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
		HttpSession session = request.getSession();
		return session;
	}

	public static FileClassLoader getLoader() {
		HttpSession session = getSession();
		FileClassLoader loader = (FileClassLoader) session.getAttribute(LOADER);
		if (loader == null) {
			session.setAttribute(LOADER, loader = new FileClassLoader());
		}
		return loader;
	}

	public static SessionClassService getService() {
		HttpSession session = getSession();
		SessionClassService sevice = (SessionClassService) session.getAttribute(SERVICE);
		FileClassLoader loader = getLoader();
		if (sevice == null || sevice.getLoader() != loader) {
			session.setAttribute(SERVICE, sevice = new SessionClassService(loader));
		}
		return sevice;
	}

	public static void resetLoader() {
		getSession().removeAttribute(LOADER);
	}

	public static void resetService() {
		getSession().removeAttribute(SERVICE);
	}

}
