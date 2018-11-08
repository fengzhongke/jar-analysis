package com.ali.analysis.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ali.analysis.support.InvokeLoader;
import com.ali.analysis.support.JarClassLoader;
import com.ali.analysis.support.SessionClassService;
import com.ali.analysis.support.SessionInvokeService;

public class WebUtils {
	private static HttpSession getSession() {
		RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttr).getRequest();
		HttpSession session = request.getSession();
		return session;
	}

	public static <T> T getLoader(Class<T> loaderClass) throws Exception {
		HttpSession session = getSession();
		String name = loaderClass.getName();
		@SuppressWarnings("unchecked")
		T loader = (T) session.getAttribute(name);
		if (loader == null) {
			session.setAttribute(name, loader = loaderClass.newInstance());
		}
		return loader;
	}

	public static <T> void resetLoader(Class<T> loaderClass) {
		getSession().removeAttribute(loaderClass.getName());
	}

	public static SessionClassService getClassService() throws Exception {
		HttpSession session = getSession();
		String name = SessionClassService.class.getName();
		SessionClassService sevice = (SessionClassService) session.getAttribute(name);
		JarClassLoader loader = getLoader(JarClassLoader.class);
		if (sevice == null || sevice.getLoader() != loader) {
			session.setAttribute(name, sevice = new SessionClassService(loader));
		}
		return sevice;
	}

	public static SessionInvokeService getInvokeService() throws Exception {
		HttpSession session = getSession();
		String name = SessionInvokeService.class.getName();
		SessionInvokeService sevice = (SessionInvokeService) session.getAttribute(name);
		InvokeLoader loader = getLoader(InvokeLoader.class);
		if (sevice == null || sevice.getLoader() != loader) {
			session.setAttribute(name, sevice = new SessionInvokeService(loader));
		}
		return sevice;
	}

}
