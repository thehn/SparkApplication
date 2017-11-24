package kr.gaion.ceh.restapi.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import kr.gaion.ceh.common.bean.settings.DBSCANSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.algorithms.DBSCANClustering;
import kr.gaion.ceh.restapi.elasticsearch.ElasticsearchUtil;
import kr.gaion.ceh.restapi.interfaces.IPredictable;
import kr.gaion.ceh.restapi.interfaces.ITrainable;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

public class ApiDBSCAN implements ITrainable, IPredictable {

	final static Logger logger = LoggerFactory.getLogger(ApiDBSCAN.class);

	/**
	 * train API
	 */
	public IResponseObject train(IConfigurable config) throws Exception {

		logger.info("Start KMediods API-train ..");

		// Run spark application with received configuration
		IResponseObject response = DBSCANClustering.train(config);
		/*boolean saveDataToEs = config.getSetting(DBSCANSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(DBSCANSettings.ES_WRITING_INDEX);
			String type = config.getSetting(DBSCANSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}*/

		return response;
	}

	/**
	 * predict API
	 */
	public IResponseObject predict(IConfigurable config) throws Exception {

		IResponseObject response = DBSCANClustering.predictClusters(config);

		// save to ElasticSearch
		boolean saveDataToEs = config.getSetting(DBSCANSettings.SAVE_TO_ES);
		if (saveDataToEs) {
			ElasticsearchUtil.deleteOldDataFromEs(config);
			logger.info("Write metrics to ElasticSearch");
			String index = config.getSetting(DBSCANSettings.ES_WRITING_INDEX);
			String type = config.getSetting(DBSCANSettings.ES_WRITING_TYPE);
			String url = SparkEsConnector.getURL(index, type);
			Map<String, Object> map = ImmutableMap.of("response", new Gson().toJson(response));
			SparkEsConnector.writeDataToEs(url, map);
		} else {
			// continue
		}

		return response;
	}

}
