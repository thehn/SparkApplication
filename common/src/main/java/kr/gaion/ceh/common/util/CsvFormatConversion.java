package kr.gaion.ceh.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;

/**
 * all conversion about CSV format will come here
 * 
 * @author hoang
 *
 */
public class CsvFormatConversion {

	static Gson gson = new Gson();

	/**
	 * 
	 * @param header
	 * @param line
	 * @param separator
	 * @return
	 */
	public static String convertCsvToJson(String[] header, String line, String separator) {
		Map<String, String> map = new HashMap<>();
		String[] values = line.split(separator);
		if (header.length != values.length) {
			return "";
		}

		int index = 0;
		for (String col : header) {
			if (values[index].length() == 0) {
				map.put(col, "0");
				index++;
			} else {
				map.put(col, values[index++]);
			}
		}
		return gson.toJson(map);
	}

	/**
	 * to make data to fit with DataTable format (JavaScript array)
	 * 
	 * @param listData
	 * @return
	 */
	public static String covertListStringToJavascrtipTableData(List<?> listData) {
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("[");
		for (Object filteredData : listData) {
			dataBuilder.append("[");
			dataBuilder.append(filteredData);
			dataBuilder.append("]");
			dataBuilder.append(",");
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");
		return dataBuilder.toString();
	}

	/**
	 * to make data (CSV format) for printing without header
	 * 
	 * @param fileNameWithoutExtension
	 * @return
	 * @throws IOException
	 */
	public static String saveListDataToFile(List<?> listData, String fileNameWithoutExtension) throws IOException {
		StringBuilder dataBuilder = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		String fileName = fileNameWithoutExtension + Constants.CSV_EXTENSION;
		String fileOutput = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, fileName);

		for (Object filteredData : listData) {
			dataBuilder.append(filteredData);
			dataBuilder.append(lineSeparator);
		}

		try {
			FileUtils.touch(new File(fileOutput));
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput));
			writer.write(dataBuilder.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileOutput;
	}

	/**
	 * to make data (CSV format) for printing within header
	 * 
	 * @param fileNameWithoutExtension
	 * @return
	 * @throws IOException
	 */
	public static String saveListDataToFile(String[] header, List<?> listData, String fileNameWithoutExtension)
			throws IOException {
		StringBuilder dataBuilder = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		String fileName = fileNameWithoutExtension + Constants.CSV_EXTENSION;
		String fileOutput = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, fileName);
		String delimiter = Constants.CSV_SEPARATOR;

		dataBuilder.append("predicted_label");
		dataBuilder.append(delimiter);
		for (String column : header) {
			dataBuilder.append(column);
			dataBuilder.append(delimiter);
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append(lineSeparator);

		for (Object filteredData : listData) {
			dataBuilder.append(filteredData);
			dataBuilder.append(lineSeparator);
		}

		try {
			FileUtils.touch(new File(fileOutput));
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput));
			writer.write(dataBuilder.toString());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileOutput;
	}

}
