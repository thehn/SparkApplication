package kr.gaion.ceh.restapi.handler;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.DataFormat;
import kr.gaion.ceh.common.bean.settings.EsIndexInfo;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.data.processing.BasketDataProcessor;
import kr.gaion.ceh.restapi.elasticsearch.EsConnector;
import kr.gaion.ceh.restapi.elasticsearch.EsQueryConditions;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

/**
 * This class contains API to save data sent from web server to ElasticSearch
 * 
 * @author hoang
 *
 */
public class ApiIndexToEs {

	/**
	 * For outputting log
	 */
	final static Logger logger = LoggerFactory.getLogger(ApiIndexToEs.class);

	/**
	 * to index data from CSV file to Elasticsearch
	 * @param info
	 * @param fileList
	 * @throws Exception
	 */
	public static void indexCsvFileToEs(EsIndexInfo info, List<String> fileList) throws Exception {
		
		// Start watch
		long startTime = System.currentTimeMillis();
		
		// Get settings
		String _index = info.getIndexW();
		String _type = info.getTypeW();
		String delimiter = info.getDelimiter();
		String url = SparkEsConnector.getURL(_index, _type);
		
		// Delete old data

		if (info.getClearOldData()) {
			EsConnector esCon = EsConnector.getInstance(MainEntry.restAppConfig);
			EsQueryConditions<String> query = new EsQueryConditions<>();
			logger.info(String.format("delete old data from _index: %s, type: %s", _index, _type));
			query.set("index", _index);
			query.set("type", _type);
			esCon.delete(query);
			query.set("type", _type + "_header"); // #PC0001
			esCon.delete(query);
		} else {
			// keep old data, continue
		}
		
		// Start index
		logger.info("index to Elasticsearch with data format is CSV");
		for (String file : fileList) {
			logger.info("call Spark connector:");
			SparkEsConnector.writeCsvFileToEs(url, file, delimiter);
		}
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		logger.info("elapsed time: estimatedTime " + estimatedTime + " (ms)");
	}
	
	/**
	 * to index data from file to Elasticsearch
	 * 
	 * @param info
	 * @param fileList
	 * @throws Exception
	 */
	@Deprecated
	public static void indexDataToEs(EsIndexInfo info, List<String> fileList) throws Exception {

		// start timer
		logger.info("start timmer");
		long startTime = System.currentTimeMillis();

		// get information
		logger.info("get information");
		logger.info("Object value: " + info.toJson());
		String _index = info.getIndexW();
		String _type = info.getTypeW();
		String _source = info.getSourceName();
		String delimiter = info.getDelimiter();
		int indexOfColumnID = 0, indexOfColumnCategory = 0;
		DataFormat format = info.getFormat();
		EsConnector esCon = EsConnector.getInstance(MainEntry.restAppConfig);
		EsQueryConditions<String> query = new EsQueryConditions<>();

		// delete old data
		if (info.getClearOldData()) {
			logger.info(String.format("delete old data from _index: %s, type: %s", _index, _type));
			query.set("index", _index);
			query.set("type", _type);
			esCon.delete(query);
		} else {
			// keep old data, continue
		}

		if (format == DataFormat.BASKET) {
			indexOfColumnID = info.getIndexOfColumnID();
			indexOfColumnCategory = info.getIndexOfColumnCategory();
			logger.info("Basket data format. ID: " + indexOfColumnID + ", Category: " + indexOfColumnCategory);

		} else if (format == DataFormat.DENSE) {
			// delete old information about labeled index
			logger.info(String.format("delete old information about labeled index from _index: %s, type: %s", _index,
					_type + Constants.SPA_ES_LABELED_TYPE));
			query.set("index", _index);
			query.set("type", _type + Constants.SPA_ES_LABELED_TYPE);
			esCon.delete(query);

			// save labeled field information:
			// _type="[type_name]_labeled_field";source="index"
			// not that the "index" is started from zero (0)
			String urlOfLabeledInfo = SparkEsConnector.getURL(_index, _type + Constants.SPA_ES_LABELED_TYPE);
			logger.info("save labeled field information. URL: " + urlOfLabeledInfo);
			Map<String, Object> labeledInfo = ImmutableMap.of(Constants.SPA_ES_LABELED_INDEX, info.getLabeledIndex());
			logger.info("Writing to ES labeled field: " + labeledInfo);
			SparkEsConnector.writeDataToEs(urlOfLabeledInfo, labeledInfo);

		} else {
			// other formats, do nothing
			// continue
		}

		// Finally:
		// iterate all uploaded files
		// BufferedReader is best way to read large text file
		String url = SparkEsConnector.getURL(_index, _type);
		logger.info("iterate all uploaded files");
		logger.info("URL: " + url);

		switch (format) {
		case BASKET: {
			logger.info("index to Elasticsearch with data format is BASKET");
			for (String file : fileList) {

				// Process data first
				BasketDataProcessor basketDataProcessor = new BasketDataProcessor();
				File tmpFile = Utilities.makeTempleFile();
				basketDataProcessor.processDataFromFile(file, tmpFile.getAbsolutePath(), delimiter, indexOfColumnID,
						indexOfColumnCategory);

				// write processed data to ElasticSearch
				logger.info("call Spark connector:");
				SparkEsConnector.writeDataToEs(url, _source, tmpFile.getAbsolutePath());

				// delete temple file
				logger.info("delete temple file");
				tmpFile.delete();
			}
			break;
		}

		case DENSE: {
			// delete old information about type of format
			logger.info(String.format("delete old information about labeled index from _index: %s, type: %s", _index,
					_type + Constants.SPA_ES_DATA_FORMAT_TYPE));
			query.set("type", _type + Constants.SPA_ES_DATA_FORMAT_TYPE);
			esCon.delete(query);

			// save format of data: SPARSE ? DENSE
			String urlOfDataFormat = SparkEsConnector.getURL(_index, _type + Constants.SPA_ES_DATA_FORMAT_TYPE);
			logger.info("save data format information. URL: " + urlOfDataFormat);
			Map<String, Object> dataFormat = ImmutableMap.of(Constants.SPA_ES_DATA_FORMAT, EsIndexInfo.DENSE_FORMAT);
			logger.info("Writing to ES data format: " + dataFormat);
			SparkEsConnector.writeDataToEs(urlOfDataFormat, dataFormat);

			// index file to ES
			logger.info("index to Elasticsearch with data format is DENSE");
			for (String file : fileList) {
				logger.info("call Spark connector:");
				SparkEsConnector.writeDataToEs(url, _source, file);
			}
			break;
		}

		case SPARSE: {
			// save format of data: SPARSE ? DENSE
			String urlOfDataFormat = SparkEsConnector.getURL(_index, _type + Constants.SPA_ES_DATA_FORMAT_TYPE);
			logger.info("save data format information. URL: " + urlOfDataFormat);
			Map<String, Object> dataFormat = ImmutableMap.of(Constants.SPA_ES_DATA_FORMAT, EsIndexInfo.SPARSE_FORMAT);
			logger.info("Writing to ES data format: " + dataFormat);
			SparkEsConnector.writeDataToEs(urlOfDataFormat, dataFormat);

			logger.info("index to Elasticsearch with data format is SPARSE");
			for (String file : fileList) {
				logger.info("call Spark connector:");
				SparkEsConnector.writeLabeledPointDataToEs(url, _source, file);
			}
			break;
		}
		case CSV: {
			logger.info("index to Elasticsearch with data format is CSV");
			for (String file : fileList) {
				logger.info("call Spark connector:");
				SparkEsConnector.writeCsvFileToEs(url, file, delimiter);
			}
			break;
		}
		default:
			logger.info("Format of file is not accepted");
			break;
		}

		logger.info("stop timmer");
		long estimatedTime = System.currentTimeMillis() - startTime;
		logger.info("elapsed time: estimatedTime " + estimatedTime + " (ms)");

		return;
	}
}
