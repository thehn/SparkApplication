package kr.gaion.ceh.restapi.handler;

import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.RandomForestClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.algorithms.RandomForestClassifier;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * 
 * @author hoang
 *
 */
public class ApiRandomForestClassifier implements ITrainable, IPredictable {

	final static Logger logger = LoggerFactory.getLogger(ApiRandomForestClassifier.class);

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		IResponseObject response = RandomForestClassifier.trainModel(config);

		// save response to ElasticSearch
		/*boolean saveDataToEs = config.getSetting(RandomForestClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(RandomForestClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(RandomForestClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}*/

		return response;
	}

	@Override
	public IResponseObject predict(IConfigurable config) throws Exception {

		RandomForestModel model = null;

		// get model index from settings
		String modelNameRfc = config.getSetting(RandomForestClassifierSettings.MODEL_NAME);
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, RandomForestClassifier.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelNameRfc);
		String dataInputOption = config.getSetting(RandomForestClassifierSettings.DATA_INPUT_OPTION);
		SparkContext sc = SparkEsConnector.getSparkContext();

		// load model from local directory
		model = RandomForestModel.load(sc, modelDir);

		// get test data from ElasticSearch
		JavaRDD<Vector> data = null;
		switch (dataInputOption) {
		case RandomForestClassifierSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.loadUnlabeledDataFromFile(config);
			break;
		}
		case RandomForestClassifierSettings.INPUT_FROM_ES: {
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

		// run prediction
		IResponseObject response = RandomForestClassifier.predictUnlabeledData(config, model, data);

		// save response to ElasticSearch
		boolean saveDataToEs = config.getSetting(RandomForestClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(RandomForestClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(RandomForestClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}

}
