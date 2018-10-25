package com.ali.analysis.model;

import com.ali.analysis.consts.RelateType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatePlot {
	private RelateType type;
	private RelateTreeNode fromBean;
	private RelateTreeNode toBean;
}
