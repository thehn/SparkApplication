package kr.gaion.ceh.restapi.algorithms;

import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.response.LogisticRegressionClassifierResponse;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.LogisticRegressionClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import scala.Tuple2;

/**
 * 
 * @author hoang
 *
 */
public class LogisticRegressionClassifier {

	final static Logger logger = LoggerFactory.getLogger(LogisticRegressionClassifier.class);

	/**
	 * to train labeled data
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject train(IConfigurable config) throws Exception {

		// get settings
		logger.info("get settings");
		long seed = config.getSetting(LogisticRegressionClassifierSettings.SEED);
		int numIterations = config.getSetting(LogisticRegressionClassifierSettings.NUMBER_ITERATIONS);
		double threshold = config.getSetting(LogisticRegressionClassifierSettings.THRESHOLD);
		double regParam = config.getSetting(LogisticRegressionClassifierSettings.REG_PARAM);
		double alpha = config.getSetting(LogisticRegressionClassifierSettings.ELASTIC_NET_MIXING);
		boolean intercept = config.getSetting(LogisticRegressionClassifierSettings.INTERCEPT);

		// #PC0016 - Start
		boolean featuresSelectionEnabelFlg = config
				.getSetting(LogisticRegressionClassifierSettings.FEATURE_SELECTION_ENABEL_FLG);
		String[] listSelectedFeatures = null;
		if (featuresSelectionEnabelFlg) {
			listSelectedFeatures = FSChiSqSelector.selectFeatures(config);
			config.set(LogisticRegressionClassifierSettings.LIST_FEATURES_COL, listSelectedFeatures);
		} else {
			listSelectedFeatures = config.getSetting(LogisticRegressionClassifierSettings.LIST_FEATURES_COL);
		}
		// #PC0016 - End

		// get data from Elasticsearch
		logger.info("get data from Elasticsearch");
		Dataset<Row> dataFrame = SparkEsConnector.getDatasetFromESWithDenseFormat(config);

		// Split the data into train and test
		logger.info("Split the data into train and test");
		double fraction = config.getSetting(LogisticRegressionClassifierSettings.FRACTION);
		Dataset<Row> train = null, test = null;
		if (fraction < 100.0) {
			// random split data
			double ratio = fraction * 0.01;
			Dataset<Row>[] splits = dataFrame.randomSplit(new double[] { ratio, 1 - ratio }, seed);
			train = splits[0];
			test = splits[1];
		} else {
			train = dataFrame;
		}

		// Create new classifier
		LogisticRegression classifier = new LogisticRegression().setMaxIter(numIterations).setThreshold(threshold).setRegParam(regParam)
				.setFitIntercept(intercept).setElasticNetParam(alpha);
		// Fit the model
		LogisticRegressionModel model = classifier.fit(train);
		

		// Save model
		logger.info("Saving model ..");
		String modelFullPathName = "";
		String modelName = config.getSetting(LogisticRegressionClassifierSettings.MODEL_NAME) == null
				? AlgorithmSettings.DEFAULT_MODEL_NAME
				: config.getSetting(LogisticRegressionClassifierSettings.MODEL_NAME);
		modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR,
				LogisticRegressionClassifierSettings.ALGORITHM_NAME, Constants.MODEL_DIR, modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		model.save(modelFullPathName);

		// compute accuracy on the test set
		Dataset<Row> result = model.transform(test);

		Dataset<Row> predictionLabelsFeatures = result.select("prediction", "label", "features");

		// Set of prediction, labels
		JavaPairRDD<Object, Object> predictionAndLabelRdd = predictionLabelsFeatures.toJavaRDD()
				.mapToPair(new PairFunction<Row, Object, Object>() {
					private static final long serialVersionUID = 8910290912040713930L;
					@Override
					public Tuple2<Object, Object> call(Row row) throws Exception {
						return new Tuple2<>(row.get(0), row.get(1));
					}
				});

		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabelRdd.rdd());

		// Set of predictions, labels, features
		// #PC0002 - Start
		LogisticRegressionClassifierResponse response = new LogisticRegressionClassifierResponse(ResponseType.OBJECT_DATA);
		JavaRDD<String> predictedLabelAndVector = predictionLabelsFeatures.toJavaRDD().map(new Function<Row, String>() {
			private static final long serialVersionUID = 8119201695243614025L;
			public String call(Row row) {
				StringBuilder lineBuilder = new StringBuilder();
				lineBuilder.append(row.get(0));
				lineBuilder.append(Constants.CSV_SEPARATOR);
				lineBuilder.append(row.get(1));
				lineBuilder.append(Constants.CSV_SEPARATOR);
				StringBuilder featuresBuilder = new StringBuilder(row.get(2).toString());
				lineBuilder
						.append(featuresBuilder.deleteCharAt(0).deleteCharAt(featuresBuilder.length() - 1).toString());
				return lineBuilder.toString();
			}
		});

		int maxResults = Integer.parseInt(MainEntry.restAppConfig.getSetting(Constants.CONF_MAX_RESULTS)); // #PC0006
		response.set(LogisticRegressionClassifierResponse.PREDICTED_ACTUAL_FEATURE_INFO,
				predictedLabelAndVector.take(maxResults), // #PC0002
				// //
				// #PC0006
				List.class);
		response.set(LogisticRegressionClassifierResponse.LIST_FEATURES, listSelectedFeatures, String[].class); // #PC0002 //
																										// #PC0016
		// #PC0002 - End

		// labels
		response.set(LogisticRegressionClassifierResponse.LABELS, metrics.labels(), double[].class);

		// confusion matrix
		response.set(LogisticRegressionClassifierResponse.CONFUSION_MATRIX, metrics.confusionMatrix().toArray(), double[].class);

		// Overall statistics
		response.set(LogisticRegressionClassifierResponse.ACCURACY, Utilities.roundDouble(metrics.accuracy(), 2), double.class);

		// Weighted metrics
		response.set(LogisticRegressionClassifierResponse.WEIGHTED_PRECISION,
				Utilities.roundDouble(metrics.weightedPrecision(), 2), double.class);
		response.set(LogisticRegressionClassifierResponse.WEIGHTED_RECALL, Utilities.roundDouble(metrics.weightedRecall(), 2),
				double.class);
		response.set(LogisticRegressionClassifierResponse.WEIGHTED_F_MEASURE,
				Utilities.roundDouble(metrics.weightedFMeasure(), 2), double.class);
		response.set(LogisticRegressionClassifierResponse.WEIGHTED_FALSE_POSITIVE,
				Utilities.roundDouble(metrics.weightedFalsePositiveRate(), 2), double.class);
		response.set(LogisticRegressionClassifierResponse.WEIGHTED_TRUE_POSISTIVE,
				Utilities.roundDouble(metrics.weightedTruePositiveRate(), 2), double.class);

		logger.info("evaluated successfully!");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		/*// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");

		Gson gson = new Gson(); // #PC0007
		String responseData = gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<>(); // #PC0007
		map.put("response", responseData); // #PC0007
		String insertInfo = EsConnector.getInstance(MainEntry.restAppConfig).insert(gson.toJson(map),
				LogisticRegressionClassifierSettings.ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		logger.info(insertInfo);*/ // #PC0022
		EsConnector.getInstance(MainEntry.restAppConfig).insertNewMlResponse(response,
				LogisticRegressionClassifierSettings.ALGORITHM_NAME, modelName); // #PC0022

		return response;
	}

	/**
	 * to predict unlabeled data
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject predict(IConfigurable config) throws Exception {

		// 0. Get settings
		String dataInputOption = config.getSetting(LogisticRegressionClassifierSettings.DATA_INPUT_OPTION);
		String modelName = config.getSetting(LogisticRegressionClassifierSettings.MODEL_NAME);

		// 1. get data
		JavaRDD<Vector> data = null;
		switch (dataInputOption) {
		case LogisticRegressionClassifierSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.getMlVectorFromFileWithDenseFormat(config);
			break;
		}
		case LogisticRegressionClassifierSettings.INPUT_FROM_ES: {
			// get test data from ElasticSearch
			// data = SparkEsConnector.getMlVectorFromESWithDenseFormat(config); // TODO
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
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, LogisticRegressionClassifierSettings.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		LogisticRegressionModel model = LogisticRegressionModel.load(modelDir);

		// 3. predict
		// #PC0002 - Start
		IResponseObject response = new LogisticRegressionClassifierResponse(ResponseType.OBJECT_DATA);
		JavaRDD<String> lineData = data.map(new Function<Vector, String>() {
			private static final long serialVersionUID = 2252101043879638450L;
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
		
		response.set(LogisticRegressionClassifierResponse.PREDICTED_FEATURE_INFO, lineData.collect(), List.class); // #PC0002
		response.set(LogisticRegressionClassifierResponse.LIST_FEATURES,
				config.getSetting(LogisticRegressionClassifierSettings.LIST_FIELD_FOR_PREDICT), String[].class); // #PC0002
		// #PC0002 - End

		logger.info("predicted unlabeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// 4. Save results (if need) & respond
		// save response to ElasticSearch
		boolean saveDataToEs = config.getSetting(LogisticRegressionClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(LogisticRegressionClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(LogisticRegressionClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}
}
