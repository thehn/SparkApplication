package kr.gaion.ceh.web.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import kr.gaion.ceh.common.bean.response.FpGrowthResponse;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.FpGrowthSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.response_processing.PatternDataProcessing;

@Controller
public class FpGrowthController {

	final static Logger logger = LoggerFactory.getLogger(FpGrowthController.class);
	static Gson gson = new Gson();

	/**
	 * Send FP-Growth request to Spark Rest API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-FpGrowth", method = RequestMethod.POST)
	public ModelAndView requestFpGrowth(@ModelAttribute("appProperties") FpGrowthSettings settings,
			BindingResult result, ModelMap model) {

		logger.info("Handling request for FP-Growth: Train new model.");
		String jsonResponse = "";
		try {

			// TODO - only support first element from list Lookup definition
			LookupDataController.applyLookupForSettings(settings.getListLookups(), settings);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String jsonRequest = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);

			jsonResponse = restClient.postForObject(uri, jsonRequest, String.class);

			// Get response - JSON object
			FpGrowthResponse objResponse = gson.fromJson(jsonResponse, FpGrowthResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// type == OBJECT_DATA
			List<Map<String, Object>> freqPattersResponse = (List<Map<String, Object>>) objResponse.getFreqPatterns();

			// Make data for filling to Data tables

			String dataFreqPatterns = buildJsArrayDataFromResponse(freqPattersResponse);
			// reload setting and page
			model.addAttribute("dataFreqPatterns", dataFreqPatterns);

			// process data for visualization
			PatternDataProcessing dataProcessing = new PatternDataProcessing();
			String processedData = dataProcessing.processedPattersData(freqPattersResponse);
			model.addAttribute("processedData", processedData);

			HashMap<String, String> matchingTable = getMatchingTable(BaseController.webConfig);

			boolean isMatchable = false;
			if (matchingTable != null)
				if (matchingTable.size() > 0) {
					isMatchable = true;
				}

			if (isMatchable) {
				model.addAttribute("matchingTable", gson.toJson(matchingTable));
			} else {
				model.addAttribute("matchingTable", gson.toJson(new HashMap<String, String>()));
			}

			return new ModelAndView("fpGrowth_tableFreqPatterns");

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			System.out.println(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}

	}

	/**
	 * to request generate Association Rules, then display them
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-asRules", method = RequestMethod.POST)
	public ModelAndView requestAsRules(@ModelAttribute("appProperties") FpGrowthSettings settings, BindingResult result,
			ModelMap model) {

		logger.info("Handling request for FP-Growth: Generate Association Rules.");
		String jsonResponse = "";
		try {

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String jsonRequest = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);

			jsonResponse = restClient.postForObject(uri, jsonRequest, String.class);

			// Get response - JSON object
			FpGrowthResponse objResponse = gson.fromJson(jsonResponse, FpGrowthResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			// type == OBJECT_DATA
			List<Map<String, Object>> asRulesResponse = (List<Map<String, Object>>) objResponse.getAsRules();

			// Make data for filling to Data tables
			String dataAsRules = buildJsArrayDataFromResponse(asRulesResponse);

			// reload setting and page
			model.addAttribute("dataAsRules", dataAsRules);
			// model.addAttribute("dataFreqPatterns", dataFreqPatterns);

			return new ModelAndView("fpGrowth_tableAsRules");

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			System.out.println(msg);
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
	@RequestMapping(value = "/schedule-fpGrowth", method = RequestMethod.POST)
	@ResponseBody
	public String requestSchedule(@ModelAttribute("appProperties") FpGrowthSettings settings, BindingResult result,
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
	 * To get matching table
	 * 
	 * @param descriptionFile
	 * @return Map of Key-Value
	 * @throws IOException
	 */
	static HashMap<String, String> getMatchingTable(IConfigurable config) throws IOException {

		String line = null;
		String[] arr = null;
		String separator = config.getSetting(Constants.CONF_FPG_DATA_SEPARATOR);
		String descriptionFile = config.getSetting(Constants.CONF_FPG_DATA_MATCHING_TABLE);
		BufferedReader br = null;

		if (descriptionFile == null || descriptionFile.length() == 0) {
			logger.warn("Matching table was not specified, matching task was arborted. Continue with raw data.");
			return null;
		}

		if (separator == null || separator.length() == 0) {
			logger.warn(
					"Seperator for matching table was not specified, matching task was arborted. Continue with raw data.");
			return null;
		}

		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(descriptionFile), "UTF8"));
		} catch (FileNotFoundException e) {
			// matching table was not specified
			logger.warn("Matching table was not specified, continue with raw data");
			e.printStackTrace();
			return null;
		}
		HashMap<String, String> matchingTable = new HashMap<>();

		while ((line = br.readLine()) != null) {
			// process the line
			arr = line.split(separator);
			if (arr.length >= 2) {
				matchingTable.put(arr[0], arr[1]);
			} else {
				logger.warn("In matching table, there was error of file format, skip this line, keep continuing ..");
			}
		}
		br.close();

		return matchingTable;
	}

	/**
	 * Make data for filling to Data tables
	 * 
	 * @param listResponse
	 * @return
	 * @throws IOException
	 */
	static String buildJsArrayDataFromResponse(List<Map<String, Object>> listResponse) throws IOException {

		StringBuilder dataBuilder = new StringBuilder();
		Gson gson = new Gson();

		HashMap<String, String> matchingTable = getMatchingTable(BaseController.webConfig);
		boolean isMatchable = false;
		if (matchingTable != null)
			if (matchingTable.size() > 0) {
				isMatchable = true;
			}

		dataBuilder.append("[");
		for (Iterator<Map<String, Object>> it = listResponse.iterator(); it.hasNext();) {
			dataBuilder.append("[");
			Map<String, Object> map = it.next();
			for (Object value : map.values()) {
				if (isMatchable) {
					try {
						Double.parseDouble(value.toString());
						dataBuilder.append(gson.toJson(value));
					} catch (NumberFormatException e) {
						@SuppressWarnings("unchecked")
						List<String> listProductID = (List<String>) (value);
						List<String> listProductName = new ArrayList<>();
						for (String productID : listProductID) {
							String productName = matchingTable.get(productID);
							listProductName.add(productName);
						}
						dataBuilder.append(gson.toJson(listProductName));
					}
				} else {
					dataBuilder.append(gson.toJson(value));
				}
				dataBuilder.append(",");
			}
			dataBuilder.deleteCharAt(dataBuilder.length() - 1);
			dataBuilder.append("],");
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");

		return dataBuilder.toString();
	}
}
