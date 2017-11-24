package kr.gaion.ceh.restapi.algorithms;

/**
 * 
 */
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.ml.classification.MultilayerPerceptronClassificationModel;
import org.apache.spark.ml.classification.MultilayerPerceptronClassifier;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.MLPClassifierResponse;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseStatus;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.MLPClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import scala.Tuple2;

import static org.apache.spark.sql.functions.*;

/**
 * 
 * @author hoang
 *
 */
public class MLPClassifier {

	final static Logger logger = LoggerFactory.getLogger(MLPClassifier.class);

	/**
	 * to train new model
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject train(IConfigurable config) throws Exception {

		// get settings from client
		long seed = config.getSetting(MLPClassifierSettings.SEED);
		int[] layers = config.getSetting(MLPClassifierSettings.LAYERS);
		int blockSize = config.getSetting(MLPClassifierSettings.BLOCK_SIZE);
		int maxIter = config.getSetting(MLPClassifierSettings.MAX_ITER);

		// #PC0016 - Start
		boolean featuresSelectionEnabelFlg = config.getSetting(MLPClassifierSettings.FEATURE_SELECTION_ENABEL_FLG);
		String[] listSelectedFeatures = null;
		if (featuresSelectionEnabelFlg) {
			listSelectedFeatures = FSChiSqSelector.selectFeatures(config);
			config.set(MLPClassifierSettings.LIST_FEATURES_COL, listSelectedFeatures);
			// special statement for Multiple Layers Perceptron only
			// it needs to change the first layer (Input layer) because the Feature
			// Selection algorithm filtered original features out
			layers[0] = listSelectedFeatures.length;
		} else {
			listSelectedFeatures = config.getSetting(MLPClassifierSettings.LIST_FEATURES_COL);
		}
		// #PC0016 - End

		Dataset<Row> dataFrame = SparkEsConnector.getDatasetFromESWithDenseFormat(config);

		// Split the data into train and test
		double fraction = config.getSetting(MLPClassifierSettings.FRACTION);
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

		// #PC0013 - Start (count distinct classes)
		String classCol = config.getSetting(AlgorithmSettings.CLASS_COL);
		/*
		 * int classesCount = (new
		 * Long(train.select(classCol).distinct().count())).intValue();
		 */ // solution 1 => bad performance, but absolutely exact
		int classesCount = new Long(train.agg(approx_count_distinct(classCol)).first().getLong(0)).intValue(); // solution
																												// 2 =>
																												// good
																												// performance,
																												// but
																												// it is
																												// just
																												// approximate
																												// value
		System.out.println("Total classes: " + classesCount);
		layers[1] = classesCount;
		// #PC0013 - End

		// create the trainer and set its parameters
		MultilayerPerceptronClassifier trainer = new MultilayerPerceptronClassifier().setLayers(layers)
				.setBlockSize(blockSize).setSeed(seed).setMaxIter(maxIter);

		// train the model
		MultilayerPerceptronClassificationModel model = trainer.fit(train);

		// Save model
		logger.info("Saving model ..");
		String modelFullPathName = "";
		String modelName = config.getSetting(MLPClassifierSettings.MODEL_NAME) == null
				? AlgorithmSettings.DEFAULT_MODEL_NAME
				: config.getSetting(MLPClassifierSettings.MODEL_NAME);
		modelFullPathName = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, MLPClassifierSettings.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		Utilities.deleteIfExisted(modelFullPathName);
		model.save(modelFullPathName);

		// compute accuracy on the test set
		Dataset<Row> result = model.transform(test);

		Dataset<Row> predictionLabelsFeatures = result.select("prediction", "label", "features");

		// Set of prediction, labels
		JavaPairRDD<Object, Object> predictionAndLabelRdd = predictionLabelsFeatures.toJavaRDD()
				.mapToPair(new PairFunction<Row, Object, Object>() {
					private static final long serialVersionUID = 2113199201851594743L;

					@Override
					public Tuple2<Object, Object> call(Row row) throws Exception {
						return new Tuple2<>(row.get(0), row.get(1));
					}
				});

		MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabelRdd.rdd());

		// Set of predictions, labels, features
		// #PC0002 - Start
		MLPClassifierResponse response = new MLPClassifierResponse(ResponseType.OBJECT_DATA);
		/*
		 * JavaRDD<Tuple3<Object, Object, Object>> predictedLabelAndVector =
		 * predictionLabelsFeatures.toJavaRDD() .map(new Function<Row, Tuple3<Object,
		 * Object, Object>>() { private static final long serialVersionUID =
		 * -7804994369331652749L;
		 * 
		 * public Tuple3<Object, Object, Object> call(Row row) { return new
		 * Tuple3<>(row.get(0), row.get(1), row.get(2)); } }); List<PredictionInfo<?,
		 * ?>> predictionInfo =
		 * PredictionUtil.getPredictionInfo(predictedLabelAndVector);
		 * response.set(MLPClassifierResponse.PREDICTION_INFO, predictionInfo,
		 * List.class);
		 */
		JavaRDD<String> predictedLabelAndVector = predictionLabelsFeatures.toJavaRDD().map(new Function<Row, String>() {
			private static final long serialVersionUID = -6554874834801818033L;

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
		response.set(MLPClassifierResponse.PREDICTED_ACTUAL_FEATURE_INFO, predictedLabelAndVector.take(maxResults), // #PC0002
																													// //
																													// #PC0006
				List.class);
		response.set(MLPClassifierResponse.LIST_FEATURES, listSelectedFeatures, String[].class); // #PC0002 // #PC0016
		// #PC0002 - End

		// labels
		response.set(MLPClassifierResponse.LABELS, metrics.labels(), double[].class);

		// confusion matrix
		response.set(MLPClassifierResponse.CONFUSION_MATRIX, metrics.confusionMatrix().toArray(), double[].class);

		// Overall statistics
		response.set(MLPClassifierResponse.ACCURACY, Utilities.roundDouble(metrics.accuracy(), 2), double.class);

		// Weighted metrics
		response.set(MLPClassifierResponse.WEIGHTED_PRECISION, Utilities.roundDouble(metrics.weightedPrecision(), 2),
				double.class);
		response.set(MLPClassifierResponse.WEIGHTED_RECALL, Utilities.roundDouble(metrics.weightedRecall(), 2),
				double.class);
		response.set(MLPClassifierResponse.WEIGHTED_F_MEASURE, Utilities.roundDouble(metrics.weightedFMeasure(), 2),
				double.class);
		response.set(MLPClassifierResponse.WEIGHTED_FALSE_POSITIVE,
				Utilities.roundDouble(metrics.weightedFalsePositiveRate(), 2), double.class);
		response.set(MLPClassifierResponse.WEIGHTED_TRUE_POSISTIVE,
				Utilities.roundDouble(metrics.weightedTruePositiveRate(), 2), double.class);

		logger.info("evaluated successfully!");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		/*// save response to ES; _index:ALGORITHM_NAME, _type:modelName
		ElasticsearchUtil.deleteOldDataFromEs(config);
		logger.info("Write metrics to ElasticSearch");
		// String index = config.getSetting(AlgorithmSettings.ES_WRITING_INDEX);
		// it has to convert to lower case, because Elasticsearch does not allow any
		// upper character as index
		 * String url =
		 * SparkEsConnector.getURL(MLPClassifierSettings.ALGORITHM_NAME.toLowerCase(),
		 * modelName); Map<String, Object> map = ImmutableMap.of("response", new
		 * Gson().toJson(response)); SparkEsConnector.writeDataToEs(url, map);
		 */ // #PC0007
		/*Gson gson = new Gson(); // #PC0007
		String responseData = gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<String, Object>(); // #PC0007
		map.put("response", responseData); // #PC0007
		String insertInfo = EsConnector.getInstance(MainEntry.restAppConfig).insert(gson.toJson(map),
				MLPClassifierSettings.ALGORITHM_NAME.toLowerCase(), modelName); // #PC0007
		logger.info(insertInfo);*/ // #PC0022
		EsConnector.getInstance(MainEntry.restAppConfig).insertNewMlResponse(response,
				MLPClassifierSettings.ALGORITHM_NAME, modelName); // #PC0022

		return response;
	}

	/**
	 * to predict, using trained model
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public static IResponseObject predict(IConfigurable config) throws Exception {

		// 0. Get settings
		String dataInputOption = config.getSetting(MLPClassifierSettings.DATA_INPUT_OPTION);
		String modelName = config.getSetting(MLPClassifierSettings.MODEL_NAME);

		// 1. get data
		JavaRDD<Vector> data = null;
		switch (dataInputOption) {
		case MLPClassifierSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.getMlVectorFromFileWithDenseFormat(config);
			break;
		}
		case MLPClassifierSettings.INPUT_FROM_ES: {
			// get test data from ElasticSearch
			// data = SparkEsConnector.getMlVectorFromESWithDenseFormat(config);
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
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, MLPClassifierSettings.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		MultilayerPerceptronClassificationModel model = MultilayerPerceptronClassificationModel.load(modelDir);

		// 3. predict
		// #PC0002 - Start
		IResponseObject response = new MLPClassifierResponse(ResponseType.OBJECT_DATA);
		/*
		 * JavaPairRDD<Double, Object> predictionAndVector = data.mapToPair(new
		 * PairFunction<Vector, Double, Object>() { private static final long
		 * serialVersionUID = 2291957238659533426L;
		 * 
		 * @Override public Tuple2<Double, Object> call(Vector feature) throws Exception
		 * { Double prediction = model.predict(feature); return new Tuple2<>(prediction,
		 * feature); } }); // return prediction information to response
		 * List<PredictionInfo<?, ?>> predictionInfo =
		 * PredictionUtil.getPredictionInfo(predictionAndVector);
		 * response.set(MLPClassifierResponse.PREDICTION_INFO, predictionInfo,
		 * List.class);
		 */
		JavaRDD<String> lineData = data.map(new Function<Vector, String>() {
			private static final long serialVersionUID = 1340407230452354596L;

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
		response.set(MLPClassifierResponse.PREDICTED_FEATURE_INFO, lineData.collect(), List.class); // #PC0002
		response.set(MLPClassifierResponse.LIST_FEATURES,
				config.getSetting(MLPClassifierSettings.LIST_FIELD_FOR_PREDICT), String[].class); // #PC0002
		// #PC0002 - End

		logger.info("predicted unlabeled data successfully.");
		response.set(ResponseBase.STATUS, ResponseStatus.SUCCESS, ResponseStatus.class);

		// 4. Save results (if need) & respond
		// save response to ElasticSearch
		boolean saveDataToEs = config.getSetting(MLPClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(MLPClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(MLPClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}
}
