package com.ali.analysis.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RelateTreeNode {
	private ClassBean bean;
	private Map<String, RelateTreeNode> parents = new HashMap<String, RelateTreeNode>();
	private Map<String, RelateTreeNode> sons = new HashMap<String, RelateTreeNode>();
}
