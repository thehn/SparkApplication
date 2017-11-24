package kr.gaion.ceh.restapi.spark;

import java.util.Properties;
import java.util.Map.Entry;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.config.FileConfigLoader;
import kr.gaion.ceh.common.util.Utilities;

public class SparkContextManager {

	/**
	 * logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(SparkContextManager.class);
	
	/**
	 * private properties
	 */
	private static SparkSession ss = null;
	private static SparkConf sConf = null;
	private static SparkContext sContext = null;
	private static JavaSparkContext jvContext = null;
	private static FileConfigLoader sparkSettings = null;

	/**
	 * current INSTANCE of class
	 */
	private static SparkContextManager INSTANCE = null;

	/**
	 * Constructor
	 * 
	 * @throws Exception
	 */
	private SparkContextManager() throws Exception {
		
		logger.info("Initialize new instance of SparkContext");

		String sparkConfigFile = Utilities.getPathInWorkingFolder(Constants.CONF_DIR, Constants.SPARK_CONFIG_FILE);
		sparkSettings = new FileConfigLoader(sparkConfigFile);
		sConf = new SparkConf();

		// All configuration:
		loadAllSettingsFromConfFile(sparkSettings, sConf);

		String hadoopHomeDir = sparkSettings.getSetting("hadoop.home.dir");
		if (hadoopHomeDir != null && hadoopHomeDir.length() > 0) {
			System.setProperty("hadoop.home.dir", sparkSettings.getSetting("hadoop.home.dir"));
		}

		// initialize SparkContext
		jvContext = new JavaSparkContext(sConf);
		sContext = jvContext.sc();
	}

	/**
	 * to get current INSTANCE of SparkContext
	 * 
	 * @return
	 * @throws Exception
	 */
	public static SparkContextManager getInstance() throws Exception {
		if (INSTANCE == null || sContext.isStopped()) {
			INSTANCE = new SparkContextManager();
		} else {
			// continue
		}
		return INSTANCE;
	}

	/**
	 * to get JavaSparkContext
	 * 
	 * @return
	 */
	public JavaSparkContext getJavaSparkContext() {
		return jvContext;
	}

	/**
	 * to stop current SparkContext
	 */
	public void stop() {
		logger.info("Call stop SparkContext");
		jvContext.stop();
		INSTANCE = null;
	}

	/**
	 * to load all settings from configuration file<br>
	 * configuration file was located in _app_home_dir/conf/app.conf
	 * 
	 * @param sparkSettings
	 * @param sparkConf
	 */
	private void loadAllSettingsFromConfFile(FileConfigLoader sparkSettings, SparkConf sparkConf) {

		Properties prop = sparkSettings.getProperties();
		for (Entry<Object, Object> entry : prop.entrySet()) {
			sparkConf.set(entry.getKey().toString(), entry.getValue().toString());
		}
	}
}
