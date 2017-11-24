package kr.gaion.ceh.restapi.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.RandomForestClassifierResponse;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.bean.settings.RandomForestClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import kr.gaion.ceh.restapi.util.PredictionUtil;
import scala.Tuple2;
import scala.Tuple3;

/**
 * Random Forest Classification Algorithm
 * 
 * @author hoang
 *
 */
public class RandomForestClassifier {
	/**
	 * For writing log
	 */
	final static Logger logger = LoggerFactory.getLogger(RandomForestClassifier.class);
	public static final String ALGORITHM_NAME = "RandomForestClassifier";

	/**
	 * Train model with algorithm: Random Forest Classification
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject trainModel(IConfigurable config) throws Exception {

		logger.info("Starting train model ..");

		// return variable
		RandomForestClassifierResponse response = new RandomForestClassifierResponse(ResponseType.OBJECT_DATA);

		// Get Spark context
		JavaSparkContext jsc = SparkEsConnector.getJavaSparkContext();

		// If empty categoricalFeaturesInfo then it indicates all features are
		// continuous.
		HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<>();
		// categoricalFeaturesInfo.put(new Integer(21), new Integer(5));

		// Get all settings sent through REST-client
		Integer numClasses = config.getSetting(RandomForestClassifierSettings.NUMCLASSES); //#PC0010
		Integer numTrees = config.getSetting(RandomForestClassifierSettings.NUMTREES);
		String featureSubsetStrategy = config.getSetting(RandomForestClassifierSettings.FEATURE_SUBSET_STRATEGY);
		String impurity = config.getSetting(RandomForestClassifierSettings.IMPURITY);
		Integer maxDepths = config.getSetting(RandomForestClassifierSettings.MAXDEPTHS);
		Integer maxBins = config.getSetting(RandomForestClassifierSettings.MAXBINS);
		double fraction = config.getSetting(RandomForestClassifierSettings.FRACTION);
		long lSeed = config.getSetting(RandomForestClassifierSettings.SEED);
		
		// #PC0016 - Start
		boolean featuresSelectionEnabelFlg = config.getSetting(RandomForestClassifierSettings.FEATURE_SELECTION_ENABEL_FLG);
		String[] listSelectedFeatures = null;
		if(featuresSelectionEnabelFlg) {
			listSelectedFeatures = FSChiSqSelector.selectFeatures(config);
			config.set(RandomForestClassifierSettings.LIST_FEATURES_COL, listSelectedFeatures);
		} else {
			listSelectedFeatures = config.getSetting(RandomForestClassifierSettings.LIST_FEATURES_COL);
		}
		// #PC0016 - End

		// split data set, training and testing
		JavaRDD<LabeledPoint>[] splits = null;
		JavaRDD<LabeledPoint> trainingData = null;
		JavaRDD<LabeledPoint> testData = null;

		// Load data from ElasticSearch
		// List<String> listClasses = new ArrayList<>();
		// int[] numClassesRef = new int[1]; // #P0003 //#PC0010
		JavaRDD<LabeledPoint> data = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);
		// Integer numClasses = listClasses.size(); // #P0003

		// Split the data into training and test sets
		if (fraction <= 100.0) {
			splits = data.randomSplit(new double[] { fraction * 0.01, 1 - fraction * 0.01 }, lSeed);
			trainingData = splits[0];
			testData = splits[1];
		} else {
			trainingData = data;
		}

		/**
		 * Method to train a decision tree model for binary or multiclass
		 * classification.
		 *
		 * @param input
		 *            Training dataset: RDD of
		 *            [[org.apache.spark.mllib.regression.LabeledPoint]]. Labels should
		 *            take values {0, 1, ..., numClasses-1}.
		 * @param numClasses
		 *            Number of classes for classification.
		 * @param categoricalFeaturesInfo
		 *            Map storing arity of categorical features. An entry (n to k)
		 *            indicates that feature n is categorical with k categories indexed
		 *            from 0: {0, 1, ..., k-1}.
		 * @param numTrees
		 *            Number of trees in the random forest.
		 * @param featureSubsetStrategy
		 *            Number of features to consider for splits at each node. Supported
		 *            values: "auto", "all", "sqrt", "log2", "onethird". If "auto" is
		 *            set, this parameter is set based on numTrees: if numTrees == 1,
		 *            set to "all"; if numTrees is greater than 1 (forest) set to
		 *            "sqrt".
		 * @param impurity
		 *            Criterion used for information gain calculation. Supported values:
		 *            "gini" (recommended) or "entropy".
		 * @param maxDepth
		 *            Maximum depth of the tree (e.g. depth 0 means 1 leaf node, depth 1
		 *            means 1 internal node + 2 leaf nodes). (suggested value: 4)
		 * @param maxBins
		 *            Maximum number of bins used for splitting features (suggested
		 *            value: 100)
		 * @param seed
		 *            Random seed for bootstrapping and choosing feature subsets.
		 * @return RandomForestModel that can be used for prediction.
		 */
		// Get random seed
		int seed = ThreadLocalRandom.current().nextInt();
		final RandomForestModel model = RandomForest.trainClassifier(trainingData, /*numClassesRef[0] #PC0010*/numClasses,
				categoricalFeaturesInfo, numTrees, featureSubsetStrategy, impurity, maxDepths, maxBins, seed);

		// Save model
		logger.info("Saving model ..");
		String modelFullPathName = "";
		String modelName = config.getSetting(RandomForestClassifierSettings.MODEL_NAME);
		modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, ALGORITHM_NAME, Constants.MODEL_DIR,
				modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		model.save(jsc.sc(), modelFullPathName);

		// Test model
		if (fraction <= 100.0) {
			predictLabeledData(model, testData, response);
			evaluate(model, testData, response);
		} else {
			response = new RandomForestClassifierResponse(ResponseType.MESSAGE);
			response.set("message", "trained successfully", String.class);
		}

		// set labels (because index of listClasses and labels are ascending, so it does
		// not need to reorder
		// response.set(RandomForestClassifierResponse.LABELS, listClasses, List.class);
		// #PC0003

		// view debug string
		response.set(RandomForestClassifierResponse.DECISION_TREE, model.toDebugString(), String.class);
		response.set(RandomForestClassifierResponse.LIST_FEATURES, listSelectedFeatures, String[].class); // #PC0002 // #PC0016
		// Response to client
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		/*ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// String index = config.getSetting(AlgorithmSettings.ES_WRITING_INDEX);
		// it has to convert to lower case, because Elasticsearch does not allow any
		// upper character as index
		// String url = SparkEsConnector.getURL(ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		Gson gson = new Gson(); // #PC0007
		String responseData =  gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<>(); // #PC0007
		map.put("response", responseData); // #PC0007
		// SparkEsConnector.writeDataToEs(url, map); // #PC0007
		String insertInfo = EsConnector.getInstance(MainEntry.restAppConfig).insert(gson.toJson(map), ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		logger.info(insertInfo);*/ // #PC0022
		EsConnector.getInstance(MainEntry.restAppConfig).insertNewMlResponse(response,
				RandomForestClassifierSettings.ALGORITHM_NAME, modelName); // #PC0022

		return response;
	}

	/**
	 * To predict and respond evaluation metrics #PC0003
	 * 
	 * @param model
	 */
	public static void predictLabeledData(RandomForestModel model, JavaRDD<LabeledPoint> testData,
			List<String> listClasses, IResponseObject response) {

		logger.info("Start predicting labeled data ..");
		if (response == null) {
			response = new RandomForestClassifierResponse(ResponseType.OBJECT_DATA);
		} else {
			// continue ..
		}

		// return predicted labels, actual labels and vectors to response
		JavaRDD<Tuple3<Object, Object, Object>> predictedLabelAndVector = testData
			.map(new Function<LabeledPoint, Tuple3<Object, Object, Object>>() {
				private static final long serialVersionUID = 2542942941746661803L;

				public Tuple3<Object, Object, Object> call(LabeledPoint p) {
					Double dPrediction = model.predict(p.features());
					String strPrediction = listClasses.get(dPrediction.intValue());
					Double dLabel = p.label();
					String actualLabel = listClasses.get(dLabel.intValue());
					return new Tuple3<Object, Object, Object>(strPrediction, actualLabel, p.features());
				}
			});

		List<PredictionInfo<?, ?>> predictionInfo = PredictionUtil.getPredictionInfo(predictedLabelAndVector);
		response.set(RandomForestClassifierResponse.PREDICTION_INFO, predictionInfo, List.class);

		logger.info("predicted labeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);
		return;
	}

	/**
	 * To predict and respond evaluation metrics
	 * 
	 * @param model
	 */
	public static void predictLabeledData(RandomForestModel model, JavaRDD<LabeledPoint> testData,
			IResponseObject response) {

		logger.info("Start predicting labeled data ..");
		if (response == null) {
			response = new RandomForestClassifierResponse(ResponseType.OBJECT_DATA);
		} else {
			// continue ..
		}

		// return predicted labels, actual labels and vectors to response
		// #PC0002 - Start
		/*
		 * JavaRDD<Tuple3<Object, Object, Object>> predictedLabelAndVector = testData
		 * .map(new Function<LabeledPoint, Tuple3<Object, Object, Object>>() { private
		 * static final long serialVersionUID = -5342900108414007440L;
		 * 
		 * public Tuple3<Object, Object, Object> call(LabeledPoint p) { Double
		 * dPrediction = model.predict(p.features()); Double dLabel = p.label(); return
		 * new Tuple3<Object, Object, Object>(dPrediction, dLabel, p.features()); } });
		 */

		// List<PredictionInfo<?, ?>> predictionInfo =
		// PredictionUtil.getPredictionInfo(predictedLabelAndVector); #PC0002
		// response.set(RandomForestClassifierResponse.PREDICTION_INFO, predictionInfo,
		// List.class);

		JavaRDD<String> predictActualFeature = testData.map(new Function<LabeledPoint, String>() {

			private static final long serialVersionUID = 892113607308835014L;

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
		// #PC0002 - End

		int maxResults = Integer.parseInt(MainEntry.restAppConfig.getSetting(Constants.CONF_MAX_RESULTS)); // #PC0006
		response.set(RandomForestClassifierResponse.PREDICTED_ACTUAL_FEATURE_INFO, predictActualFeature.take(maxResults), // #PC0002 // #PC0006
				List.class);

		logger.info("predicted labeled data successfully.");
		return;
	}

	/**
	 * get all metrics - to evaluate
	 * 
	 * @param predictionAndLabel
	 * @param response
	 * @return
	 */
	public static void evaluate(RandomForestModel model, JavaRDD<LabeledPoint> testData, IResponseObject response) {

		logger.info("Start evaluating ..");
		if (response == null) {
			response = new RandomForestClassifierResponse(ResponseType.OBJECT_DATA);
		} else {
			// continue ..
		}

		// Evaluate model on test instances and compute test error
		@SuppressWarnings("serial")
		JavaPairRDD<Object, Object> predictionAndLabel = testData
				.mapToPair(new PairFunction<LabeledPoint, Object, Object>() {
					@Override
					public Tuple2<Object, Object> call(LabeledPoint p) {
						return new Tuple2<>(model.predict(p.features()), p.label());
					}
				});

		// Get evaluation metrics.
		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabel.rdd());

		// labels
		response.set(RandomForestClassifierResponse.LABELS, metrics.labels(), double[].class); // #PC0003

		// confusion matrix
		response.set(RandomForestClassifierResponse.CONFUSION_MATRIX, metrics.confusionMatrix().toArray(),
				double[].class);

		// Overall statistics
		response.set(RandomForestClassifierResponse.ACCURACY, Utilities.roundDouble(metrics.accuracy(), 2),
				double.class);

		// Weighted metrics
		response.set(RandomForestClassifierResponse.WEIGHTED_PRECISION,
				Utilities.roundDouble(metrics.weightedPrecision(), 2), double.class);
		response.set(RandomForestClassifierResponse.WEIGHTED_RECALL, Utilities.roundDouble(metrics.weightedRecall(), 2),
				double.class);
		response.set(RandomForestClassifierResponse.WEIGHTED_F_MEASURE,
				Utilities.roundDouble(metrics.weightedFMeasure(), 2), double.class);
		response.set(RandomForestClassifierResponse.WEIGHTED_FALSE_POSITIVE,
				Utilities.roundDouble(metrics.weightedFalsePositiveRate(), 2), double.class);
		response.set(RandomForestClassifierResponse.WEIGHTED_TRUE_POSISTIVE,
				Utilities.roundDouble(metrics.weightedTruePositiveRate(), 2), double.class);

		logger.info("evaluated successfully!");
		return;
	}

	/**
	 * to predict unlabeled data
	 * 
	 * @param config
	 * @param model
	 * @param testData
	 * @return
	 */
	public static IResponseObject predictUnlabeledData(IConfigurable config, RandomForestModel model,
			JavaRDD<Vector> testData) {
		logger.info("Start predicting unlabeled data ..");

		// set specified threshold
		IResponseObject response = new RandomForestClassifierResponse(ResponseType.OBJECT_DATA);

		// #PC0002 - Start
		/*
		 * JavaPairRDD<Object, Object> predictionAndVector = testData .mapToPair(new
		 * PairFunction<Vector, Object, Object>() {
		 * 
		 * @Override public Tuple2<Object, Object> call(Vector vector) throws Exception
		 * { double predictVal = model.predict(vector); return new Tuple2<Object,
		 * Object>(predictVal, vector); } });
		 */
		// 
		
		JavaRDD<String> lineData = testData.map(new Function<Vector, String>() {
			private static final long serialVersionUID = -5502602637332666668L;
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
		/*
		 * List<PredictionInfo<?, ?>> predictionInfo =
		 * PredictionUtil.getPredictionInfo(predictionAndVector);
		 */ // #PC0002
		response.set(RandomForestClassifierResponse.PREDICTED_FEATURE_INFO, lineData.collect(), List.class); // #PC0002
		response.set(RandomForestClassifierResponse.LIST_FEATURES,
				config.getSetting(RandomForestClassifierSettings.LIST_FIELD_FOR_PREDICT), String[].class); // #PC0002
		// #PC0002 - End
		
		logger.info("predicted unlabeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);
		return response;
	}

}
