package kr.gaion.ceh.restapi.handler;

import java.util.Map;

import org.apache.spark.SparkContext;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.KmeansClusteringSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.algorithms.KmeansClustering;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * 
 * @author hoang
 *
 */
public class ApiKmeansClustering implements ITrainable, IPredictable {

	final static Logger logger = LoggerFactory.getLogger(ApiKmeansClustering.class);

	/**
	 * train API
	 */
	public IResponseObject train(IConfigurable config) throws Exception {

		logger.info("Start K-means training..");

		// Run spark application with received configuration
		IResponseObject response = KmeansClustering.train(config);
		/*boolean saveDataToEs = config.getSetting(KmeansClusteringSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(KmeansClusteringSettings.ES_WRITING_INDEX);
			String type = config.getSetting(KmeansClusteringSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}*/

		return response;
	}

	/**
	 * predict API
	 */
	public IResponseObject predict(IConfigurable config) throws Exception {

		// to load the saved model
		KMeansModel model = null;
		String modelName = config.getSetting(KmeansClusteringSettings.MODEL_NAME);
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, KmeansClusteringSettings.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		SparkContext sc = SparkEsConnector.getSparkContext();
		model = KMeansModel.load(sc, modelDir);

		// get data for predicting
		String dataInputOption = config.getSetting(KmeansClusteringSettings.DATA_INPUT_OPTION);
		JavaRDD<Vector> data = null;

		switch (dataInputOption) {
		case KmeansClusteringSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.loadUnlabeledDataFromFile(config);
			break;
		}
		case KmeansClusteringSettings.INPUT_FROM_ES: {
			// get test data from ElasticSearch
			data = SparkEsConnector.getClusteringDataFromES(config);
			break;
		}
		default: {
			// abnormal case:
			ResponseBase err = new ResponseBase(ResponseType.MESSAGE);
			err.setMessage("Input method is not acceptable: " + dataInputOption);
			return err;
		}
		}

		IResponseObject response = KmeansClustering.predictClusters(config, model, data);

		// save to ElasticSearch
		boolean saveDataToEs = config.getSetting(KmeansClusteringSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(KmeansClusteringSettings.ES_WRITING_INDEX);
			String type = config.getSetting(KmeansClusteringSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}

}
