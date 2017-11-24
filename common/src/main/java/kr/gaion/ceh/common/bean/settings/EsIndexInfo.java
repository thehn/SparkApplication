package kr.gaion.ceh.common.bean.settings;

import com.google.gson.Gson;

import kr.gaion.ceh.common.config.ConfigBase;

public class EsIndexInfo extends ConfigBase {

	public static final int INDEX_NUMBER = 1;
	public static final int FIELD_NAME = 2;
	public static final String FILE_NAME = "fileName";
	public static final String DELIMITER = "delimiter";
	public static final int UNDEFINED_FORMAT = 0;
	public static final int DENSE_FORMAT = 1;
	public static final int SPARSE_FORMAT = 2;

	/**
	 * fieldOpt = INDEX_NUMBER: this is index of field<br>
	 * fieldOpt = FIELD_NAME: this is name of field<br>
	 */
	int fieldOpt;

	/**
	 * index of labeled field
	 */
	int labeledIndex;

	/**
	 * The value which user inputed
	 */
	String fieldValue;

	/**
	 * _index to save to ElasticSearch
	 */
	String indexW;

	/**
	 * _type to save to ElasticSearch
	 */
	String typeW;

	/**
	 * source name to save to ElasticSearch
	 */
	String sourceName;

	/**
	 * format of data file: DENSE or SPARSE
	 */
	DataFormat format;

	/**
	 * file name (full path) to index
	 */
	String fileName;

	/**
	 * delimiter to split data
	 */
	String delimiter;
	/**
	 * if this set, old data will be deleted from ElasticSearch
	 */
	boolean clearOldData;

	/**
	 * to send list of columns
	 */
	int[] listColumns;

	/**
	 * index of ID column in data BASKET format
	 */
	int indexOfColumnID;

	/**
	 * index of Category column in data BASKET format
	 */
	int indexOfColumnCategory;

	/**
	 * amount of features (using for SPARSE format)
	 */
	int numberFeatures;

	String listUploadedFiles;

	public String getListUploadedFiles() {
		return listUploadedFiles;
	}

	public void setListUploadedFiles(String listUploadedFiles) {
		this.listUploadedFiles = listUploadedFiles;
	}

	public int getNumberFeatures() {
		return numberFeatures;
	}

	public void setNumberFeatures(int numberFeatures) {
		this.numberFeatures = numberFeatures;
	}

	public int getIndexOfColumnID() {
		return indexOfColumnID;
	}

	public void setIndexOfColumnID(int indexOfColumnID) {
		this.indexOfColumnID = indexOfColumnID;
	}

	public int getIndexOfColumnCategory() {
		return indexOfColumnCategory;
	}

	public void setIndexOfColumnCategory(int indexOfColumnCategory) {
		this.indexOfColumnCategory = indexOfColumnCategory;
	}

	public int[] getListColumns() {
		return listColumns;
	}

	public void setListColumns(int[] listColumns) {
		this.listColumns = listColumns;
	}

	public DataFormat getFormat() {
		return format;
	}

	public void setFormat(DataFormat format) {
		this.format = format;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Getters and setters
	 * 
	 */
	public void setFieldOpt(int fieldOpt) {
		this.fieldOpt = fieldOpt;
	}

	public void setLabeledIndex(int labeledIndex) {
		this.labeledIndex = labeledIndex;
	}

	public void setIndexW(String esIndex) {
		this.indexW = esIndex;
	}

	public void setTypeW(String esType) {
		this.typeW = esType;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * Getters
	 * 
	 * @return
	 */
	public int getFieldOpt() {
		return this.fieldOpt;
	}

	public int getLabeledIndex() {
		return this.labeledIndex;
	}

	public String getIndexW() {
		return this.indexW;
	}

	public String getTypeW() {
		return this.typeW;
	}

	public String getSourceName() {
		return this.sourceName;
	}

	public String getFieldValue() {
		return this.fieldValue;
	}

	public boolean getClearOldData() {
		return clearOldData;
	}

	public void setClearOldData(boolean clearOldData) {
		this.clearOldData = clearOldData;
	}

	/**
	 * Convert to JSON format
	 * 
	 * @return
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}
}
