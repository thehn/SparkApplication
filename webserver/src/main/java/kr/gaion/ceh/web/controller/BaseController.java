package kr.gaion.ceh.web.controller;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.config.FileConfigLoader;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.config.AppSettingUtil;
import kr.gaion.ceh.web.model.EsWebConnector;
import kr.gaion.ceh.web.model.WebSettings;

/**
 * This controller handles index page
 * 
 * @author hoang
 *
 */
@Controller
public class BaseController {
	/**
	 * Global variable<br>
	 * This property has package access scope Using to load, change the setting for
	 * Spark application
	 */
	static FileConfigLoader webConfig = null;
	static Gson gson = new Gson();

	/**
	 * Constructor
	 * 
	 * @throws IOException
	 */
	public BaseController() throws IOException {
		// load current spark-server settings
		String configFilePath = Utilities.getPathInWorkingFolder(Constants.CONF_DIR, Constants.APP_CONFIG_FILE);
		webConfig = new FileConfigLoader(configFilePath);
		// initialize ElasticSearch connection
		EsWebConnector.getInstance(webConfig);
	}

	/**
	 * home page
	 * 
	 * @param model
	 * @return
	 * @throws IOException
	 */
//	@RequestMapping(value = "/")
//	public ModelAndView welcome(ModelMap model) throws IOException {
//		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
//		/* return new ModelAndView(Constants.HOME_PAGE); */
//		return new ModelAndView("dashboard_demo");
//	}

	/**
	 * Frequent Patterns Mining
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fpGrowth-homepage")
	public ModelAndView fpGrowthHome(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.FPGROWTH_HOMEPAGE);
	}

	/**
	 * System monitoring
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/system-monitoring")
	public String showSystemMonitoring(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return "systemMonitoring";
	}

	/**
	 * Support Vector Machine
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-svm-homepage")
	public ModelAndView svmClassification(ModelMap model) {

		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.SVM_CLASSIFICATION_HOMEPAGE);
	}
	
	/**
	 * Support Vector Classifier
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-svc-homepage")
	public ModelAndView svcClassification(ModelMap model) {

		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.SVC_CLASSIFICATION_HOMEPAGE);
	}

	/**
	 * Random Forest Classifier
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-rfc-homepage")
	public ModelAndView rfcClassification(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.RFC_CLASSIFICATION_HOMEPAGE);
	}

	/**
	 * Multiple Layers Perceptron Classifier
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-mlp-homepage")
	public ModelAndView mlpClassification(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.MLP_CLASSIFICATION_HOMEPAGE);
	}

	/**
	 * Naive Bayes Classifier
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-nb-homepage")
	public ModelAndView nbClassification(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.NB_CLASSIFICATION_HOMEPAGE);
	}
	
	/**
	 * Logistic Regression Classifier
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/classification-lr-homepage")
	public ModelAndView lgClassification(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		return new ModelAndView(Constants.LR_CLASSIFICATION_HOMEPAGE);
	}

	/**
	 * KMeans clustering
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/clustering-{clusterAlgorithm}")
	public ModelAndView test(ModelMap model, @PathVariable("clusterAlgorithm") String clusterAlgorithm) {
		AppSettingUtil.loadCurrentConfigToModel(model, webConfig);
		model.addAttribute("clusterAlgorithm", clusterAlgorithm);
		return new ModelAndView(Constants.CLUSTERING_HOMEPAGE);
	}

	/**
	 * Update configuration
	 * 
	 * @param settings
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateConfig", method = RequestMethod.POST)
	public ModelAndView updateConfig(@ModelAttribute("sparkSetting") WebSettings webSettings, BindingResult result,
			ModelMap model) {
		try {
			AppSettingUtil.updateSettingsFromWeb(webSettings, BaseController.webConfig);
			// reload setting and page
			model.addAttribute("msg", "Updated");
			return new ModelAndView("subPage-responseViewer");
		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! Info: " + e.getMessage();
			model.addAttribute("msg", new Gson().toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}
	}

	/**
	 * TODO search data from ElasticSearch
	 * 
	 * @param query
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/searchData", method = RequestMethod.POST)
	public ModelAndView searchData(@RequestParam("query") String query, ModelMap model) {

		String jsonResponse = "";
		try {
			// TODO Send query to Spark server, for search
			RestTemplate restClient = new RestTemplate();
			String uri = Utilities.getUri(BaseController.webConfig, Constants.API_SEARCH);
			jsonResponse = restClient.postForObject(uri, query, String.class);

			// TODO Add response to model
			model.addAttribute("json", jsonResponse);

			return new ModelAndView("tableViewer");
		} catch (Exception e) {
			String msg = "Server internal error occurred, please check the inputs again! " + jsonResponse;
			System.out.println(msg);
			model.addAttribute("msg", gson.toJson(msg));
			return new ModelAndView("subPage-responseViewer");
		}
	}

}
