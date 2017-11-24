package kr.gaion.ceh.restapi.elasticsearch;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.restapi.MainEntry;

public class ElasticsearchUtil {

	final static Logger logger = LoggerFactory.getLogger(ElasticsearchUtil.class);

	/**
	 * to delete old data from Elasticsearch
	 * 
	 * @param config
	 * @throws UnknownHostException
	 */
	public static void deleteOldDataFromEs(IConfigurable config) throws UnknownHostException {

		String index = config.getSetting(AlgorithmSettings.ALGORITHM);
		String type = config.getSetting(AlgorithmSettings.MODEL_NAME);

		logger.info(
				String.format("Automatically delete old data from Elasticsearch: index: %s, type: %s", index, type));
		EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", index.toLowerCase());
		query.set("type", type);
		EsConnector esConnection = EsConnector.getInstance(MainEntry.restAppConfig);
		esConnection.delete(query);
	}

	/**
	 * Relate to #PC0022
	 * 
	 * @param index
	 * @param type
	 * @throws UnknownHostException
	 */
	public static void deleteOldDataFromEs(String index, String type) throws UnknownHostException {

		logger.info(
				String.format("Automatically delete old data from Elasticsearch: index: %s, type: %s", index, type));
		EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", index.toLowerCase());
		query.set("type", type);
		EsConnector esConnection = EsConnector.getInstance(MainEntry.restAppConfig);
		esConnection.delete(query);
	}

	/**
	 * Relate to #PC0022
	 * 
	 * @param index
	 * @param type
	 * @param query
	 * @throws UnknownHostException
	 */
	public static void deleteOldDataFromEs(String index, String type, EsQueryConditions<String> query)
			throws UnknownHostException {

		logger.info(
				String.format("Automatically delete old data from Elasticsearch: index: %s, type: %s", index, type));
		query.set("index", index.toLowerCase());
		query.set("type", type);
		EsConnector esConnection = EsConnector.getInstance(MainEntry.restAppConfig);
		esConnection.delete(query);
	}

	/**
	 * Relate to #PC0022
	 * @param query
	 * @throws UnknownHostException
	 */
	public static void deleteOldDataFromEs(EsQueryConditions<String> query) throws UnknownHostException {

		logger.info("Delete old data from Elasticsearch");
		EsConnector esConnection = EsConnector.getInstance(MainEntry.restAppConfig);
		esConnection.delete(query);
	}
}
