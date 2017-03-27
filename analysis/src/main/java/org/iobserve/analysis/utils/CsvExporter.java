package org.iobserve.analysis.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class CsvExporter {

	private String filePath;
	private StringBuilder fileContent;
	
	public CsvExporter(String filePath) {
		this.filePath = /*"output\\logging\\" + */filePath;
		this.fileContent = new StringBuilder();
	}
	
	public void setHeadline(String headline) {
		fileContent.append(headline);
		fileContent.append(';');

		fileContent.append('\n');
	}

	public void addRow(List<? extends Object> elements) {
		fileContent.append(';');
		for(Object object : elements) {
			fileContent.append(object.toString());
			fileContent.append(';');
		}

		fileContent.append('\n');
	}

	public void export() {
		try {
			PrintWriter pw = new PrintWriter(filePath, "UTF-8");
			pw.write(fileContent.toString());
	        pw.close();
		} catch (FileNotFoundException e) {
			System.err.println("Could not create file " + filePath);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
