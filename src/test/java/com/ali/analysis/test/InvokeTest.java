package com.ali.analysis.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.ali.analysis.support.InvokeLoader;

public class InvokeTest {
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = "/u01/trace/jdbc/simple/com.framework.example.test.PoolTest-1.xml.xml";
		InvokeLoader loader = new InvokeLoader();
		try (FileInputStream in = new FileInputStream(fileName)) {

			loader.loadSource(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(loader.getInvokeBeans().lastEntry());

	}
}
