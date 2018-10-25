package com.ali.analysis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileTableVO {
	private String fileName;
	private int succ;
	private int fail;
	private int count;
	private int interfaceCnt;
	private int abstractCnt;
	private int classCnt;
	private int annotationCnt;

}
