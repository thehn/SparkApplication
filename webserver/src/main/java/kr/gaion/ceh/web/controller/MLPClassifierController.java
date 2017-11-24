package kr.gaion.ceh.web.controller;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kr.gaion.ceh.common.bean.response.MLPClassifierResponse;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.MLPClassifierSettings;
import kr.gaion.ceh.common.util.CsvFormatConversion;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.model.EsWebConnector;
import kr.gaion.ceh.web.response_processing.DatatableDataProcessing;

@Controller
public class MLPClassifierController {

	public static Gson gson = new Gson();
	static final String fileNameWithoutExtension = "mlp_predicted";
	final static Logger logger = LoggerFactory.getLogger(MLPClassifierController.class);

	/**
	 * Send MLP-Classifier request to Spark Rest API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-MLPTrain", method = RequestMethod.POST)
	public ModelAndView requestMLPClassifier(@ModelAttribute("appProperties") MLPClassifierSettings settings,
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
			MLPClassifierResponse objResponse = gson.fromJson(jsonResponse, MLPClassifierResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// get metrics from response, parse them to web model
			parseEvaluationMetrics(objResponse, model);

			// reload setting and page
			return new ModelAndView("classification_mlp_trainingResult");
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
	@RequestMapping(value = "/request-MLPPredict", method = RequestMethod.POST)
	public ModelAndView requestMLPPredict(@ModelAttribute("appProperties") MLPClassifierSettings settings,
			BindingResult result, ModelMap model) {

		String jsonResponse = "";
		try {
			// TODO - hard setting
			settings.setDataInputOption(MLPClassifierSettings.INPUT_FROM_FILE);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String json = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);
			jsonResponse = restClient.postForObject(uri, json, String.class);

			// Get response - JSON object
			MLPClassifierResponse objResponse = gson.fromJson(jsonResponse, MLPClassifierResponse.class);

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
	@RequestMapping(value = "/schedule-mlp", method = RequestMethod.POST)
	@ResponseBody
	public String requestSchedule(@ModelAttribute("appProperties") MLPClassifierSettings settings, BindingResult result,
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
	static void parsePredictionMetrics(MLPClassifierResponse objResponse, ModelMap model, boolean saveToFile)
			throws IOException {

		// List<PredictionInfo<?, ?>> listPredictionInfo =
		// objResponse.getPredictionInfo(); // #PC0002

		// save data to file, use can download
		if (saveToFile) {
			// #PC0002 - Start
			// FileOperations.savePredictedInfoToCSV(listPredictionInfo,
			// fileNameWithoutExtension);
			String[] header = objResponse.getListFeatures();
			if (header != null && header.length > 0) {
				CsvFormatConversion.saveListDataToFile(header, objResponse.getPredictedFeatureLine(),
						fileNameWithoutExtension);
			} else {
				CsvFormatConversion.saveListDataToFile(objResponse.getPredictedFeatureLine(), fileNameWithoutExtension);
			}
			// #PC0002 - End
			model.addAttribute("predictedFileName", fileNameWithoutExtension);
		} else {
			// Confusion matrix
			String confusionMatrix = gson.toJson(objResponse.getConfusionMatrix());
			model.addAttribute("confusionMatrix", confusionMatrix);

			// labels
			String labels = gson.toJson(objResponse.getLabels());
			model.addAttribute("labels", labels);
		}
		
		// predictionInfo
		// String predictionInfo =
		// ResponseParser.parsePredictedActualFeatureValues(listPredictionInfo); //
		// #PC0002
		String predictionInfo = CsvFormatConversion
				.covertListStringToJavascrtipTableData(objResponse.getPredictedFeatureLine()); // #PC0002
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
	@RequestMapping(value = "/request-MLPModelDetail", method = RequestMethod.POST)
	public ModelAndView requestMLPModelDetail(@ModelAttribute("appProperties") MLPClassifierSettings settings,
			BindingResult result, ModelMap model) throws UnknownHostException {

		/*String modelName = settings.getModelName();

		EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", settings.getAlgorithm().toLowerCase());
		query.set("type", modelName);
		query.set("count", "1");
		EsWebConnector esConnection = EsWebConnector.getInstance(BaseController.webConfig);
		String results = esConnection.selectDataAsJson(query);
		JsonArray jsonObj = gson.fromJson(results, JsonArray.class);
		String strResponse = jsonObj.get(0).getAsJsonObject().get("response").getAsString();*/  // #PC0022
		String strResponse = EsWebConnector.getInstance(BaseController.webConfig).selectMlResponse(settings); // #PC0022
		MLPClassifierResponse response = gson.fromJson(strResponse, MLPClassifierResponse.class);

		// get metrics from response, parse them to web model
		if (response.getType() == ResponseType.MESSAGE) {
			model.addAttribute("msg", "This model does not have any evaluation metric!");
			return new ModelAndView("subPage-responseViewer");
		} else {
			// continue ...
		}
		parseEvaluationMetrics(response, model);
		model.addAttribute("featuresForPredict", new Gson().toJson(response.getListFeatures())); // #P00005

		return new ModelAndView("classification_mlp_modelEvaluationMetrics");
	}

	/**
	 * to parse evaluation metrics to web model
	 * 
	 * @param objResponse
	 * @param model
	 */
	@SuppressWarnings("boxing")
	static void parseEvaluationMetrics(MLPClassifierResponse objResponse, ModelMap model) {
		// get metrics from response, parse them to web model
		model.addAttribute("confusionMatrix", gson.toJson(objResponse.getConfusionMatrix()));
		model.addAttribute("labels", gson.toJson(objResponse.getLabels()));
		model.addAttribute("weightedFMeasure", objResponse.getWeightedFMeasure());
		model.addAttribute("accuracy", objResponse.getAccuracy());
		model.addAttribute("weightedPrecision", objResponse.getWeightedPrecision());
		model.addAttribute("weightedRecall", objResponse.getWeightedRecall());
		model.addAttribute("weightedFalsePositiveRate", objResponse.getWeightedFalsePositiveRate());
		model.addAttribute("weightedTruePositiveRate", objResponse.getWeightedTruePositiveRate());
		
		// predictionInfo
		// PC0002 - Add-Start
		/*List<PredictionInfo<?, ?>> listPredictionInfo = objResponse.getPredictionInfo();
		String predictionInfo = ResponseParser.parsePredictedActualFeatureValues(listPredictionInfo);
		model.addAttribute("predictionInfo", predictionInfo);*/
		String predictionInfo = CsvFormatConversion
				.covertListStringToJavascrtipTableData(objResponse.getPredictedActualFeatureLine());
		model.addAttribute("predictionInfo", predictionInfo);
		// list of columns to fill into data table
		String columnsList = DatatableDataProcessing.makeColumnsList(new String[] { "prediction", "actual" },
				objResponse.getListFeatures());
		model.addAttribute("columnsList", columnsList);
		// PC0002 - Add-End
	}

}
