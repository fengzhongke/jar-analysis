package com.ali.analysis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class InvokePlot {

	private List<Node> nodes = new ArrayList<Node>();
	private List<Invoke> relates = new ArrayList<Invoke>();
	

	private Map<String, Integer> xMap = new HashMap<String, Integer>();
	//private Map<String, Integer> yMap = new HashMap<String, Integer>();
	private int yIdx = 2;

	public Node addNode(long id, String mName, String cName) {
		Node node = new Node(id, mName, cName);
		nodes.add(node);
		setOffset(node);
		return node;
	}

	public Invoke addInvoke(long from, long to, int seq) {
		Invoke invoke = new Invoke(from, to, seq);
		relates.add(invoke);
		return invoke;
	}
	
	
	Consumer<Node> setOffset = (node)->{
		String cName = node.getCName();
		Integer x = xMap.get(cName);
		if(x == null){
			x = xMap.size() + 2;
			xMap.put(cName, x);
		}
//		Integer y = yMap.get(cName);
//		if (y == null) {
//			yMap.put(cName, y = 1);
//		} else {
//			yMap.put(cName, ++y);
//		}
		int y = yIdx++;
		node.setXOffset(x);
		node.setYOffset(y);
	};
	
	public void setOffset(Node node){
		setOffset.accept(node);
	}

	@Data
	public class Node {
		private String mName;
		private String cName;
		private long id;
		public Node(long id, String mName, String cName){
			this.id = id;
			this.mName = mName;
			this.cName = cName;
		}
		private int xOffset;
		private int yOffset;
	}

	@Data
	@AllArgsConstructor
	public class Invoke {
		private long from;
		private long to;
		private int seq;
	}
}
