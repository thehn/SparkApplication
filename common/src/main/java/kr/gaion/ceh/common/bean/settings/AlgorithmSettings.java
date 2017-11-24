package kr.gaion.ceh.common.bean.settings;

import kr.gaion.ceh.common.config.ConfigBase;
import kr.gaion.ceh.common.interfaces.IJsonObject;
import com.google.gson.Gson;

/**
 * To save all settings of an algorithm
 * 
 * @author hoang
 *
 */
/**
 * @author hoang
 *
 */
public class AlgorithmSettings extends ConfigBase implements IJsonObject {

	/**
	 * Classes constants
	 */
	public static final String ACTION = "action";
	public static final String ALGORITHM = "algorithm";
	public static final String ACTION_TRAIN = "train";
	public static final String ACTION_GEN_ASRULES = "genAsRules";
	public static final String ACTION_PREDICT = "predict";
	public static final String ES_READING_INDEX = "indexR";
	public static final String ES_READING_TYPE = "typeR";
	public static final String ES_READING_FIELDNAME = "fieldNameR";
	public static final String ES_WRITING_INDEX = "indexW";
	public static final String ES_WRITING_TYPE = "typeW";
	public static final String ES_WRITING_FIELDNAME = "fieldNameW";
	public static final String SAVE_TO_ES = "saveToES";
	public static final String MODEL_NAME = "modelName";
	public static final String DEFAULT_MODEL_NAME = "Default_Model";
	public static final String FILE_NAME = "fileName";
	public static final String DELIMITER = "delimiter";
	public static final String DATA_INPUT_OPTION = "dataInputOption";
	public static final String INPUT_FROM_FILE = "file";
	public static final String INPUT_FROM_ES = "es";
	public static final String INDEX_OF_LABELED_FIELD = "indexOfLabeledField";
	public static final String CLASS_COL = "classCol";
	public static final String LIST_FEATURES_COL = "featureCols";
	public static final String LIST_FIELD_FOR_PREDICT = "fieldsForPredict";

	// for scheduling
	public static final String SCHEDULE_FLG = "isSchedule";
	public static final String JOB_TITLE = "title";
	public static final String SCHEDULE_DESC = "description";
	public static final String SCHEDULE_INTERVAL = "interval";
	public static final String SCHEDULE_WDAY = "wday";
	public static final String SCHEDULE_CRON_EXP = "cronExpression";
	public static final String SCHEDULE_MIN = "min";
	public static final String SCHEDULE_HOUR = "hour";
	public static final String SCHEDULE_MDAY = "mday";

	// Lookup
	public static final String LOOKUP_DATA_LIST = "listLookups";

	/*
	 * Default setting values
	 */
	public final static String DEFAULT_DATA_INPUT_OPTION = "file";

	/*
	 * #PC0016: Add features selection (optional) to each algorithm
	 */
	public final static String FEATURE_SELECTION_ENABEL_FLG = "featuresSelectionEnabelFlg";
	public final static String NUMBER_FEATURES = "numberFeatures";
	public final static String NUMBER_BINS = "bin";
	public static final int DEFAULT_BIN = 1;

	/*
	 * Spark application setting values
	 */
	protected String indexR;
	protected String typeR;
	protected String fieldNameR;
	protected String indexW;
	protected String typeW;
	protected String fieldNameW;
	protected String action;
	protected String algorithm;
	protected String modelName;
	protected String fileName;
	protected String delimiter;
	protected String dataInputOption;
	protected String classCol;
	protected String[] featureCols;
	protected String[] fieldsForPredict;
	protected boolean saveToES = false;
	protected boolean isSchedule = false;
	protected int indexOfLabeledField;

	/*
	 * Schedule setting values
	 */
	protected String title;
	protected String description;
	protected String interval;
	protected String wday;
	protected String cronExpression;
	protected int min;
	protected int hour;
	protected int mday;

	/*
	 * Properties for data pre-processing
	 */
	protected boolean featuresSelectionEnabelFlg = false;
	protected int numberFeatures; // #PC0016
	protected int bin; // #PC0016

	// #PC0016 - Start
	public int getNumberFeatures() {
		return numberFeatures;
	}

	public boolean getFeaturesSelectionEnabelFlg() {
		return featuresSelectionEnabelFlg;
	}

	public void setFeaturesSelectionEnabelFlg(boolean featuresSelectionEnabelFlg) {
		this.featuresSelectionEnabelFlg = featuresSelectionEnabelFlg;
	}

	public void setNumberFeatures(int numberFeatures) {
		this.numberFeatures = numberFeatures;
	}

	public int getBin() {
		if (bin == 0) {
			return DEFAULT_BIN;
		} else
			return bin;
	}

	public void setBin(int bin) {
		this.bin = bin;
	}
	// #PC0016 - End

	/*
	 * Lookup data
	 */
	protected int listLookups;

	/**
	 * This is constructor without parameters
	 */
	public AlgorithmSettings() {
	}

	/*
	 * Setters and Getters
	 */
	public boolean getSaveToES() {
		return saveToES;
	}

	public void setSaveToES(boolean saveToES) {
		this.saveToES = saveToES;
	}

	/*
	 * public void setAppMaster(String appMaster) { this.appMaster = appMaster; }
	 */

	/*
	 * public void setAppName(String appName) { this.appName = appName; }
	 */

	public void setIndexR(String indexR) {
		this.indexR = indexR;
	}

	public void setTypeR(String typeR) {
		this.typeR = typeR;
	}

	public void setFieldNameR(String fieldName) {
		this.fieldNameR = fieldName;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	/*
	 * public String getAppMaster() { return this.appMaster; }
	 * 
	 * public String getAppName() { return this.appName; }
	 */

	public String[] getFieldsForPredict() {
		return fieldsForPredict;
	}

	public void setFieldsForPredict(String[] fieldsForPredict) {
		this.fieldsForPredict = fieldsForPredict;
	}

	public String getIndexR() {
		return this.indexR;
	}

	public String getTypeR() {
		return this.typeR;
	}

	public String getFieldNameR() {
		return this.fieldNameR;
	}

	public String getAction() {
		return this.action;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	public String getIndexW() {
		return indexW;
	}

	public void setIndexW(String indexW) {
		this.indexW = indexW;
	}

	public String getTypeW() {
		return typeW;
	}

	public void setTypeW(String typeW) {
		this.typeW = typeW;
	}

	public String getFieldNameW() {
		return fieldNameW;
	}

	public void setFieldNameW(String fieldNameW) {
		this.fieldNameW = fieldNameW;
	}

	public String getModelName() {
		if (modelName == null || modelName.isEmpty()) {
			return DEFAULT_MODEL_NAME;
		}
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName.replaceAll(" ", "");
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileId) {
		this.fileName = fileId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDataInputOption() {
		if (this.dataInputOption == null || dataInputOption.isEmpty()) {
			return DEFAULT_DATA_INPUT_OPTION;
		} else
			return dataInputOption;
	}

	public void setDataInputOption(String dataInputOption) {
		this.dataInputOption = dataInputOption;
	}

	public boolean getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(boolean isSchedule) {
		this.isSchedule = isSchedule;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getWday() {
		return wday;
	}

	public void setWday(String wday) {
		this.wday = wday;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMday() {
		return mday;
	}

	public void setMday(int mday) {
		this.mday = mday;
	}

	public int getListLookups() {
		return listLookups;
	}

	public void setListLookups(int listLookups) {
		this.listLookups = listLookups;
	}

	public int getIndexOfLabeledField() {
		return indexOfLabeledField;
	}

	public void setIndexOfLabeledField(int indexOfLabeledField) {
		this.indexOfLabeledField = indexOfLabeledField;
	}

	public String getClassCol() {
		return classCol;
	}

	public void setClassCol(String classCol) {
		this.classCol = classCol;
	}

	public String[] getFeatureCols() {
		return featureCols;
	}

	public void setFeatureCols(String[] featureCols) {
		this.featureCols = featureCols;
	}

	/**
	 * This interface to convert current object to JSON format
	 */
	public String toJson() {
		return new Gson().toJson(this);
	}

}
