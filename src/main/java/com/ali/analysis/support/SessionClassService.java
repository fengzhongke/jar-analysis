package com.ali.analysis.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ali.analysis.consts.RelateType;
import com.ali.analysis.model.ClassBean;
import com.ali.analysis.model.ClassPlot;
import com.ali.analysis.model.RelatePlot;
import com.ali.analysis.model.RelateTreeNode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SessionClassService {

	private ClassContainer container;
	private final JarClassLoader loader;

	public SessionClassService(JarClassLoader loader) {
		this.loader = loader;
		try {
			this.container = (ClassContainer) loader.getContainer().clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	private int isSon(ClassBean from, ClassBean to) {
		Class<?> fromClazz = from.getClazz();
		Class<?> toClazz = to.getClazz();
		if (fromClazz != null && toClazz != null && fromClazz != toClazz) {
			if (RelateType.getRelate(fromClazz, toClazz) != null) {
				return 1;
			} else if (RelateType.getRelate(toClazz, fromClazz) != null) {
				return -1;
			}
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	private boolean isDirect(Class<?> from, Class<?> to) {
		if (from == to.getSuperclass()) {
			return true;
		} else if (from.isAnnotation() && to.isAnnotationPresent((Class<? extends Annotation>) from)) {
			return true;
		} else {
			Class<?>[] inters = to.getInterfaces();
			if (inters != null) {
				for (Class<?> inter : inters) {
					if (inter == from) {
						return true;
					}
				}
			}
			return false;
		}
	}

	private void addSon(RelateTreeNode parent, RelateTreeNode son) {
		Map<String, RelateTreeNode> sons = parent.getSons();
		Class<?> sonClazz = son.getBean().getClazz();
		Class<?> parentClazz = parent.getBean().getClazz();
		boolean add = false;
		Iterator<Entry<String, RelateTreeNode>> sonItr = sons.entrySet().iterator();
		while (sonItr.hasNext()) {
			RelateTreeNode relate = sonItr.next().getValue();
			Class<?> relateClazz = relate.getBean().getClazz();
			if (relateClazz != sonClazz) {
				if (RelateType.getRelate(relateClazz, sonClazz) != null) {
					add = true;
					addSon(relate, son);
				} else if (RelateType.getRelate(sonClazz, relateClazz) != null) {
					if (!isDirect(parentClazz, relateClazz)) {
						relate.getParents().remove(parent.getBean().getCName());
						sonItr.remove();
					}
					if (!son.getSons().containsKey(relate.getBean().getCName())) {
						son.getSons().put(relate.getBean().getCName(), relate);
						relate.getParents().put(son.getBean().getCName(), son);
					}
				}
			}
		}

		Map<String, RelateTreeNode> parents = son.getParents();
		Iterator<Entry<String, RelateTreeNode>> parentItr = parents.entrySet().iterator();
		while (parentItr.hasNext()) {
			RelateTreeNode relate = parentItr.next().getValue();
			Class<?> relateClazz = relate.getBean().getClazz();
			if (relateClazz != parentClazz) {
				if (RelateType.getRelate(parentClazz, relateClazz) != null) {
					add = true;
					addSon(parent, relate);
				} else if (RelateType.getRelate(relateClazz, parentClazz) != null) {
					if (!isDirect(relateClazz, sonClazz)) {
						relate.getSons().remove(son.getBean().getCName());
						parentItr.remove();
					}
					if (!relate.getSons().containsKey(parent.getBean().getCName())) {
						relate.getSons().put(parent.getBean().getCName(), parent);
						parent.getParents().put(relate.getBean().getCName(), relate);
					}
				}
			}
		}
		if (!add || isDirect(parentClazz, sonClazz)) {
			parent.getSons().put(son.getBean().getCName(), son);
			son.getParents().put(parent.getBean().getCName(), parent);
		}
	}

	public Map<String, RelateTreeNode> getTree(List<String> classNames) {
		Map<String, ClassBean> beans = container.getClassBeans();
		Map<String, RelateTreeNode> treeNodes = new HashMap<String, RelateTreeNode>();
		for (String className : classNames) {
			ClassBean classBean = beans.get(className);
			if (classBean != null) {
				for (Entry<String, ClassBean> entry : beans.entrySet()) {
					ClassBean bean = entry.getValue();
					int isSon = isSon(bean, classBean);
					if (isSon != 0) {
						RelateTreeNode classNode = treeNodes.get(className);
						if (classNode == null) {
							treeNodes.put(className, classNode = new RelateTreeNode());
							classNode.setBean(classBean);
						}
						RelateTreeNode node = treeNodes.get(bean.getCName());
						if (node == null) {
							treeNodes.put(bean.getCName(), node = new RelateTreeNode());
							node.setBean(bean);
						}
						if (isSon == 1) {
							addSon(node, classNode);
						} else {
							addSon(classNode, node);
						}
					}
				}
			}
		}

		for (String className : classNames) {
			ClassBean classBean = beans.get(className);
			RelateTreeNode classNode = treeNodes.get(className);
			if (classNode == null && classBean != null && classBean.getClazz() != null) {
				treeNodes.put(className, classNode = new RelateTreeNode());
				classNode.setBean(classBean);
			}
		}
		return treeNodes;
	}

	public List<RelatePlot> getRelates(Map<String, RelateTreeNode> treeNodes) {
		List<RelatePlot> relates = new ArrayList<RelatePlot>();
		for (RelateTreeNode node : treeNodes.values()) {
			Class<?> clazz = node.getBean().getClazz();
			for (RelateTreeNode sonNode : node.getSons().values()) {
				Class<?> sonClazz = sonNode.getBean().getClazz();
				RelatePlot relate = new RelatePlot(RelateType.getRelate(clazz, sonClazz), node, sonNode);
				relates.add(relate);
				// System.out.println(relate.getToBean().getBean().getCName() +
				// " [" + relate.getType().getName() + "] "
				// + relate.getFromBean().getBean().getCName());
			}
		}
		return relates;
	}

	private void setX(Map<String, ClassPlot> plotMap, ClassPlot plot, int x) {
		if (plot != null) {
			int max = plot.getXOffset();
			max = max > x ? max : x;
			plot.setXOffset(max);
			RelateTreeNode node = plot.getNode();
			Map<String, RelateTreeNode> sons = node.getSons();
			for (String son : sons.keySet()) {
				setX(plotMap, plotMap.get(son), max + 1);
			}
		}
	}

	public List<ClassPlot> getPlot(Map<String, RelateTreeNode> treeNodes) {
		List<ClassPlot> plots = new ArrayList<ClassPlot>();
		Map<String, ClassPlot> plotMap = new HashMap<String, ClassPlot>();
		for (RelateTreeNode node : treeNodes.values()) {
			ClassPlot plot = new ClassPlot();
			plot.setNode(node);
			plotMap.put(node.getBean().getCName(), plot);
			plots.add(plot);
		}
		for (ClassPlot plot : plotMap.values()) {
			setX(plotMap, plot, 1);
		}
		Map<Integer, Integer> yMap = new HashMap<Integer, Integer>();
		for (ClassPlot plot : plotMap.values()) {
			int x = plot.getXOffset();
			Integer y = yMap.get(x);
			if (y == null) {
				yMap.put(x, 2);
				plot.setYOffset(1);
			} else {
				yMap.put(x, y + 1);
				plot.setYOffset(y);
			}
		}

		for (ClassPlot plot : plotMap.values()) {
			System.out.println(
					plot.getNode().getBean().getCName() + "(" + plot.getXOffset() + "," + plot.getYOffset() + ")");
			;
		}
		return plots;
	}

}
