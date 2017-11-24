package kr.gaion.ceh.web.response_processing;

import java.util.List;

/**
 * this class contains all utilities for make prober data for DataTable
 * 
 * @author hoang
 *
 */
public class DatatableDataProcessing {

	/**
	 * make columns list including: Label and other features
	 * 
	 * @param listFields
	 * @return
	 */
	public static String makeColumnsList(String classCol, List<String> listFields) {
		StringBuilder dataBuilder = new StringBuilder();

		dataBuilder.append("[");
		dataBuilder.append("{title:\"");
		dataBuilder.append(classCol);
		dataBuilder.append("\"},");
		for (String field : listFields) {
			dataBuilder.append("{title:\"");
			dataBuilder.append(field);
			dataBuilder.append("\"},");
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");

		return dataBuilder.toString();
	}
	
	/**
	 * make columns list including: Label and other features
	 * 
	 * @param listFields
	 * @return
	 */
	public static String makeColumnsList(String[] classCol, String[] listFields) {
		StringBuilder dataBuilder = new StringBuilder();

		dataBuilder.append("[");
		for (String field : classCol) {
			dataBuilder.append("{title:\"");
			dataBuilder.append(field);
			dataBuilder.append("\"},");
		}
		for (String field : listFields) {
			dataBuilder.append("{title:\"");
			dataBuilder.append(field);
			dataBuilder.append("\"},");
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");

		return dataBuilder.toString();
	}
}
