package kr.gaion.ceh.restapi.algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import com.clust4j.data.DataSet;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.KMediodsClusteringSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * 
 * @author hoang
 *
 */
public class ClusterCommon {

	/**
	 * to get data to clustering
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static DataSet getDataSet(IConfigurable config) throws Exception {
		JavaRDD<double[]> dataRdd = SparkEsConnector.getClusterDataFromESForDataSet(config);
		List<double[]> listData = dataRdd.collect();
		double[][] arr2d = new double[listData.size()][];
		int index = 0;
		for (double[] arr1d : listData) {
			arr2d[index++] = arr1d;
		}

		DataSet dataSet = new DataSet(arr2d);

		return dataSet;
	}

	/**
	 * to get unlabeled data to clustering
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static DataSet getUnlabeledData(IConfigurable config) throws Exception {
		// get data for predicting
		String dataInputOption = config.getSetting(KMediodsClusteringSettings.DATA_INPUT_OPTION);
		JavaRDD<double[]> data = null;

		switch (dataInputOption) {
		case KMediodsClusteringSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.loadDataFromFile(config);
			break;
		}
		case KMediodsClusteringSettings.INPUT_FROM_ES: {
			// get test data from ElasticSearch
			data = SparkEsConnector.getClusterDataFromESForDataSet(config);
			break;
		}
		default: {
			// abnormal case:
			// TODO default case
			break;
		}
		}

		int index = 0;
		List<double[]> listData = data.collect();
		double[][] arr2d = new double[listData.size()][];
		for (double[] arr1d : listData) {
			arr2d[index++] = arr1d;
		}

		DataSet dataSet = new DataSet(arr2d);

		return dataSet;
	}

	/**
	 * to get FileInputStream, to load saved model
	 * 
	 * @param config
	 * @return
	 * @throws IOException
	 */
	public static FileInputStream getFileInputStream(IConfigurable config) throws IOException {

		String modelName = config.getSetting(AlgorithmSettings.MODEL_NAME);
		String pathname = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				config.getSetting(AlgorithmSettings.ALGORITHM), Constants.MODEL_DIR, modelName, modelName);
		FileInputStream fis = new FileInputStream(new File(pathname));
		return fis;
	}

	/**
	 * to get FileOutputStream, to save model
	 * 
	 * @param config
	 * @return
	 * @throws IOException
	 */
	public static FileOutputStream getFileOutputStream(IConfigurable config) throws IOException {
		String modelName = config.getSetting(AlgorithmSettings.MODEL_NAME);
		String pathname = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				config.getSetting(AlgorithmSettings.ALGORITHM), Constants.MODEL_DIR, modelName, modelName);
		File output = new File(pathname);
		if (output.exists()) {
			Utilities.deleteIfExisted(pathname);
		}else
		{
			output.getParentFile().mkdirs();
			output.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(output);
		return fos;
	}
}
