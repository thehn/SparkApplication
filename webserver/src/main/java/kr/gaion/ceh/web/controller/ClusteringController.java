package kr.gaion.ceh.web.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.gaion.ceh.common.bean.response.ClusterResponse;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.ClusteringAlgorithm;
import kr.gaion.ceh.common.bean.settings.PredictionInfo;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.io.FileOperations;
import kr.gaion.ceh.web.model.EsWebConnector;
import kr.gaion.ceh.web.response_processing.ResponseParser;

/**
 * This controller handles the launch Spark-Application
 * 
 * @author hoang
 *
 */
@Controller
public class ClusteringController {

	public static Gson gson = new Gson();
	static final String fileNameWithoutExtension = "predicted_cluster";
	final static Logger logger = LoggerFactory.getLogger(ClusteringController.class);

	/**
	 * Send -Clustering request to Spark Rest API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-Train", method = RequestMethod.POST)
	public ModelAndView requestTrain(@ModelAttribute("appProperties") ClusteringAlgorithm settings,
			BindingResult result, ModelMap model) {

		String jsonResponse = "";
		try {

			// TODO - only support first element from list Lookup definition
			LookupDataController.applyLookupForSettings(settings.getListLookups(), settings);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String json = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);
			jsonResponse = restClient.postForObject(uri, json, String.class);

			// Get response - JSON object
			ClusterResponse objResponse = gson.fromJson(jsonResponse, ClusterResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// get metrics from response, parse them to web model
			parseEvaluationMetrics(settings.getAlgorithm(), objResponse, model);

			// reload setting and page
			return new ModelAndView("clustering_trainingResult");
		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			System.out.println(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}
	}

	/**
	 * to handle prediction request
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-Predict", method = RequestMethod.POST)
	public ModelAndView requestPredict(@ModelAttribute("appProperties") ClusteringAlgorithm settings,
			BindingResult result, ModelMap model) {

		String jsonResponse = "";
		try {
			// TODO - hard setting
			settings.setDataInputOption(ClusteringAlgorithm.INPUT_FROM_FILE);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String json = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);
			jsonResponse = restClient.postForObject(uri, json, String.class);

			// Get response - JSON object
			ClusterResponse objResponse = gson.fromJson(jsonResponse, ClusterResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// get metrics from response, parse them to web model
			parsePredictionMetrics(objResponse, model, true);

			// reload setting and page
			return new ModelAndView("classification_predictedLabelsViewer");

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			logger.error(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}

	}

	/**
	 * request for scheduling
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/schedule-clustering", method = RequestMethod.POST)
	@ResponseBody
	public String requestSchedule(@ModelAttribute("appProperties") ClusteringAlgorithm settings, BindingResult result,
			ModelMap model) {

		logger.info("Handling request schedule for FP-Growth: Train new model.");
		String jsonResponse = "";
		try {

			// because of the FP-Growth only has training method
			settings.setIsSchedule(true);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String jsonRequest = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);

			jsonResponse = restClient.postForObject(uri, jsonRequest, String.class);

			return jsonResponse;

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			System.out.println(msg);
			return msg;
		}
	}

	/**
	 * to parse prediction metrics from response to web model
	 * 
	 * @param objResponse
	 * @param model
	 * @throws IOException
	 */
	static void parsePredictionMetrics(ClusterResponse objResponse, ModelMap model, boolean saveToFile)
			throws IOException {

		List<PredictionInfo<?, ?>> listPredictionInfo = objResponse.getPredictionInfo();

		// save data to file, use can download
		if (saveToFile) {
			FileOperations.savePredictedInfoToCSV(listPredictionInfo, fileNameWithoutExtension);
			model.addAttribute("predictedFileName", fileNameWithoutExtension);
		} else {
			// continue
		}

		// predictionInfo
		String predictionInfo = ResponseParser.parsePredictedActualFeatureValues(listPredictionInfo);
		model.addAttribute("predictionInfo", predictionInfo);

	}

	/**
	 * to handle request predict
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/request-ModelDetail/{clusterAlgorithmName}", method = RequestMethod.POST)
	public ModelAndView requestModelDetail(@ModelAttribute("appProperties") ClusteringAlgorithm settings,
			@PathVariable("clusterAlgorithmName") String clusterAlgorithmName, BindingResult result, ModelMap model)
			throws UnknownHostException {

		/*String modelName = settings.getModelName();

		EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", clusterAlgorithmName.toLowerCase());
		query.set("type", modelName);
		query.set("count", "1");
		EsWebConnector esConnection = EsWebConnector.getInstance(BaseController.webConfig);
		String results = esConnection.selectDataAsJson(query);
		JsonArray jsonObj = gson.fromJson(results, JsonArray.class);
		String strResponse = jsonObj.get(0).getAsJsonObject().get("response").getAsString();*/  // #PC0022
		String strResponse = EsWebConnector.getInstance(BaseController.webConfig).selectMlResponse(settings); // #PC0022
		ClusterResponse response = gson.fromJson(strResponse, ClusterResponse.class);

		// get metrics from response, parse them to web model
		if (response.getType() == ResponseType.MESSAGE) {
			model.addAttribute("msg", "This model does not have any evaluation metric!");
			return new ModelAndView("subPage-responseViewer");
		} else {
			// continue ...
		}
		parseEvaluationMetrics(clusterAlgorithmName, response, model);

		return new ModelAndView("clustering_modelEvaluationMetrics");
	}

	/**
	 * to parse evaluation metrics to web model
	 * 
	 * @param objResponse
	 * @param model
	 */
	static void parseEvaluationMetrics(String algorithmName, ClusterResponse objResponse, ModelMap model) {

		List<PredictionInfo<?, ?>> listClustered = objResponse.getPredictionInfo();
		JsonObject jsonObj = null;
		JsonElement values = null;
		Map<Double, List<double[]>> map = new HashMap<>();

		List<double[]> listCoordinate = null;
		for (PredictionInfo<?, ?> element : listClustered) {
			Double label = (Double) element.getPredictedValue();
			if (map.containsKey(label)) {
				listCoordinate = map.get(label);
			} else {
				listCoordinate = new ArrayList<double[]>();
				map.put(label, listCoordinate);
			}
			jsonObj = gson.fromJson(element.getFeatures().toString(), JsonObject.class);
			values = jsonObj.get("values");
			double[] coordinate = gson.fromJson(values, double[].class);
			listCoordinate.add(coordinate);
		}

		List<List<double[]>> listData = new ArrayList<>();
		SortedSet<Double> keys = new TreeSet<Double>(map.keySet());
		for (Double key : keys) {
			listData.add(map.get(key));
		}

		model.addAttribute("listData", gson.toJson(listData));
		model.addAttribute("minCluster", gson.toJson(keys.first()));
	}
}
