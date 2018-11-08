package com.ali.analysis.support;

import java.util.List;
import java.util.TreeMap;

import com.ali.analysis.model.InvokeBean;
import com.ali.analysis.model.InvokePlot;
import com.ali.analysis.model.InvokePlot.Node;

import lombok.Data;

@Data
public class SessionInvokeService {
	private InvokeLoader loader;

	public SessionInvokeService(InvokeLoader loader) {
		this.loader = loader;
	}

	public InvokePlot getInvokes(long id) {
		TreeMap<Long, InvokeBean> beanMap = loader.getInvokeBeans();
		InvokePlot plot = new InvokePlot();
		InvokeBean bean = beanMap.get(id);
		if (bean != null) {
			String cName = bean.getCName();
			Node node = plot.addNode(id, bean.getMName(), cName);

			node.setXOffset(1);
			List<Long> is = bean.getInvokeIds();
			int length = is.size();
			for (int i = 0; i < length; i++) {
				long subId = is.get(i);
				InvokeBean subBean = beanMap.get(subId);
				plot.addNode(subId, subBean.getMName(), subBean.getCName());
				plot.addInvoke(id, subId, i + 1);
//				List<Long> subIs = subBean.getInvokeIds();
//				int subLength = subIs.size();
//				for(int j=0; j<subLength; j++){
//					long ssubId = subIs.get(j);
//					InvokeBean ssubBean = beanMap.get(ssubId);
//					plot.addNode(ssubId, ssubBean.getMName(), ssubBean.getCName());
//					plot.addInvoke(subId, ssubId, j + 1);
//				}
			}
		}
		return plot;
	}
}
