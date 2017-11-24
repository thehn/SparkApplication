package kr.gaion.ceh.web.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.MultiSearchResponse.Item;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.IndexNotFoundException;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.web.interfaces.IDbOperations;
import kr.gaion.ceh.web.interfaces.IQueryConditions;
import static org.elasticsearch.index.query.QueryBuilders.*; 

/**
 * This class use to interact with ElasticSearch
 * 
 * @author hoang
 * Note: this file was cloned then modified from @see kr.gaion.ceh.restapi.elasticsearch.EsConnector
 *
 */
public class EsWebConnector implements IDbOperations {

	private TransportClient client;
	private Settings settings;
	private final static Logger logger = LoggerFactory.getLogger(EsWebConnector.class);
	
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
	private static EsWebConnector INSTANCE = null;

	/**
	 * Constructor, to initialize the connection to ElasticeSearch
	 * 
	 * @param config
	 * @throws UnknownHostException
	 */
	@SuppressWarnings("resource")
	private EsWebConnector(IConfigurable config) throws UnknownHostException {

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
		client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(host), port)); // #PC0014
		/*InetSocketTransportAddress clientAddress = new InetSocketTransportAddress(InetAddress.getByName(host), port); // #PC0014
		client.addTransportAddress(clientAddress);*/ // #PC0014
	}

	/**
	 * to get current INSTANCE<br>
	 * Singleton pattern (lazy initialization mechanism technique within thread safe)
	 * 
	 * @param config
	 * @return
	 * @throws UnknownHostException
	 */
	public synchronized static EsWebConnector getInstance(IConfigurable config) throws UnknownHostException {
		if (INSTANCE == null) {
			INSTANCE = new EsWebConnector(config);
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
	 * to get data from ElasticSearch by specified queries <br>
	 * @since #PC0022 => Deprecated
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Deprecated
	public <T> T select(IQueryConditions query) {
		String index = query.get("index");
		String type = query.get("type");
		String strCount = query.get("count");
		int count = Integer.parseInt(strCount);
		MultiSearchResponse results = null;

		SearchRequestBuilder srb = client
				.prepareSearch(index)
				.setTypes(type)
				.setSize(count);
		results = client.prepareMultiSearch()./* add(srb1). */add(srb).get();

		return (T) results;
	}

	@Override
	public <T> boolean insert(IQueryConditions query, T data) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * To delete data from ElasticSearch by specified queries
	 * 
	 * @param queryr
	 * @return
	 */
	@SuppressWarnings("boxing")
	@Override
	public boolean delete(IQueryConditions query) {
		String index = query.get("index");
		String type = query.get("type");

		try {
			// #PC0015 - Start
			/*BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(QueryBuilders.matchQuery("_type", type)).source(index).get();
			long deleted = response.getDeleted();
			logger.info(String.format("Deleted _index: %s, _type: %s, affected: %d ", index, type, deleted));*/ // #PC0015
			DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
					.filter(matchQuery("_type", type)).source(index).execute(new ActionListener<BulkByScrollResponse>() {
						@Override
						public void onResponse(BulkByScrollResponse response) {
							 long deleted = response.getDeleted();
							 logger.info(String.format("Deleted _index: %s, _type: %s, affected: %d ", index, type, deleted));
						}
						@Override
						public void onFailure(Exception e) {
							// TODO Auto-generated method stub
							logger.warn(String.format("Delete failed. Cause", e.getStackTrace().toString()));
						}
					});
			// #PC0015 - End
		} catch (IndexNotFoundException e) {
			logger.warn(String.format("The index %s not found.", index));
		}

		return false;
	}

	/**
	 * to select data from Elasticsearch then return JSON format<br>
	 * @since #PC0022 => @Deprecated
	 * 
	 * @param query
	 * @return
	 */
	@Deprecated
	public String selectDataAsJson(IQueryConditions query) {
		StringBuilder dataBuilder = new StringBuilder();

		MultiSearchResponse msr = select(query);
		for (Item item : msr.getResponses()) {
			SearchResponse response = item.getResponse();
			dataBuilder.append("[");
			for (SearchHit hit : response.getHits().getHits()) {
				dataBuilder.append(hit.getSourceAsString());
				dataBuilder.append(",");
			}
			dataBuilder.deleteCharAt(dataBuilder.length() - 1);
			dataBuilder.append("]");

			// TODO - NOTE when using this function
			// I used break here because [...]
			// break;
		}

		return dataBuilder.toString();
	}
	
	
	/**
	 * refer #PC0022
	 * @param settings
	 * @return
	 */
	public String selectMlResponse(AlgorithmSettings settings) {
		
		// get setting
		String _index = settings.getAlgorithm().toLowerCase();
		String _type = DEFAULT_MODEL_MAPPING_TYPE;
		String modelName = settings.getModelName();

		// search data from Elasticsearch
		SearchRequestBuilder srb = client
				.prepareSearch(_index)
				.setTypes(_type)
				.setSize(1)
				.setQuery(matchQuery("modelName", modelName));
		MultiSearchResponse msr = client.prepareMultiSearch().add(srb).get();
		
		// Build properly data
		StringBuilder dataBuilder = new StringBuilder();
		for (Item item : msr.getResponses()) {
			SearchResponse response = item.getResponse();
			dataBuilder.append("[");
			for (SearchHit hit : response.getHits().getHits()) {
				dataBuilder.append(hit.getSourceAsString());
				dataBuilder.append(",");
			}
			dataBuilder.deleteCharAt(dataBuilder.length() - 1);
			dataBuilder.append("]");
		}
		
		JsonArray jsonObj = new Gson().fromJson(dataBuilder.toString(), JsonArray.class);
		String strResponse = jsonObj.get(0).getAsJsonObject().get("response").getAsString();
		
		return strResponse;
	}

	public TransportClient getClient() {
		return client;
	}

	public void setClient(TransportClient client) {
		this.client = client;
	}

}
