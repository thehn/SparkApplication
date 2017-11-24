package kr.gaion.ceh.restapi.algorithms;

import scala.Tuple2;

import java.util.List;
import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.NaiveBayesClassifierResponse;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.NaiveBayesClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * 
 * @author hoang
 *
 */
public class NaiveBayesClassifier {

	final static Logger logger = LoggerFactory.getLogger(NaiveBayesClassifier.class);

	/**
	 * to train
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject train(IConfigurable config) throws Exception {

		// get Spark context
		JavaSparkContext jsc = SparkEsConnector.getJavaSparkContext();

		// get settings
		double fraction = config.getSetting(NaiveBayesClassifierSettings.FRACTION);
		double lambda = config.getSetting(NaiveBayesClassifierSettings.LAMBDA);
		long seed = config.getSetting(NaiveBayesClassifierSettings.SEED);
		
		// #PC0016 - Start
		boolean featuresSelectionEnabelFlg = config.getSetting(NaiveBayesClassifierSettings.FEATURE_SELECTION_ENABEL_FLG);
		String[] listSelectedFeatures = null;
		if(featuresSelectionEnabelFlg) {
			listSelectedFeatures = FSChiSqSelector.selectFeatures(config);
			config.set(NaiveBayesClassifierSettings.LIST_FEATURES_COL, listSelectedFeatures);
		} else {
			listSelectedFeatures = config.getSetting(NaiveBayesClassifierSettings.LIST_FEATURES_COL);
		}
		// #PC0016 - End

		// split data set, training and testing
		JavaRDD<LabeledPoint>[] splits = null;
		JavaRDD<LabeledPoint> trainingData = null;
		JavaRDD<LabeledPoint> testData = null;

		// Load data from ElasticSearch
		JavaRDD<LabeledPoint> data = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);

		// Split the data into training and test sets
		if (fraction <= 100.0) {
			splits = data.randomSplit(new double[] { fraction * 0.01, 1 - fraction * 0.01 }, seed);
			trainingData = splits[0];
			testData = splits[1];
		} else {
			trainingData = data;
		}

		trainingData.cache();

		NaiveBayesModel model = NaiveBayes.train(trainingData.rdd(), lambda);

		// Save model
		logger.info("Saving model ..");
		String modelFullPathName = "";
		String modelName = config.getSetting(NaiveBayesClassifierSettings.MODEL_NAME) == null
				? AlgorithmSettings.DEFAULT_MODEL_NAME
				: config.getSetting(NaiveBayesClassifierSettings.MODEL_NAME);
		modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				NaiveBayesClassifierSettings.ALGORITHM_NAME, Constants.MODEL_DIR, modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		model.save(jsc.sc(), modelFullPathName);

		// evaluate metrics then respond
		NaiveBayesClassifierResponse response = new NaiveBayesClassifierResponse(ResponseType.OBJECT_DATA);
		JavaPairRDD<Object, Object> predictionAndLabel = testData
				.mapToPair(new PairFunction<LabeledPoint, Object, Object>() {
					private static final long serialVersionUID = -1659570871850322745L;

					@Override
					public Tuple2<Object, Object> call(LabeledPoint p) {
						return new Tuple2<>(model.predict(p.features()), p.label());
					}
				});
		// Get evaluation metrics.
		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabel.rdd());
		// labels
		response.set(NaiveBayesClassifierResponse.LABELS, metrics.labels(), double[].class); // #PC0003
		// confusion matrix
		response.set(NaiveBayesClassifierResponse.CONFUSION_MATRIX, metrics.confusionMatrix().toArray(),
				double[].class);
		// Overall statistics
		response.set(NaiveBayesClassifierResponse.ACCURACY, Utilities.roundDouble(metrics.accuracy(), 2), double.class);
		// Weighted metrics
		response.set(NaiveBayesClassifierResponse.WEIGHTED_PRECISION,
				Utilities.roundDouble(metrics.weightedPrecision(), 2), double.class);
		response.set(NaiveBayesClassifierResponse.WEIGHTED_RECALL, Utilities.roundDouble(metrics.weightedRecall(), 2),
				double.class);
		response.set(NaiveBayesClassifierResponse.WEIGHTED_F_MEASURE,
				Utilities.roundDouble(metrics.weightedFMeasure(), 2), double.class);
		response.set(NaiveBayesClassifierResponse.WEIGHTED_FALSE_POSITIVE,
				Utilities.roundDouble(metrics.weightedFalsePositiveRate(), 2), double.class);
		response.set(NaiveBayesClassifierResponse.WEIGHTED_TRUE_POSISTIVE,
				Utilities.roundDouble(metrics.weightedTruePositiveRate(), 2), double.class);

		// #PC0002 - Start
		/*
		 * JavaRDD<Tuple3<Object, Object, Object>> predictionsLabelsAndVectors =
		 * testData .map(new Function<LabeledPoint, Tuple3<Object, Object, Object>>() {
		 * private static final long serialVersionUID = 7394816318475152382L;
		 * 
		 * public Tuple3<Object, Object, Object> call(LabeledPoint p) { Double
		 * prediction = model.predict(p.features()); return new Tuple3<Object, Object,
		 * Object>(prediction, p.label(), p.features()); } });
		 * 
		 * List<PredictionInfo<?, ?>> predictionInfo =
		 * PredictionUtil.getPredictionInfo(predictionsLabelsAndVectors);
		 * response.set(NaiveBayesClassifierResponse.PREDICTION_INFO, predictionInfo,
		 * List.class);
		 */

		JavaRDD<String> predictActualFeature = testData.map(new Function<LabeledPoint, String>() {
			private static final long serialVersionUID = -1026804115550078331L;

			@Override
			public String call(LabeledPoint lbpt) throws Exception {
				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(model.predict(lbpt.features()));
				lineBuilder.append(Constants.CSV_SEPARATOR);
				lineBuilder.append(lbpt.label());
				lineBuilder.append(Constants.CSV_SEPARATOR);
				for (double feature : lbpt.features().toArray()) {
					lineBuilder.append(feature);
					lineBuilder.append(Constants.CSV_SEPARATOR);
				}
				lineBuilder.deleteCharAt(lineBuilder.length() - 1);
				return lineBuilder.toString();
			}
		});
		int maxResults = Integer.parseInt(MainEntry.restAppConfig.getSetting(Constants.CONF_MAX_RESULTS)); // #PC0006
		response.set(NaiveBayesClassifierResponse.PREDICTED_ACTUAL_FEATURE_INFO, predictActualFeature.take(maxResults), // #PC0006
				List.class);
		response.set(NaiveBayesClassifierResponse.LIST_FEATURES, listSelectedFeatures, String[].class); // #PC0002 // #PC0016
		// #PC0002 - End
		response.setStatus(ResponseStatus.SUCCESS);

		/*// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// String index = config.getSetting(AlgorithmSettings.ES_WRITING_INDEX);
		// it has to convert to lower case, because Elasticsearch does not allow any
		// upper character as index
		Gson gson = new Gson(); // #PC0007
		String responseData = gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<String, Object>(); // #PC0007
		map.put("response", responseData); // #PC0007
		// SparkEsConnector.writeDataToEs(url, map); // #PC0007
		String insertInfo = EsConnector.getInstance(MainEntry.restAppConfig).insert(gson.toJson(map),
				NaiveBayesClassifierSettings.ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		logger.info(insertInfo);*/ // #PC0022
		EsConnector.getInstance(MainEntry.restAppConfig).insertNewMlResponse(response,
				NaiveBayesClassifierSettings.ALGORITHM_NAME, modelName); // #PC0022

		return response;
	}

	/**
	 * to predict
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject predict(IConfigurable config) throws Exception {
		// 0. Get settings
		String dataInputOption = config.getSetting(NaiveBayesClassifierSettings.DATA_INPUT_OPTION);
		String modelName = config.getSetting(NaiveBayesClassifierSettings.MODEL_NAME);
		SparkContext sc = SparkEsConnector.getSparkContext();

		// 1. get data
		JavaRDD<Vector> data = null;
		switch (dataInputOption) {
		case NaiveBayesClassifierSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.loadUnlabeledDataFromFile(config);
			break;
		}
		case NaiveBayesClassifierSettings.INPUT_FROM_ES: {
			// get test data from ElasticSearch
			data = SparkEsConnector.getUnlabeledDataFromES(config);
			break;
		}
		default: {
			// abnormal case:
			ResponseBase err = new ResponseBase(ResponseType.MESSAGE);
			err.setMessage("Input method is not acceptable: " + dataInputOption);
			return err;
		}
		}

		// 2. load model
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				NaiveBayesClassifierSettings.ALGORITHM_NAME, Constants.MODEL_DIR, modelName);
		NaiveBayesModel model = NaiveBayesModel.load(sc, modelDir);

		// 3. predict
		// #PC0002 - Start
		/*
		 * JavaPairRDD<Double, Object> predictionAndVector = data.mapToPair(new
		 * PairFunction<Vector, Double, Object>() { private static final long
		 * serialVersionUID = 2291957238659533426L;
		 * 
		 * @Override public Tuple2<Double, Object> call(Vector feature) throws Exception
		 * { Double prediction = model.predict(feature); return new Tuple2<>(prediction,
		 * feature); } });
		 */
		JavaRDD<String> lineData = data.map(new Function<Vector, String>() {
			private static final long serialVersionUID = -4018298952176170991L;

			@Override
			public String call(Vector vector) throws Exception {
				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(model.predict(vector));
				lineBuilder.append(Constants.CSV_SEPARATOR);
				for (double feature : vector.toArray()) {
					lineBuilder.append(feature);
					lineBuilder.append(Constants.CSV_SEPARATOR);
				}
				lineBuilder.deleteCharAt(lineBuilder.length() - 1);
				return lineBuilder.toString();
			}
		});

		// return prediction information to response
		IResponseObject response = new NaiveBayesClassifierResponse(ResponseType.OBJECT_DATA);

		// List<PredictionInfo<?, ?>> predictionInfo =
		// PredictionUtil.getPredictionInfo(predictionAndVector); // #P0002
		// response.set(NaiveBayesClassifierResponse.PREDICTION_INFO, predictionInfo,
		// List.class); // #P0002
		response.set(NaiveBayesClassifierResponse.PREDICTED_FEATURE_INFO, lineData.collect(), List.class); // #PC0002
		response.set(NaiveBayesClassifierResponse.LIST_FEATURES,
				config.getSetting(NaiveBayesClassifierSettings.LIST_FIELD_FOR_PREDICT), String[].class); // #PC0002

		logger.info("predicted unlabeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// 4. Save results (if need) & respond
		// save response to ElasticSearch
		boolean saveDataToEs = config.getSetting(NaiveBayesClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(NaiveBayesClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(NaiveBayesClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}

}