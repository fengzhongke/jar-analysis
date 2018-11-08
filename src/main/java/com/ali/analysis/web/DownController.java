package com.ali.analysis.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("down")
public class DownController {

	@Autowired
	private HttpServletResponse response;

	@RequestMapping("load")
	public void load(Model model, @RequestParam(value = "fileName") String fileName) {
		System.out.println("download[" + fileName + "]");
		File downloadFile = new File(fileName);
		response.setContentType("application/octet-stream");
		response.setContentLength((int) downloadFile.length());
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		try (InputStream in = new FileInputStream(downloadFile)) {

			byte[] data = new byte[8 * 1024 * 1024];
			int total = 0;
			int len = 0;
			while ((len = in.read(data)) != 0) {
				response.getOutputStream().write(data, 0, len);
				total += len;
				System.out.println("fileName [" + total + "]");
			}
			response.flushBuffer();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("download[" + fileName + "] done");
	}
}
