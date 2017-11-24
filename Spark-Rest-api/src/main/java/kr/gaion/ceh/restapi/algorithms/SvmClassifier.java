package kr.gaion.ceh.restapi.algorithms;

import java.util.List;

import org.apache.spark.SparkContext;

import scala.Tuple2;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.classification.SVMWithSGD;
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.linalg.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.response.SvmClassifierResponse;
import kr.gaion.ceh.common.bean.settings.ModelInfo;
import kr.gaion.ceh.common.bean.settings.SvmClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * @author hoang
 *
 */
public class SvmClassifier {

	/**
	 * For output log
	 */
	final static Logger logger = LoggerFactory.getLogger(SvmClassifier.class);
	public final static String ALGORITHM_NAME = "SvmClassifier";

	/**
	 * Train a Support Vector Machine (SVM) using Stochastic Gradient Descent
	 * regularization is used, which can be changed via `SVMWithSGD.optimizer`
	 * 
	 * @param config
	 * @throws Exception
	 */
	public static IResponseObject trainWithSVMSGD(IConfigurable config) throws Exception {

		logger.info("Start training ...");
		String modelFullPathName = "";
		String modelName = config.getSetting(SvmClassifierSettings.MODEL_NAME);
		double fraction = config.getSetting(SvmClassifierSettings.FRACTION);
		long seed = config.getSetting(SvmClassifierSettings.SEED);
		SvmClassifierResponse response = new SvmClassifierResponse(ResponseType.OBJECT_DATA);
		int numIterations = config.getSetting(SvmClassifierSettings.NUMBER_ITERATIONS);
		
		// #PC0016 - Start
		boolean featuresSelectionEnabelFlg = config.getSetting(SvmClassifierSettings.FEATURE_SELECTION_ENABEL_FLG);
		String[] listSelectedFeatures = null;
		if(featuresSelectionEnabelFlg) {
			listSelectedFeatures = FSChiSqSelector.selectFeatures(config);
			config.set(SvmClassifierSettings.LIST_FEATURES_COL, listSelectedFeatures);
		} else {
			listSelectedFeatures = config.getSetting(SvmClassifierSettings.LIST_FEATURES_COL);
		}
		// #PC0016 - End
		
		// Get data from ElasticSearch
		JavaRDD<LabeledPoint> data = SparkEsConnector.getLabeledPointFromEsCsvFormat(config);
		JavaRDD<LabeledPoint> trainDataset = null;
		JavaRDD<LabeledPoint> testDataset = null;
		
		if (fraction < 100.0) {
			// random split data
			double ratio = fraction * 0.01;
			JavaRDD<LabeledPoint>[] split = data.randomSplit(new double[] { ratio, 1 - ratio }, seed);
			trainDataset = split[0];
			testDataset = split[1];
		} else {
			trainDataset = data;
		}

		// cache data to memory
		trainDataset.cache();

		// Run training algorithm to build the model.
		final SVMModel model = SVMWithSGD.train(trainDataset.rdd(), numIterations);

		// Save model, and its information
		logger.info("Save model");
		modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, ALGORITHM_NAME, Constants.MODEL_DIR,
				modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		SparkContext sc = SparkEsConnector.getSparkContext();
		model.save(sc, modelFullPathName);
		ModelInfo modelInfo = new ModelInfo(ALGORITHM_NAME, config);
		modelInfo.saveJson(modelName);

		// predict with split data: test data set
		// note that prediction has to precede evaluation
		// because evaluation will clear the default threshold => compute with
		// raw data, hence it cannot predict to class probabilities
		if (fraction < 100.0) {
			predictLabeledData(config, model, testDataset, response);
			// evaluate the training model
			evaluate(config, model, testDataset, response);
		} else {
			response = new SvmClassifierResponse(ResponseType.MESSAGE);
			response.set("message", "trained successfully", String.class);
		}
		response.set(SvmClassifierResponse.LIST_FEATURES, listSelectedFeatures, String[].class); // #PC0002 // #PC0016
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		/*ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// String index = config.getSetting(SvmClassifierSettings.ES_WRITING_INDEX);
		// it has to convert to lower case, because Elasticsearch does not allow any upper character as index
		// #PC0007 - Start
		String url = SparkEsConnector.getURL(ALGORITHM_NAME.toLowerCase(), modelName);
		Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
		SparkEsConnector.writeDataToEs(url, map); // #PC0007
		Gson gson = new Gson(); // #PC0007
		String responseData = gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<String, Object>(); // #PC0007
		map.put("response", responseData); // #PC0007
		String insertInfo = EsConnector.getInstance(MainEntry.restAppConfig).insert(gson.toJson(map),
				SvmClassifierSettings.ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		logger.info(insertInfo);
		// #PC0007 - End*/ // #PC0022
		EsConnector.getInstance(MainEntry.restAppConfig).insertNewMlResponse(response,
				SvmClassifierSettings.ALGORITHM_NAME, modelName); // #PC0022
		return response;
	}

	/**
	 * to predict labeled data<br>
	 * use for evaluating trained model
	 * 
	 * @param config
	 * @param model
	 * @param testData
	 * @param response
	 */
	public static void predictLabeledData(IConfigurable config, SVMModel model, JavaRDD<LabeledPoint> testData,
			IResponseObject response) {

		logger.info("Start predicting labeled data ..");

		if (response == null) {
			response = new SvmClassifierResponse(ResponseType.OBJECT_DATA);
		} else {
			// continue ..
		}
		
		JavaRDD<Tuple2<Object, Object>> predictionAndLabels = testData
				.map(new Function<LabeledPoint, Tuple2<Object, Object>>() {
					private static final long serialVersionUID = 7096752668508743505L;
					public Tuple2<Object, Object> call(LabeledPoint p) {
						Double prediction = model.predict(p.features());
						return new Tuple2<Object, Object>(prediction, p.label());
					}
				});

		// Get evaluation metrics.
		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());

		// TODO - evaluate by labels: reservation
		/*
		 * for (int i = 0; i < metrics.labels().length; i++) {
		 * System.out.format("Class %f precision = %f\n", metrics.labels()[i],
		 * metrics.precision(metrics.labels()[i]));
		 * System.out.format("Class %f recall = %f\n", metrics.labels()[i],
		 * metrics.recall(metrics.labels()[i]));
		 * System.out.format("Class %f F1 score = %f\n", metrics.labels()[i],
		 * metrics.fMeasure(metrics.labels()[i])); }
		 */
		// labels
		response.set(SvmClassifierResponse.LABELS, metrics.labels(), double[].class);

		// confusion matrix
		response.set(SvmClassifierResponse.CONFUSION_MATRIX, metrics.confusionMatrix().toArray(), double[].class);

		// Overall statistics
		response.set(SvmClassifierResponse.ACCURACY, Utilities.roundDouble(metrics.accuracy(), 2), double.class);

		// Weighted metrics
		response.set(SvmClassifierResponse.WEIGHTED_PRECISION, Utilities.roundDouble(metrics.weightedPrecision(), 2),
				double.class);
		response.set(SvmClassifierResponse.WEIGHTED_RECALL, Utilities.roundDouble(metrics.weightedRecall(), 2),
				double.class);
		response.set(SvmClassifierResponse.WEIGHTED_F_MEASURE, Utilities.roundDouble(metrics.weightedFMeasure(), 2),
				double.class);
		response.set(SvmClassifierResponse.WEIGHTED_FALSE_POSITIVE,
				Utilities.roundDouble(metrics.weightedFalsePositiveRate(), 2), double.class);
		response.set(SvmClassifierResponse.WEIGHTED_TRUE_POSISTIVE,
				Utilities.roundDouble(metrics.weightedTruePositiveRate(), 2), double.class);

		// return predicted labels, actual labels and vectors to response
		// #PC0002 - Start
		/*@SuppressWarnings("serial")
		JavaRDD<Tuple3<Object, Object, Object>> predictedLabelAndVector = testData
				.map(new Function<LabeledPoint, Tuple3<Object, Object, Object>>() {
					public Tuple3<Object, Object, Object> call(LabeledPoint p) {
						Double prediction = model.predict(p.features());
						return new Tuple3<Object, Object, Object>(prediction, p.label(), p.features());
					}
				});
		List<PredictionInfo<?, ?>> predictionInfo = PredictionUtil.getPredictionInfo(predictedLabelAndVector);
		response.set(SvmClassifierResponse.PREDICTION_INFOR, predictionInfo, List.class);*/ // #PC0002
		
		JavaRDD<String> predictActualFeature = testData.map(new Function<LabeledPoint, String>() {
			private static final long serialVersionUID = -1425346075772351796L;
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
		response.set(SvmClassifierResponse.PREDICTED_ACTUAL_FEATURE_INFO, predictActualFeature.take(maxResults), // #PC0002 // #PC0006
				List.class);
		// #PC0002 - End
 
		logger.info("predicted labeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);
		return;
	}

	/**
	 * to evaluate trained model
	 * 
	 * @param config
	 * @param model
	 * @param testData
	 * @param response
	 */
	public static void evaluate(IConfigurable config, SVMModel model, JavaRDD<LabeledPoint> testData,
			IResponseObject response) {

		logger.info("Start evaluating ..");

		if (response == null) {
			response = new SvmClassifierResponse(ResponseType.OBJECT_DATA);
		} else {
			// continue ..
		}

		// Clear the default threshold to compute raw scores
		model.clearThreshold();

		// Compute raw scores on the test set.
		JavaRDD<Tuple2<Object, Object>> predictionAndLabels = testData
				.map(new Function<LabeledPoint, Tuple2<Object, Object>>() {
					private static final long serialVersionUID = 6386275588685876924L;
					@Override
					public Tuple2<Object, Object> call(LabeledPoint p) {
						Double prediction = model.predict(p.features());
						return new Tuple2<Object, Object>(prediction, p.label());
					}
				});

		// Get evaluation metrics.
		BinaryClassificationMetrics metrics = new BinaryClassificationMetrics(predictionAndLabels.rdd());

		// Precision by threshold
		JavaRDD<Tuple2<Object, Object>> precision = metrics.precisionByThreshold().toJavaRDD();
		response.set(SvmClassifierResponse.PRECISION_BY_THRESHOLD, precision.collect(), List.class);

		// Recall by threshold
		JavaRDD<Tuple2<Object, Object>> recall = metrics.recallByThreshold().toJavaRDD();
		response.set(SvmClassifierResponse.RECALL_BY_THRESHOLD, recall.collect(), List.class);

		// F Score by threshold
		JavaRDD<Tuple2<Object, Object>> f1Score = metrics.fMeasureByThreshold().toJavaRDD();
		response.set(SvmClassifierResponse.F1_SCORE_BY_THRESHOLD, f1Score.collect(), List.class);

		JavaRDD<Tuple2<Object, Object>> f2Score = metrics.fMeasureByThreshold(2.0).toJavaRDD();
		response.set(SvmClassifierResponse.F2_SCORE_BY_THRESHOLD, f2Score.collect(), List.class);

		// Precision-recall curve
		JavaRDD<Tuple2<Object, Object>> prc = metrics.pr().toJavaRDD();
		response.set(SvmClassifierResponse.PRECISION_RECALL_CURVE, prc.collect(), List.class);

		// Thresholds
		JavaRDD<Double> thresholds = precision.map(new Function<Tuple2<Object, Object>, Double>() {
			private static final long serialVersionUID = -3252224500161927762L;
			@Override
			public Double call(Tuple2<Object, Object> t) {
				return new Double(t._1().toString());
			}
		});
		response.set(SvmClassifierResponse.LIST_THRESHOLD, thresholds.collect(), List.class);

		// ROC Curve
		JavaRDD<Tuple2<Object, Object>> roc = metrics.roc().toJavaRDD();
		response.set(SvmClassifierResponse.ROC_CURVE_BY_THRESHOLD, roc.collect(), List.class);

		// AUPRC
		response.set(SvmClassifierResponse.AREA_UNDER_PRECISION_RECALL_CURVE,
				Utilities.roundDouble(metrics.areaUnderPR(), 2), double.class);

		// AUROC
		response.set(SvmClassifierResponse.AREA_UNDER_ROC, Utilities.roundDouble(metrics.areaUnderROC(), 2),
				double.class);

		logger.info("evaluated successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);
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
	public static IResponseObject predictUnlabeledData(IConfigurable config, SVMModel model, JavaRDD<Vector> testData) {

		logger.info("Start predicting unlabeled data ..");

		// set specified threshold
		IResponseObject response = new SvmClassifierResponse(ResponseType.OBJECT_DATA);
		double threshold = config.getSetting(SvmClassifierSettings.THRESHOLD);
		if (threshold != 0.0) {
			logger.info("Predicting with threshold = " + threshold);
			model.setThreshold(threshold);
		} else {
			// use default threshold (0.0)
		}

		// #PC0002 - Start
		/*JavaPairRDD<Object, Object> predictionAndVector = testData
				.mapToPair(new PairFunction<Vector, Object, Object>() {

					@Override
					public Tuple2<Object, Object> call(Vector vector) throws Exception {
						double predictVal = model.predict(vector);
						return new Tuple2<Object, Object>(predictVal, vector);
					}
				});

		// return prediction information to response
		List<PredictionInfo<?, ?>> predictionInfo = PredictionUtil.getPredictionInfo(predictionAndVector);
		response.set(SvmClassifierResponse.PREDICTION_INFOR, predictionInfo, List.class);*/ // #PC0002
		
		JavaRDD<String> lineData = testData.map(new Function<Vector, String>() {
			private static final long serialVersionUID = 4815941728840045376L;
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
		response.set(SvmClassifierResponse.PREDICTED_FEATURE_INFO, lineData.collect(), List.class); // #PC0002
		response.set(SvmClassifierResponse.LIST_FEATURES,
				config.getSetting(SvmClassifierSettings.LIST_FIELD_FOR_PREDICT), String[].class); // #PC0002
		// #PC0002 - End

		logger.info("predicted successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);
		return response;
	}

}
