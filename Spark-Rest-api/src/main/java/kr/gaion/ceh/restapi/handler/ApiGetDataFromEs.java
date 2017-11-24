package kr.gaion.ceh.restapi.handler;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * This class uses for getting data from ElasticSearch
 * 
 * @author hoang
 *
 */
public class ApiGetDataFromEs {

	final static Logger logger = LoggerFactory.getLogger(ApiGetDataFromEs.class);

	/**
	 * get data from ElasticSearch by query sent from client<br>
	 * the query must be following format: _index/_type/_source_name<br>
	 * 
	 * @param query
	 * @return
	 * @throws Exception 
	 */
	public static List<Map<String, Object>> search(String query) throws Exception {

		logger.info("Do search for: " + query);
		List<Map<String, Object>> listData = new ArrayList<>();

		String[] searchInfo = query.split("/");
		if (searchInfo.length < 3) {
			throw new InputMismatchException("You have to enter the invalid format of query search");
		} else {
			String index = searchInfo[0];
			String type = searchInfo[1];
			String key = searchInfo[2];
			logger.info("index: " + index + ";type: " + type + ";key: " + key);
			JavaPairRDD<String, Map<String, Object>> jvPairRdd = SparkEsConnector.getDataFromEs(index, type);
			listData = jvPairRdd.values().collect();
		}

		logger.info("Search done");
		return listData;
	}
	
	/**
	 * To make search query, or make URL for work with ElasticSearch
	 * @param str
	 * @return
	 */
	public static String makeQuery(String ... str){
		StringBuilder strBuilder = new StringBuilder();
		for (String element : str){
			strBuilder.append(element);
			strBuilder.append("/");
		}
		if (str.length >= 2){
			strBuilder.deleteCharAt(strBuilder.length()-1);
		}
		return strBuilder.toString();
	}
}
