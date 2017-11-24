package kr.gaion.ceh.common.bean.settings;

import com.google.gson.JsonObject;

import kr.gaion.ceh.common.config.ConfigBase;

/**
 * 
 * @author hoang
 *
 */
public class DataLookupDefinition extends ConfigBase {

	public static final String ACTION_EDIT = "edit";
	public static final String ACTION_ADD = "add";

	String action;
	String lookupName;
	String index;
	String type;
	String source;
	String delimiter;
	int indexOfLabeledField;

	/**
	 * to get JSON object from current instance
	 * 
	 * @return
	 */
	public JsonObject toJsonObject() {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("lookupName", lookupName);
		jsonObj.addProperty("index", index);
		jsonObj.addProperty("type", type);
		jsonObj.addProperty("source", source);
		jsonObj.addProperty("delimiter", delimiter);
		jsonObj.addProperty("indexOfLabeledField", indexOfLabeledField);
		return jsonObj;
	}

	/**
	 * to apply all settings of current instance
	 * 
	 * @param config
	 */
	public void applySettings(AlgorithmSettings config) {
		config.setIndexR(this.index);
		config.setTypeR(this.type);
		config.setFieldNameR(this.source);
		config.setDelimiter(this.delimiter);
		config.setIndexOfLabeledField(this.indexOfLabeledField);
	}

	/**
	 * to apply all settings from JSON object
	 * 
	 * @param jsonObj
	 * @param config
	 */
	public static void applySettings(JsonObject jsonObj, AlgorithmSettings config) {

		String index = jsonObj.getAsJsonObject().get("index").getAsString();
		String type = jsonObj.getAsJsonObject().get("type").getAsString();
		String source = jsonObj.getAsJsonObject().get("source").getAsString();
		String delimiter = jsonObj.getAsJsonObject().get("delimiter").getAsString();
		int indexOfLabeledField = jsonObj.getAsJsonObject().get("indexOfLabeledField").getAsInt();

		config.setIndexR(index);
		config.setTypeR(type);
		config.setFieldNameR(source);
		config.setDelimiter(delimiter);
		config.setIndexOfLabeledField(indexOfLabeledField);
	}

	public String getLookupName() {
		return lookupName;
	}

	public void setLookupName(String lookupName) {
		this.lookupName = lookupName;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public int getIndexOfLabeledField() {
		return indexOfLabeledField;
	}

	public void setIndexOfLabeledField(int indexOfLabeledField) {
		this.indexOfLabeledField = indexOfLabeledField;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
