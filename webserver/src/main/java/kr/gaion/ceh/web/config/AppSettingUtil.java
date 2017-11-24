package kr.gaion.ceh.web.config;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.ui.ModelMap;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.config.FileConfigLoader;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.web.model.WebSettings;

/***
 * Application setting utilities
 * 
 * @author hoang
 *
 */
public class AppSettingUtil {

	/***
	 * To load all current configuration from file to web-model
	 * 
	 * @param model
	 * @param config
	 */
	public static void loadCurrentConfigToModel(ModelMap model, IConfigurable config) {
		model.addAttribute("sparkHost", config.getSetting(Constants.CONF_SPARK_HOST));
		model.addAttribute("sparkPort", config.getSetting(Constants.CONF_SPARK_PORT));
		model.addAttribute("esHost", config.getSetting(Constants.CONF_ES_HOST));
		model.addAttribute("esClusterName", config.getSetting(Constants.CONF_ES_CLUSTER_NAME));
		model.addAttribute("esTransportPort", config.getSetting(Constants.CONF_ES_TRANSPORT_PORT));
		model.addAttribute("dataSeparator", config.getSetting(Constants.CONF_FPG_DATA_SEPARATOR));
	}

	/***
	 * To update all setting values that user inputed from web
	 * 
	 * @param properties
	 * @param fileConfig
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void updateSettingsFromWeb(IConfigurable webConfig, FileConfigLoader fileConfig)
			throws FileNotFoundException, IOException {
		fileConfig.updateProperty(Constants.CONF_SPARK_HOST, webConfig.getSetting(WebSettings.SPARK_HOST));
		fileConfig.updateProperty(Constants.CONF_SPARK_PORT, webConfig.getSetting(WebSettings.SPARK_PORT).toString());
		fileConfig.updateProperty(Constants.CONF_ES_HOST, webConfig.getSetting(WebSettings.ES_HOST));
		fileConfig.updateProperty(Constants.CONF_ES_TRANSPORT_PORT,
				webConfig.getSetting(WebSettings.ES_TRANSPORT_PORT).toString());
		fileConfig.updateProperty(Constants.CONF_FPG_DATA_SEPARATOR,
				webConfig.getSetting(WebSettings.DATA_SEPARATOR).toString());
		fileConfig.saveProperties();
	}
}
