package kr.gaion.ceh.restapi.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ClusterResponse;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.KmeansClusteringSettings;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import kr.gaion.ceh.restapi.util.PredictionUtil;
import scala.Tuple2;

/**
 * 
 * @author hoang
 *
 */
public class KmeansClustering {
	final static Logger logger = LoggerFactory.getLogger(KmeansClustering.class);

	public static final String ALGORITHM_NAME = "KmeansClustering";

	/**
	 * To create K-means model from data selected Elastic-search
	 * 
	 * @throws Exception
	 */
	public static IResponseObject train(IConfigurable config) throws Exception {

		logger.info("Train K-means model from data selected Elastic-search");
		JavaRDD<Vector> parsedData = SparkEsConnector.getClusteringDataFromES(config);
		ClusterResponse response = null;

		// cache data to memory
		parsedData.cache();

		// get setting values from client
		int numClusters = config.getSetting(KmeansClusteringSettings.NUM_CLUSTERS);
		int numIterations = config.getSetting(KmeansClusteringSettings.NUM_ITERATIONS);
		String modelName = config.getSetting(KmeansClusteringSettings.MODEL_NAME);

		// Run training algorithm to build the model.
		logger.info("Run algorithm to build the model.");
		KMeansModel clusters = KMeans.train(parsedData.rdd(), numClusters, numIterations);

		// Save and load model
		// Save model, and its information
		logger.info("Save model");
		SparkContext sc = SparkEsConnector.getSparkContext();
		String modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				KmeansClusteringSettings.ALGORITHM_NAME, Constants.MODEL_DIR, modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		clusters.save(sc, modelFullPathName);
		
		// save responses
		response = new ClusterResponse(ResponseType.OBJECT_DATA);
		logger.info("Center: ");
		List<double[]> centers = new ArrayList<>();
		for (Vector center : clusters.clusterCenters()) {
			centers.add(center.toArray());
		}
		response.setCenters(centers);

		double cost = clusters.computeCost(parsedData.rdd());
		logger.info("Cost: " + cost);
		response.setCost(cost);
		;

		// Evaluate clustering by computing Within Set Sum of Squared Errors
		double WSSSE = clusters.computeCost(parsedData.rdd());
		logger.info("Within Set Sum of Squared Errors = " + WSSSE);
		response.setWssse(WSSSE);

		// scatters
		JavaPairRDD<Object, Object> predictedLabelAndFeatures = parsedData
				.mapToPair(new PairFunction<Vector, Object, Object>() {

					private static final long serialVersionUID = 1308266331630983942L;

					@Override
					public Tuple2<Object, Object> call(Vector v) throws Exception {
						int clusterIndex = clusters.predict(v);
						return new Tuple2<Object, Object>(clusterIndex, v);
					}
				});
		List<PredictionInfo<?, ?>> predictionInfo = PredictionUtil.getPredictionInfo(predictedLabelAndFeatures);
		response.setPredictionInfo(predictionInfo);

		logger.info("trained successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// it has to convert to lower case, because Elasticsearch does not allow any
		// upper character as index
		String url = SparkEsConnector.getURL(ALGORITHM_NAME.toLowerCase(), modelName);
		Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
		SparkEsConnector.writeDataToEs(url, map);

		return response;
	}

	/**
	 * to cluster
	 * 
	 * @param config
	 * @param model
	 * @param predictData
	 * @return
	 */
	public static IResponseObject predictClusters(IConfigurable config, KMeansModel model,
			JavaRDD<Vector> predictData) {

		logger.info("Starting prediction ..");
		JavaPairRDD<Object, Object> predictedLabelAndFeatures = predictData
				.mapToPair(new PairFunction<Vector, Object, Object>() {

					private static final long serialVersionUID = -8433865427228534031L;

					@Override
					public Tuple2<Object, Object> call(Vector v) throws Exception {
						int clusterIndex = model.predict(v);
						return new Tuple2<Object, Object>(clusterIndex, v);
					}
				});

		List<PredictionInfo<?, ?>> predictionInfo = PredictionUtil.getPredictionInfo(predictedLabelAndFeatures);
		ClusterResponse response = new ClusterResponse(ResponseType.OBJECT_DATA);
		response.setPredictionInfo(predictionInfo);

		logger.info("predicted successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		return response;
	}

}
