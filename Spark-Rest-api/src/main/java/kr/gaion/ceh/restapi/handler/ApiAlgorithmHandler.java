package kr.gaion.ceh.restapi.handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.scheduler.AlgorithmExcutorJob;
import kr.gaion.ceh.restapi.scheduler.SchedulerManagement;
import kr.gaion.ceh.restapi.scheduler.TriggerUtil;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

public class ApiAlgorithmHandler {
	protected IResponseObject response;
	protected String algorithm;
	protected boolean isSchedule;
	protected IConfigurable config;
	private final static Logger logger = LoggerFactory.getLogger(ApiAlgorithmHandler.class);

	/**
	 * Constructor
	 * 
	 * @param config
	 * @throws ClassNotFoundException
	 * @throws JsonSyntaxException
	 */

	public ApiAlgorithmHandler(String jsonRequest) throws JsonSyntaxException, ClassNotFoundException {
		logger.info("Initializing ApiAlgorithmHandler(String jsonRequest) ..");
		IConfigurable setting = new Gson().fromJson(jsonRequest, AlgorithmSettings.class);
		algorithm = setting.getSetting(AlgorithmSettings.ALGORITHM);
		isSchedule = setting.getSetting(AlgorithmSettings.SCHEDULE_FLG);
		config = (IConfigurable) new Gson().fromJson(jsonRequest,
				Class.forName("kr.gaion.ceh.common.bean.settings." + algorithm + "Settings"));
		logger.info("Initialized ApiAlgorithmHandler(String jsonRequest)");
	}

	/**
	 * to execute job based on requests from client
	 * 
	 * @param jsonRequest
	 * @throws Exception
	 */
	public void executeJob() throws Exception {
		if (isSchedule) {
			// execute job with specified schedule
			executeScheduler();
		} else {
			// execute job immediately
			executeJobImmediately();
		}
	}

	/**
	 * return message
	 * 
	 * @return
	 */
	public IResponseObject getResponse() {
		return this.response;
	}

	/**
	 * execute api-algorithm immediately
	 * 
	 * @throws Exception
	 */
	public void executeJobImmediately() throws Exception {

		logger.info("AlgorithmHandler executes");
		String action = config.getSetting(AlgorithmSettings.ACTION);
		logger.debug("action: " + action);
		String algorithm = config.getSetting(AlgorithmSettings.ALGORITHM);
		logger.debug("algorithm: " + algorithm);

		// Using reflection to get constructor of class dynamically
		Class<?> cl = Class.forName("kr.gaion.ceh.restapi.handler.Api" + algorithm);
		Constructor<?> constructor = cl.getConstructor();
		Object instance = constructor.newInstance();
		logger.info("Successfully initialized algorithm executor: " + "kr.gaion.ceh.restapi.handler.Api" + algorithm);

		// Using reflection to get method name of class dynamically
		Method method = null;
		method = cl.getMethod(action, new Class[] { IConfigurable.class });

		// start Spark Context
		SparkEsConnector.getJavaSparkContext();
		response = (IResponseObject) method.invoke(instance, config);

		// Stop spark context
		// SparkEsConnector.stopJavaSparkContext();

		logger.info("Algorithm executed successfully");
	};

	/**
	 * execute job by start scheduler
	 * 
	 * @throws Exception
	 */
	public void executeScheduler() throws Exception {
		logger.info("Schedule running");
		JobDataMap data = new JobDataMap();
		String jobTitile = config.getSetting(AlgorithmSettings.JOB_TITLE);
		String jobDescription = config.getSetting(AlgorithmSettings.SCHEDULE_DESC);
		data.put("config", config);

		JobDetail job = JobBuilder.newJob(AlgorithmExcutorJob.class)
				.setJobData(data)
				.withIdentity(jobTitile, "algorithm_group")
				.withDescription(jobDescription).build();

		TriggerUtil triggerMng = new TriggerUtil(config);

		Scheduler scheduler = SchedulerManagement.getScheduler();
		scheduler.start();
		scheduler.scheduleJob(job, triggerMng.getTrigger());

		ResponseBase response = new ResponseBase(ResponseType.MESSAGE);
		response.setMessage("Scheduled job successfully");
		this.response = response;
	}

}
