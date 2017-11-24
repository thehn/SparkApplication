package kr.gaion.ceh.web.controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

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

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.response.SvmClassifierResponse;
import kr.gaion.ceh.common.bean.settings.SvmClassifierSettings;
import kr.gaion.ceh.common.util.CsvFormatConversion;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.model.EsWebConnector;
import kr.gaion.ceh.web.response_processing.DatatableDataProcessing;
import kr.gaion.ceh.web.response_processing.ResponseParser;

@Controller
public class SvmClassifierRequestController {

	/**
	 * class global variables
	 */
	final static Logger logger = LoggerFactory.getLogger(SvmClassifierRequestController.class);
	static Gson gson = new Gson();
	static String fileNameWithoutExtension = "svm_predicted";

	/**
	 * Send SVM-Classifier request for training to Spark Rest API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-SvmTrain", method = RequestMethod.POST)
	public ModelAndView requestSvmTrain(@ModelAttribute("appProperties") SvmClassifierSettings settings,
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
			SvmClassifierResponse objResponse = gson.fromJson(jsonResponse, SvmClassifierResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// get metrics from response, parse them to web model
			parseEvaluationMetrics(objResponse, model);
			parsePredictionMetrics(objResponse, model, false);

			// reload setting and page
			return new ModelAndView("classification_svm_trainingResult");

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			logger.error(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}

	}

	/**
	 * Send SVM-Classifier request for predicting to Spark Rest API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-SvmPredict", method = RequestMethod.POST)
	public ModelAndView requestSvmPredict(@ModelAttribute("appProperties") SvmClassifierSettings settings,
			BindingResult result, ModelMap model) {

		String jsonResponse = "";
		try {
			// TODO - hard setting
			settings.setDataInputOption(SvmClassifierSettings.INPUT_FROM_FILE);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String json = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);
			jsonResponse = restClient.postForObject(uri, json, String.class);

			// Get response - JSON object
			SvmClassifierResponse objResponse = gson.fromJson(jsonResponse, SvmClassifierResponse.class);

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
	@RequestMapping(value = "/schedule-svm", method = RequestMethod.POST)
	@ResponseBody
	public String requestSchedule(@ModelAttribute("appProperties") SvmClassifierSettings settings, BindingResult result,
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
	 * to return detail metrics of trained/saved model
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/request-SvmModelDetail", method = RequestMethod.POST)
	public ModelAndView requestSvmModelDetail(@ModelAttribute("appProperties") SvmClassifierSettings settings,
			BindingResult result, ModelMap model) throws UnknownHostException {

		/*String modelName = settings.getModelName();

		EsQueryConditions<String> query = new EsQueryConditions<>();
		query.set("index", settings.getAlgorithm().toLowerCase());
		query.set("type", modelName);
		query.set("count", "1");
		EsWebConnector esConnection = EsWebConnector.getInstance(BaseController.webConfig);
		String results = esConnection.selectDataAsJson(query);
		JsonArray jsonObj = gson.fromJson(results, JsonArray.class);
		String strResponse = jsonObj.get(0).getAsJsonObject().get("response").getAsString();*/ // #PC0022
		String strResponse = EsWebConnector.getInstance(BaseController.webConfig).selectMlResponse(settings); // #PC0022
		SvmClassifierResponse response = gson.fromJson(strResponse, SvmClassifierResponse.class);

		// get metrics from response, parse them to web model
		if (response.getType() == ResponseType.MESSAGE) {
			model.addAttribute("msg", "This model does not have any evaluation metric!");
			return new ModelAndView("subPage-responseViewer");
		} else {
			// continue ...
		}
		parseEvaluationMetrics(response, model);
		model.addAttribute("featuresForPredict", new Gson().toJson(response.getListFeatures())); // #P00005

		return new ModelAndView("classification_svm_modelEvaluationMetrics");
	}

	/**
	 * To parse all evaluation metrics from response to web model
	 * 
	 * @param objResponse
	 * @param model
	 */
	@SuppressWarnings("boxing")
	static void parseEvaluationMetrics(SvmClassifierResponse objResponse, ModelMap model) {

		// coordinate to draw chart
		String[] listCharts = new String[] { SvmClassifierResponse.ROC_CURVE_BY_THRESHOLD,
				SvmClassifierResponse.F1_SCORE_BY_THRESHOLD, SvmClassifierResponse.F2_SCORE_BY_THRESHOLD,
				SvmClassifierResponse.RECALL_BY_THRESHOLD, SvmClassifierResponse.PRECISION_BY_THRESHOLD };

		Map<String, String[]> mapCoordinates = ResponseParser.parseCoordinatesForMultipleLineChart(objResponse,
				listCharts);

		model.addAttribute("xValuesRoc",
				mapCoordinates.get(SvmClassifierResponse.ROC_CURVE_BY_THRESHOLD)[Constants.XAXIS_PART]);
		model.addAttribute("yValuesRoc",
				mapCoordinates.get(SvmClassifierResponse.ROC_CURVE_BY_THRESHOLD)[Constants.YAXIS_PART]);
		model.addAttribute("threshold",
				mapCoordinates.get(SvmClassifierResponse.F1_SCORE_BY_THRESHOLD)[Constants.XAXIS_PART]);
		model.addAttribute("f1Score",
				mapCoordinates.get(SvmClassifierResponse.F1_SCORE_BY_THRESHOLD)[Constants.YAXIS_PART]);
		model.addAttribute("f2Score",
				mapCoordinates.get(SvmClassifierResponse.F2_SCORE_BY_THRESHOLD)[Constants.YAXIS_PART]);
		model.addAttribute("recall",
				mapCoordinates.get(SvmClassifierResponse.RECALL_BY_THRESHOLD)[Constants.YAXIS_PART]);
		model.addAttribute("precision",
				mapCoordinates.get(SvmClassifierResponse.PRECISION_BY_THRESHOLD)[Constants.YAXIS_PART]);

		// get area under ROC metrics from response
		String areaUnderRoc = objResponse.getAreaUnderRoc() + "";
		model.addAttribute("areaUnderRoc", areaUnderRoc);

		// get area under precision recall curve metrics from response
		double areaUnderPrecisionRecallCurve = objResponse.getAreaUnderPrecisionRecallCurve();
		model.addAttribute("areaUnderPrecisionRecallCurve", areaUnderPrecisionRecallCurve);

		double weightedFalsePositiveRate = objResponse.getWeightedFalsePositiveRate();
		model.addAttribute("weightedFalsePositiveRate", weightedFalsePositiveRate);

		double weightedFMeasure = objResponse.getWeightedFMeasure();
		model.addAttribute("weightedFMeasure", weightedFMeasure);

		double accuracy = objResponse.getAccuracy();
		model.addAttribute("accuracy", accuracy);

		double weightedPrecision = objResponse.getWeightedPrecision();
		model.addAttribute("weightedPrecision", weightedPrecision);

		double weightedRecall = objResponse.getWeightedRecall();
		model.addAttribute("weightedRecall", weightedRecall);

		double weightedTruePositiveRate = objResponse.getWeightedTruePositiveRate();
		model.addAttribute("weightedTruePositiveRate", weightedTruePositiveRate);

		// Confusion matrix
		String confusionMatrix = gson.toJson(objResponse.getConfusionMatrix());
		model.addAttribute("confusionMatrix", confusionMatrix);

		// labels
		String labels = gson.toJson(objResponse.getLabels());
		model.addAttribute("labels", labels);

	}

	/**
	 * to parse prediction metrics from response to web model
	 * 
	 * @param objResponse
	 * @param model
	 * @throws IOException
	 */
	static void parsePredictionMetrics(SvmClassifierResponse objResponse, ModelMap model, boolean saveToFile)
			throws IOException {

		// #PC0002 - Start
		/*List<PredictionInfo<?, ?>> listPredictionInfo = objResponse.getPredictionInfo();
		// save data to file, use can download
		if (saveToFile) {
			FileOperations.savePredictedInfoToCSV(listPredictionInfo, fileNameWithoutExtension);
			model.addAttribute("predictedFileName", fileNameWithoutExtension);
		} else {
			// continue
		}
		// predictionInfo
		String predictionInfo = ResponseParser.parsePredictedActualFeatureValues(listPredictionInfo);*/ // #PC0002
		
		if (saveToFile) {
			String[] header = objResponse.getListFeatures();
			if (header != null && header.length > 0) {
				CsvFormatConversion.saveListDataToFile(header, objResponse.getPredictedFeatureLine(),
						fileNameWithoutExtension);
			} else {
				CsvFormatConversion.saveListDataToFile(objResponse.getPredictedFeatureLine(), fileNameWithoutExtension);
			}
			model.addAttribute("predictedFileName", fileNameWithoutExtension);
			// PC0005
			String predictionInfo = CsvFormatConversion
					.covertListStringToJavascrtipTableData(objResponse.getPredictedFeatureLine());
			model.addAttribute("predictionInfo", predictionInfo);
		} else {
			// PC0002 - Add-Start
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

}
