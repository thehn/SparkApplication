package kr.gaion.ceh.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.FSChiSqSelectorResponse;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.FSChiSqSelectorSettings;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.config.AppSettingUtil;
import kr.gaion.ceh.web.response_processing.DatatableDataProcessing;

@Controller
public class FeaturesSelectionController {

	static String fileNameWithoutExtension = "filtered_features";
	final static Logger logger = LoggerFactory.getLogger(FeaturesSelectionController.class);
	private static Gson gson = new Gson();

	/**
	 * to show Features Selection main pages
	 * 
	 * @param model
	 * @param algorithmName
	 * @return
	 */
	@RequestMapping(value = "/featuresSel-{algorithmName}")
	public String deployFeaturesSel(ModelMap model, @PathVariable(name = "algorithmName") String algorithmName) {

		model.addAttribute("algorithmName", algorithmName);
		AppSettingUtil.loadCurrentConfigToModel(model, BaseController.webConfig);
		return Constants.FEATURES_SELECTION_CHISQ_HOMEPAGE;
	}

	/**
	 * to send request train to Rest-API server
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/request-FSChisqTrain", method = RequestMethod.POST)
	public ModelAndView requestFSChisqTrain(@ModelAttribute("appProperties") FSChiSqSelectorSettings settings,
			BindingResult result, ModelMap model) {
		String jsonResponse = "";
		try {

			// TODO - only support first element from list Lookup definition
			LookupDataController.applyLookupForSettings(settings.getListLookups(), settings);

			// TODO - hard setting
			settings.setDataInputOption(FSChiSqSelectorSettings.INPUT_FROM_FILE);

			// Send request to Spark server
			RestTemplate restClient = new RestTemplate();
			String json = settings.toJson();
			String uri = Utilities.getUri(BaseController.webConfig);
			jsonResponse = restClient.postForObject(uri, json, String.class);

			// Get response - JSON object
			FSChiSqSelectorResponse objResponse = gson.fromJson(jsonResponse, FSChiSqSelectorResponse.class);

			if (objResponse.getType() == ResponseType.MESSAGE) {
				model.addAttribute("msg", gson.toJson(jsonResponse));
				return new ModelAndView("subPage-responseViewer");
			} else {
				// continue ...
			}

			String filteredFeatures = objResponse.toString();
			// save to file for downloading
			objResponse.saveFilteredFeaturesInfoToCSV(objResponse.getClassCol(), fileNameWithoutExtension);
			model.addAttribute("filteredFeatures", filteredFeatures);
			model.addAttribute("filteredFeaturesFileName", fileNameWithoutExtension);

			// list of columns to fill into data table
			String columnsList = DatatableDataProcessing.makeColumnsList(objResponse.getClassCol(),
					objResponse.getSelectedFields());
			model.addAttribute("columnsList", columnsList);

			// reload setting and page
			return new ModelAndView("featuresSelChiSq_trainingResults");

		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			logger.error(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}
	}

}
