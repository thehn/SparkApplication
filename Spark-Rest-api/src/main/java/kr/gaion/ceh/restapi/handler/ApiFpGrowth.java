package kr.gaion.ceh.restapi.handler;

import org.apache.spark.mllib.fpm.FPGrowthModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.FpGrowthSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.algorithms.FpGrowth;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.interfaces.ITrainable;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

public class ApiFpGrowth implements ITrainable {
	final static Logger logger = LoggerFactory.getLogger(ApiFpGrowth.class);

	/**
	 * to train FPGrowthModel model
	 * 
	 * @throws Exception
	 */
	public IResponseObject train(IConfigurable config) throws Exception {

		logger.info("Start FP-Growth ..");

		// Run spark application with received configuration
		logger.info("Get JavaSparkContext");
		// SparkESConnector.getJavaSparkContext();

		logger.info("Create model");
		FPGrowthModel<String> model = FpGrowth.trainModel(config);

		boolean saveDataToEs = config.getSetting(FpGrowthSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			FpGrowth.writeFrequentItemsets(config, model);
		} else {
			// continue
		}

		logger.info("Get metrics from Elasticsearch, send them to client as response");
		IResponseObject response = FpGrowth.getFrequentPatterns(model, config);

		logger.info("End program normally.");

		return response;
	}

	/**
	 * To generate Association Rules from FPGrowthModel
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public IResponseObject genAsRules(IConfigurable config) throws Exception {

		String modelName = config.getSetting(FpGrowthSettings.MODEL_NAME);
		String modelDir = Utilities.getPathInWorkingFolder(Constants.DATA_DIR, FpGrowthSettings.ALGORITHM_NAME,
				Constants.MODEL_DIR, modelName);
		
		logger.info("load FPGrowth model as name: " + modelDir);
		@SuppressWarnings("unchecked")
		FPGrowthModel<String> model = (FPGrowthModel<String>) FPGrowthModel.load(SparkEsConnector.getSparkContext(),
				modelDir);
		logger.info("Successfully loaded FPGrowth model as name: " + modelDir);
		
		boolean saveDataToEs = config.getSetting(FpGrowthSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			FpGrowth.writeAssociationRulesToEs(config, model);
		} else {
			// continue
		}
		IResponseObject response = FpGrowth.getAssociationRules(model, config);

		return response;
	}

}
