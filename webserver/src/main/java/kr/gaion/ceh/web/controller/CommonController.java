package kr.gaion.ceh.web.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.ModelInfo;
import kr.gaion.ceh.common.util.Utilities;

@Controller
public class CommonController {

	private static Gson gson = new Gson();
	final static Logger logger = LoggerFactory.getLogger(CommonController.class);

	/**
	 * to get information of specified model
	 * 
	 * @param algorithmName
	 * @param modelName
	 * @return
	 */
	public static ModelInfo getModelInfo(String algorithmName, String modelName) {

		ModelInfo modelInfo = null;
		HashMap<String, String> customRequest = new HashMap<>();
		customRequest.put("algorithm", algorithmName);
		customRequest.put("modelName", modelName);

		// Send request to Spark server
		RestTemplate restClient = new RestTemplate();
		String json = gson.toJson(customRequest);
		String uri = Utilities.getUri(BaseController.webConfig, Constants.API_GET_MODEL_INFO);
		String jsonResponse = restClient.postForObject(uri, json, String.class);

		modelInfo = gson.fromJson(jsonResponse, ModelInfo.class);

		return modelInfo;
	}

	/**
	 * to delete specified model from Rest API
	 * 
	 * @param modelName
	 * @param algorithmName
	 * @return
	 */
	@RequestMapping(value = "/deleteModel/{algorithmName}/{modelName}", method = RequestMethod.GET)
	@ResponseBody
	public String deleteSvmModel(@PathVariable("modelName") String modelName,
			@PathVariable("algorithmName") String algorithmName) {
		// String response = deleteModel(SvmClassifierSettings.ALGORITHM_NAME,
		// modelName);
		HashMap<String, String> customRequest = new HashMap<>();
		customRequest.put("algorithm", algorithmName);
		customRequest.put("modelName", modelName);

		// Send request to Spark server
		RestTemplate restClient = new RestTemplate();
		String json = gson.toJson(customRequest);
		String uri = Utilities.getUri(BaseController.webConfig, Constants.API_DEL_MODEL);
		String response = restClient.postForObject(uri, json, String.class);

		return response;
	}

	/**
	 * to get list saved models from Spark-REST server
	 * 
	 * @param algorithmName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getListModels/{algorithmName}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getListSvmModels(@PathVariable("algorithmName") String algorithmName) {

		HashMap<String, String> customRequest = new HashMap<>();
		customRequest.put("algorithm", algorithmName);

		// Send request to Spark server
		RestTemplate restClient = new RestTemplate();
		String json = gson.toJson(customRequest);
		String uri = Utilities.getUri(BaseController.webConfig, Constants.API_GET_MODELS);
		String listModels = restClient.postForObject(uri, json, String.class);

		return gson.fromJson(listModels, List.class);
	}
}
