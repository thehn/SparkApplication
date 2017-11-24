package kr.gaion.ceh.restapi;

import java.io.IOException;

// import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.config.FileConfigLoader;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.restapi.handler.MainRequestHandler;

/**
 * Main entry of program
 * 
 * @author hoang
 *
 */
public class MainEntry {

	/**
	 * global variables
	 */
	public static FileConfigLoader restAppConfig = null;

	private final static Logger logger = LoggerFactory.getLogger(MainEntry.class);

	public static void main(String[] args) {

		// configure log output
		// BasicConfigurator.configure();
		
		// initialize global variable
		try {
			String restAppConfigFile = Utilities.getPathInWorkingFolder(Constants.CONF_DIR,
					Constants.REST_APP_CONFIG_FILE);
			restAppConfig = new FileConfigLoader(restAppConfigFile);
		} catch (IOException e) {
			logger.error("Configuration file was not found: " + e.getMessage());
			return;
		}

		// handle requests
		MainRequestHandler handler = new MainRequestHandler();
		String strPort = restAppConfig.getSetting(Constants.CONF_REST_PORT);
		int iPort;
		try {
			iPort = Integer.parseInt(strPort);
		} catch (Exception e) {
			iPort = Constants.SPA_DEFAULT_PORT;
		}

		// run main handler thread
		handler.run(iPort);
	}

}
