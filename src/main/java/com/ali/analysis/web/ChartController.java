package com.ali.analysis.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ali.analysis.consts.ClassTypeColor;
import com.ali.analysis.model.AttrBean;
import com.ali.analysis.model.ClassBean;
import com.ali.analysis.model.ClassPlot;
import com.ali.analysis.model.RelatePlot;
import com.ali.analysis.model.RelateTreeNode;
import com.ali.analysis.model.vo.AttrVO;
import com.ali.analysis.model.vo.ClassVO;
import com.ali.analysis.model.vo.RelateVO;
import com.ali.analysis.support.SessionClassService;
import com.ali.analysis.util.WebUtils;

@Controller
@RequestMapping("chart")
public class ChartController {

	Function<Class<?>, String> GET_CLASS_NAME = clazz -> {
		String name = clazz.getSimpleName();
		if (name.length() == 0) {
			name = clazz.getName();
			int idx = name.lastIndexOf(".");
			if (idx > 0) {
				name = name.substring(idx);
			}
		}
		return name;
	};

	@RequestMapping("index")
	public String hirachy(Model model, @RequestParam(value = "className") String[] classNames,
			@RequestParam(value = "exclude", required = false) String[] excludes) throws Exception {
		SessionClassService classService = WebUtils.getClassService();
		try {
			model.addAttribute("className", classNames);
			model.addAttribute("exclude", excludes);
			Set<String> excludeNames = new HashSet<String>();
			if (excludes != null) {
				excludeNames.addAll(Arrays.asList(excludes));
			}

			Map<String, RelateTreeNode> treeNodes = classService.getTree(Arrays.asList(classNames));
			List<RelatePlot> relates = classService.getRelates(treeNodes);
			Iterator<Entry<String, RelateTreeNode>> itr = treeNodes.entrySet().iterator();
			while (itr.hasNext()) {
				if (excludeNames.contains(itr.next().getKey())) {
					itr.remove();
				}
			}

			List<ClassPlot> plots = classService.getPlot(treeNodes);
			List<RelateVO> relateVos = new ArrayList<RelateVO>();

			List<ClassVO> classVos = new ArrayList<ClassVO>();

			for (RelatePlot relate : relates) {
				Class<?> fromClazz = relate.getFromBean().getBean().getClazz();
				Class<?> toClazz = relate.getToBean().getBean().getClazz();

				if (!excludeNames.contains(fromClazz.getName()) && !excludeNames.contains(toClazz.getName())) {
					relateVos.add(new RelateVO(GET_CLASS_NAME.apply(fromClazz), GET_CLASS_NAME.apply(toClazz), "#56f", ""));
				}
			}
			for (ClassPlot plot : plots) {
				Class<?> clazz = plot.getNode().getBean().getClazz();
				ClassBean bean = plot.getNode().getBean();
				String color = ClassTypeColor.getColor(bean.getType()).getColor();

				List<AttrVO> attrVos = new ArrayList<AttrVO>();
				for (AttrBean attr : bean.getATTRS()) {
					String className = attr.getType().getName();
					boolean contains = classService.getContainer().getByCName(className) != null;
					attrVos.add(new AttrVO(attr.getName(), GET_CLASS_NAME.apply(attr.getType()), "#56f", contains,
							className));
				}

				classVos.add(new ClassVO(GET_CLASS_NAME.apply(clazz), clazz.getName(), plot.getXOffset(),
						plot.getYOffset(), color, attrVos));
			}
			model.addAttribute("relateVos", relateVos);
			model.addAttribute("classVos", classVos);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return "class-plot";
	}
}
