package com.ali.analysis.support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.TreeMap;

import com.ali.analysis.model.InvokeBean;

import lombok.Data;

@Data
public class InvokeLoader {
	private TreeMap<Long, InvokeBean> invokeBeans = new TreeMap<Long, InvokeBean>();

	public void loadSource(InputStream in) {
		Stack<InvokeBean> stack = new Stack<InvokeBean>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String temp = null;
			while ((temp = br.readLine()) != null) {
				String[] items = temp.split(" ");
				if (items.length != 1) {
					String mName = items[0].substring(1);
					long id = Long.valueOf(items[1].substring(3, items[1].length() - 1));
					String type = items[2].substring(3, items[2].length() - 1);
					String cName = items[3].substring(3, items[3].length() - 2);
					InvokeBean bean = new InvokeBean(id, mName, cName);
					if (!stack.isEmpty()) {
						stack.peek().addSon(id);
					}
					if (type.equalsIgnoreCase("1")) {
						invokeBeans.put(id, bean);
					}
					stack.push(bean);
				} else {
					stack.pop();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
