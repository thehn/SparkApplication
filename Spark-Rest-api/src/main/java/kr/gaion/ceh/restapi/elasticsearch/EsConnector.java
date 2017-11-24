package kr.gaion.ceh.restapi.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.restapi.interfaces.IQueryable;
import static org.elasticsearch.index.query.QueryBuilders.*; 

public class EsConnector {
	private TransportClient client;
	private Settings settings;
	private final static Logger logger = LoggerFactory.getLogger(EsConnector.class);

	/**
	 * default mapping type for saving model information to Elasticsearch (Item: #PC0022)<br>
	 * 
	 * @author hoang
	 * @since Elasticsearch 6.0
	 * @see <a href=
	 *      "https://www.elastic.co/guide/en/elasticsearch/reference/current/removal-of-types.html">Removal
	 *      of mapping types</a>
	 */
	public static final String DEFAULT_MODEL_MAPPING_TYPE = "model";

	/**
	 * Class INSTANCE
	 */
	private static EsConnector INSTANCE = null;

	/**
	 * Constructor, to initialize the connection to ElasticeSearch
	 * 
	 * @param config
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("resource")
	private EsConnector(IConfigurable config) throws UnknownHostException {

		String clusterName = config.getSetting(Constants.CONF_ES_CLUSTER_NAME);
		String host = config.getSetting(Constants.CONF_ES_HOST);
		String strPort = config.getSetting(Constants.CONF_ES_TRANSPORT_PORT);
		int port = 0;
		if (strPort != null) {
			port = Integer.parseInt(strPort);
		} else {
			port = 9300;
		}

		settings = Settings.builder().put("cluster.name", clusterName).build();
		client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port)); // #PC0014
		/*
		 * InetSocketTransportAddress clientAddress = new
		 * InetSocketTransportAddress(InetAddress.getByName(host), port); // #PC0014
		 * client.addTransportAddress(clientAddress);
		 */ // #PC0014
	}

	/**
	 * to get current INSTANCE<br>
	 * Singleton pattern (lazy initialization mechanism technique within thread
	 * safe)
	 * 
	 * @param config
	 * @return
	 * @throws UnknownHostException
	 */
	public synchronized static EsConnector getInstance(IConfigurable config) throws UnknownHostException {
		if (INSTANCE == null) {
			INSTANCE = new EsConnector(config);
			return INSTANCE;
		} else {
			return INSTANCE;
		}
	}

	/**
	 * to terminate current connection
	 */
	public void closeTransaction() {
		if (this.client != null) {
			client.close();
		}
	}

	/**
	 * to get data from ElasticSearch by specified queries
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T select(IQueryable query) {
		String index = query.get("index");
		String type = query.get("type");
		String strCount = query.get("count");
		int count = Integer.parseInt(strCount);
		MultiSearchResponse results = null;

		SearchRequestBuilder srb = client.prepareSearch(index).setTypes(type).setSize(count);
		results = client.prepareMultiSearch()./* add(srb1). */add(srb).get();

		return (T) results;
	}

	/**
	 * To delete data from ElasticSearch by specified queries
	 * 
	 * @param queryr
	 * @return
	 */
	public boolean delete(IQueryable query) {
		String index = query.get("index");
		String type = query.get("type");

		try {
			// #PC0015 - Start
			/*
			 * BulkByScrollResponse response =
			 * DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
			 * .filter(QueryBuilders.matchQuery("_type", type)).source(index).get(); long
			 * deleted = response.getDeleted();
			 * logger.info(String.format("Deleted _index: %s, _type: %s, affected: %d ",
			 * index, type, deleted));
			 */ // #PC0015
			DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(matchQuery("_type", type))
					.source(index).execute(new ActionListener<BulkByScrollResponse>() {
						@Override
						public void onResponse(BulkByScrollResponse response) {
							long deleted = response.getDeleted();
							logger.info(String.format("Deleted _index: %s, _type: %s, affected: %d ", index, type,
									deleted));
						}

						@Override
						public void onFailure(Exception e) {
							logger.warn("Delete failed. Cause: " + e.toString());
						}
					});
			// #PC0015 - End
		} catch (IndexNotFoundException e) {
			logger.warn(String.format("The index %s not found.", index));
		}

		return false;
	}

	/**
	 * to index JSON data to Elasticsearch
	 * 
	 * @param jsonData
	 * @param _index
	 * @param _type
	 * @return
	 */
	public String insert(String jsonData, String _index, String _type) {
		IndexResponse response = this.client.prepareIndex(_index, _type).setSource(jsonData, XContentType.JSON).get();
		String ret = response.toString();
		return ret;
	}

	/**
	 * Refer #PC0022
	 * 
	 * @param response
	 * @param algorithmName
	 * @param modelName
	 * @return
	 * @throws UnknownHostException
	 */
	public String insertNewMlResponse(ResponseBase response, String algorithmName, String modelName)
			throws UnknownHostException {
		
		// Delete old data
		deleteOldMlResponse(algorithmName, modelName);
		
		// Write new data
		logger.info(String.format("Write new data: Algorithm name: %s, Model name: %s.", algorithmName, modelName));
		Gson gson = new Gson(); // #PC0007
		String responseData = gson.toJson(response); // #PC0007
		Map<String, Object> map = new HashMap<>(); // #PC0007
		map.put("response", responseData); // #PC0007
		map.put("modelName", modelName); // #PC0022
		String insertInfo = insert(gson.toJson(map), algorithmName.toLowerCase(), DEFAULT_MODEL_MAPPING_TYPE); // #PC0007 // #PC0022
		logger.info(insertInfo);
		
		return insertInfo;
	}
	
	/**
	 * Refer #PC0022
	 * @param algorithmName
	 * @param modelName
	 */
	public void deleteOldMlResponse(String algorithmName, String modelName) {
		logger.info(String.format("Delete old data: Algorithm name: %s, Model name: %s.", algorithmName, modelName));
		// Delete as _index: algorithmName; _type(mapping type): DEFAULT_MODEL_MAPPING_TYPE; type: modelName
		try {
			DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
			.filter(boolQuery().must(typeQuery(DEFAULT_MODEL_MAPPING_TYPE)).must(termQuery("modelName", modelName)))
			.source(algorithmName.toLowerCase()).execute(new ActionListener<BulkByScrollResponse>() {
						@Override
						public void onResponse(BulkByScrollResponse response) {
							long deleted = response.getDeleted();
							logger.info(String.format("Deleted _index: %s, mapping_type: %s, modelName: %s, affected: %d ",
									algorithmName,
									DEFAULT_MODEL_MAPPING_TYPE,
									modelName,
									deleted));
						}
						@Override
						public void onFailure(Exception e) {
							logger.warn("Delete failed. Cause: " + e.getMessage());
						}
					});
		} catch (IndexNotFoundException e) {
			logger.warn(String.format("The index %s not found.", algorithmName));
		}
	}

}
