package kr.gaion.ceh.common.bean.response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.util.Utilities;

public class FSChiSqSelectorResponse extends ResponseBase {

	public static Gson gson = new Gson();

	// protected List<Map<String, ?>> filteredFeatures;
	protected List<String> filteredFeatures;
	protected List<String> selectedFields;
	protected String classCol;

	public FSChiSqSelectorResponse(ResponseType type) {
		super(type);
	}

	public List<?> getFilteredFeatures() {
		return filteredFeatures;
	}

	public List<String> getSelectedFields() {
		return selectedFields;
	}

	public void setSelectedFields(List<String> selectedFields) {
		this.selectedFields = selectedFields;
	}

	public void setFilteredFeatures(List<String> filteredFeatures) {
		// this.filteredFeatures = (List<Map<String, ?>>) filteredFeatures;
		this.filteredFeatures = filteredFeatures;
	}

	public String getClassCol() {
		return classCol;
	}

	public void setClassCol(String classCol) {
		this.classCol = classCol;
	}

	@Override
	public String toString() {
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("[");
		for (String filteredData : filteredFeatures) {
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
	 * to make data for printing
	 * 
	 * @param fileNameWithoutExtension
	 * @return
	 * @throws IOException
	 */
	public String saveFilteredFeaturesInfoToCSV(String label, String fileNameWithoutExtension) throws IOException {
		StringBuilder dataBuilder = new StringBuilder();
		String lineSeparator = System.lineSeparator();
		String fileName = fileNameWithoutExtension + Constants.CSV_EXTENSION;
		String fileOutput = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, fileName);
		String delimiter = Constants.CSV_SEPARATOR;
		
		dataBuilder.append(label);
		dataBuilder.append(delimiter);
		for (String column : selectedFields) {
			dataBuilder.append(column);
			dataBuilder.append(delimiter);
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append(lineSeparator);

		for (String filteredData : filteredFeatures) {
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