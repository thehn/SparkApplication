package kr.gaion.ceh.common.bean.settings;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.util.Utilities;

/**
 * to save information of trained model
 * 
 * @author hoang
 *
 */
public class ModelInfo {

	/*
	 * Properties
	 */
	protected String algorithmName;
	protected String modelName;
	protected String parameters;
	protected Date createdDate;

	/**
	 * Constructor
	 * 
	 * @param algorithmName
	 */
	public ModelInfo(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	/**
	 * Constructor
	 * 
	 * @param algorithmName
	 * @param parameters
	 */
	public ModelInfo(String algorithmName, IConfigurable parameters) {
		this.algorithmName = algorithmName;
		this.parameters = new Gson().toJson(parameters);
	}

	/**
	 * to load model informations from file saved
	 * 
	 * @param modelName
	 * @return
	 * @throws IOException
	 */
	public ModelInfo loadFromJson(String modelName) throws IOException {
		String json = "";
		String rootDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, this.algorithmName, Constants.MODEL_DIR);
		String pathname = rootDir + File.separator + modelName + ".info";
		File modelFile = new File(pathname);
		FileInputStream fis = new FileInputStream(modelFile);
		byte[] data = new byte[(int) modelFile.length()];
		fis.read(data);
		fis.close();
		json = new String(data, "UTF-8");
		ModelInfo info = new Gson().fromJson(json, ModelInfo.class);

		return info;
	}

	/**
	 * To save model information as text-JSON-formated file
	 * 
	 * @param modelName
	 * @throws IOException
	 */
	public void saveJson(String modelName) throws IOException {
		this.createdDate = new Date();
		this.modelName = modelName;
		String json = new Gson().toJson(this);
		String rootDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, this.algorithmName, Constants.MODEL_DIR);
		String outputFile = rootDir + File.separator + modelName + ".info";
		BufferedWriter buff = new BufferedWriter(new FileWriter(outputFile));
		buff.write(json);
		buff.close();
	}

	/**
	 * To delete existed model
	 * 
	 * @param modelName
	 * @return
	 * @throws IOException
	 */
	public void deleteModel(String modelName) throws IOException {
		String rootDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, this.algorithmName, Constants.MODEL_DIR);
		String pathname = rootDir + File.separator + modelName;
		File model = new File(pathname);
		FileUtils.deleteDirectory(model);
		File modelInfo = new File(pathname + ".info");
		FileUtils.forceDelete(modelInfo);
	}

	/**
	 * To get saved models by specified algorithm name
	 * 
	 * @return
	 * @throws IOException
	 */
	public String[] getSavedModels() throws IOException {
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, this.algorithmName, Constants.MODEL_DIR);
		String[] listModels = Utilities.getListFoldersInDir(modelDir);

		return listModels;
	}

	/*
	 * Reservation method
	 */
	public void loadFromXML() {

	};

	/*
	 * Reservation method
	 */
	public void saveXML(String modelName) {

	}

	/*
	 * Setters and Getters
	 */
	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
