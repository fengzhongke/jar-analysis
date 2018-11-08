package com.ali.analysis.web;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ali.analysis.model.InvokeBean;
import com.ali.analysis.model.InvokePlot;
import com.ali.analysis.model.InvokePlot.Invoke;
import com.ali.analysis.model.InvokePlot.Node;
import com.ali.analysis.model.vo.InvokeVO;
import com.ali.analysis.model.vo.RelateVO;
import com.ali.analysis.support.InvokeLoader;
import com.ali.analysis.support.SessionInvokeService;
import com.ali.analysis.util.WebUtils;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("invoke")
public class XmlInvokeController {

	@RequestMapping("index")
	public String index(Model model, HttpServletRequest request) throws Exception {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		long id = NumberUtils.toLong(request.getParameter("id"), 0);
		if (multipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multiRequest.getFile("file");
			if (!file.isEmpty()) {
				id = 0;
				WebUtils.resetLoader(InvokeLoader.class);
				WebUtils.getLoader(InvokeLoader.class).loadSource(file.getInputStream());
			}
		}
		SessionInvokeService service = WebUtils.getInvokeService();
		if (id == 0) {
			TreeMap<Long, InvokeBean> invokeBeans = service.getLoader().getInvokeBeans();
			if (!invokeBeans.isEmpty()) {
				id = service.getLoader().getInvokeBeans().lastKey();
			}
		}
		if (id != 0) {
			InvokePlot plot = service.getInvokes(id);
			List<Node> nodes = plot.getNodes();

			List<InvokeVO> invokeVos = new ArrayList<InvokeVO>();
			for (Node node : nodes) {
				invokeVos.add(new InvokeVO(node.getMName(), String.valueOf(node.getId()), node.getCName(),
						node.getXOffset(), node.getYOffset()));
			}

			List<Invoke> relates = plot.getRelates();
			List<RelateVO> relateVos = new ArrayList<RelateVO>();
			for (Invoke relate : relates) {
				relateVos.add(new RelateVO(String.valueOf(relate.getFrom()), String.valueOf(relate.getTo()), "#0f0",
						String.valueOf(relate.getSeq())));
			}

			model.addAttribute("relateVos", relateVos);
			model.addAttribute("invokeVos", invokeVos);
		}
		int size = service.getLoader().getInvokeBeans().size();
		model.addAttribute("id", id);
		model.addAttribute("size", size);
		return "invoke";
	}

	@ResponseBody
	@RequestMapping("clear")
	public String clear() {
		JSONObject json = new JSONObject();
		try {
			WebUtils.resetLoader(InvokeLoader.class);
			json.put("status", 1);
		} catch (Exception e) {
			json.put("status", -1);
			json.put("msg", "resetLoader error");

		}
		return String.valueOf(json);
	}

}
