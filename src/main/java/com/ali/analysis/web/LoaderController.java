package com.ali.analysis.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ali.analysis.consts.ClassType;
import com.ali.analysis.model.ClassBean;
import com.ali.analysis.model.FileBean;
import com.ali.analysis.model.vo.ClassTableVO;
import com.ali.analysis.model.vo.FileTableVO;
import com.ali.analysis.support.FileClassLoader;
import com.ali.analysis.support.SessionClassService;
import com.ali.analysis.util.WebUtils;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("loader")
public class LoaderController {

	@RequestMapping("index")
	public String index(Model model, HttpServletRequest request) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		FileClassLoader loader = WebUtils.getLoader();
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multiRequest.getFiles("files");
			for (MultipartFile file : files) {
				try {
					loader.loadSource(file.getOriginalFilename(), file.getInputStream());
					WebUtils.resetService();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		SessionClassService service = WebUtils.getService();
		List<FileTableVO> fileVos = new ArrayList<FileTableVO>();
		Map<String, FileBean> fileBeans = service.getContainer().getFileBeans();
		for (Entry<String, FileBean> entry : fileBeans.entrySet()) {
			String fileName = entry.getKey();
			FileBean fileBean = entry.getValue();
			int succ = fileBean.getSuccBeans().size();
			int fail = fileBean.getFailBeans().size();
			int interfaceCnt = fileBean.getTypeCnt(ClassType.INTERFACE);
			int abstractCnt = fileBean.getTypeCnt(ClassType.ABSTRACT);
			int classCnt = fileBean.getTypeCnt(ClassType.CLASS);
			int annotationCnt = fileBean.getTypeCnt(ClassType.ANNOTATION);

			fileVos.add(new FileTableVO(fileName, succ, fail, succ + fail, interfaceCnt, abstractCnt, classCnt, annotationCnt));
		}
		Map<String, ClassBean> classBeans = service.getContainer().getClassBeans();
		List<ClassTableVO> classTableVos = new ArrayList<ClassTableVO>();
		for (Entry<String, ClassBean> entry : classBeans.entrySet()) {
			String className = entry.getKey();
			ClassBean classBean = entry.getValue();
			ClassType classType = classBean.getType();
			classTableVos.add(new ClassTableVO(className, classBean.getGETTER().size(), classBean.getTO_GETTER().size(),
					classType == null ? "未知" : classType.getName()));
		}
		model.addAttribute("fileVos", fileVos);
		model.addAttribute("classTableVos", classTableVos);

		return "loader";
	}
	

	@ResponseBody
	@RequestMapping("clear")
	public String clear() {
		JSONObject json = new JSONObject();
		try {
			WebUtils.resetLoader();
			json.put("status", 1);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("msg", "resetLoader error");

		}
		return String.valueOf(json);
	}

}
