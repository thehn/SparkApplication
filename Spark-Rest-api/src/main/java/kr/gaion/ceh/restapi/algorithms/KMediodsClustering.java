package kr.gaion.ceh.restapi.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clust4j.algo.KMedoids;
import com.clust4j.algo.KMedoidsParameters;
import com.clust4j.data.DataSet;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.bean.response.ClusterResponse;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.KMediodsClusteringSettings;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

import static kr.gaion.ceh.restapi.algorithms.ClusterCommon.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author hoang
 *
 */
public class KMediodsClustering {

	public static final String ALGORITHM_NAME = "KMediodsClustering";

	final static Logger logger = LoggerFactory.getLogger(KMediodsClustering.class);

	/**
	 * to clustering
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject train(IConfigurable config) throws Exception {
		logger.info("Start training KMediods ..");
		int k = config.getSetting(KMediodsClusteringSettings.NUM_CLUSTERS);
		DataSet data = getDataSet(config);
		KMedoids kmd = new KMedoidsParameters(k).fitNewModel(data.getData());
		kmd.saveObject(getFileOutputStream(config));
		final int[] labels = kmd.getLabels();
		ClusterResponse response = new ClusterResponse(ResponseType.OBJECT_DATA);
		response.setCenters(kmd.getCentroids());

		List<PredictionInfo<?, ?>> predictionInfo = new ArrayList<PredictionInfo<?, ?>>();
		int index = 0;
		for (Integer label : labels) {
			PredictionInfo<Integer, Object> predicted = new PredictionInfo<>();
			predicted.setPredictedValue(label);
			Map<String, Object> map = new HashMap<>();
			map.put("values", data.getData().getRow(index++));
			predicted.setFeatures(map);
			predictionInfo.add(predicted);
		}

		response.setPredictionInfo(predictionInfo);
		response.setLabels(labels);

		// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		String modelName = config.getSetting(KMediodsClusteringSettings.MODEL_NAME);
		ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// it has to convert to lower case, because Elasticsearch does not allow any
		// upper character as index
		String url = SparkEsConnector.getURL(ALGORITHM_NAME.toLowerCase(), modelName);
		Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
		SparkEsConnector.writeDataToEs(url, map);
		
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		return response;
	}

	/**
	 * to predict cluster
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject predictClusters(IConfigurable config) throws Exception {
		// to load the saved model
		KMedoids model = null;
		model = (KMedoids) KMedoids.loadObject(getFileInputStream(config));
		DataSet unlabeledData = getUnlabeledData(config);
		final int[] labels = model.predict(unlabeledData.getData());
		ClusterResponse response = new ClusterResponse(ResponseType.OBJECT_DATA);
		response.setLabels(labels);
		List<PredictionInfo<?, ?>> predictionInfo = new ArrayList<PredictionInfo<?, ?>>();
		unlabeledData.setLabels(labels);
		int index = 0;
		for (Integer label : labels) {
			PredictionInfo<Integer, Object> predicted = new PredictionInfo<>();
			predicted.setPredictedValue(label);
			Map<String, Object> map = new HashMap<>();
			map.put("values", unlabeledData.getData().getRow(index++));
			predicted.setFeatures(map);
			predictionInfo.add(predicted);
		}
		response.setPredictionInfo(predictionInfo);
		
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		return response;
	}
}