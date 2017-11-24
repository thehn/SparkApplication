package kr.gaion.ceh.restapi.handler;

import java.util.Map;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.SVMModel;
import org.apache.spark.mllib.linalg.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.SvmClassifierSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.algorithms.SvmClassifier;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

public class ApiSvmClassifier implements ITrainable, IPredictable {

	final static Logger logger = LoggerFactory.getLogger(ApiSvmClassifier.class);

	@Override
	public IResponseObject train(IConfigurable config) throws Exception {
		IResponseObject response = SvmClassifier.trainWithSVMSGD(config);
		/*boolean saveDataToEs = config.getSetting(SvmClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(SvmClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(SvmClassifierSettings.ES_WRITING_TYPE);
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

		SVMModel model = null;
		String modelNameSvm = config.getSetting(SvmClassifierSettings.MODEL_NAME);
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, SvmClassifier.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelNameSvm);
		String dataInputOption = config.getSetting(SvmClassifierSettings.DATA_INPUT_OPTION);
		SparkContext sc = SparkEsConnector.getSparkContext();

		// load saved model
		model = SVMModel.load(sc, modelDir);

		// unlabeled data
		JavaRDD<Vector> data = null;

		switch (dataInputOption) {
		case SvmClassifierSettings.INPUT_FROM_FILE: {
			// get test data from uploaded file
			data = SparkEsConnector.loadUnlabeledDataFromFile(config);
			break;
		}
		case SvmClassifierSettings.INPUT_FROM_ES: {
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

		// get data from uploaded file

		// Predict
		IResponseObject response = SvmClassifier.predictUnlabeledData(config, model, data);
		boolean saveDataToEs = config.getSetting(SvmClassifierSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(SvmClassifierSettings.ES_WRITING_INDEX);
			String type = config.getSetting(SvmClassifierSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}

}
