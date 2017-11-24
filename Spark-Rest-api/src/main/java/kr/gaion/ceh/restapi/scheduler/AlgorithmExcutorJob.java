package kr.gaion.ceh.restapi.scheduler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;

public class AlgorithmExcutorJob implements org.quartz.Job {

	private final static Logger logger = LoggerFactory.getLogger(AlgorithmExcutorJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		IConfigurable config = (IConfigurable) context.getJobDetail().getJobDataMap().get("config");
		System.out.println("Job is scheduled to run with data: " + new Gson().toJson(config));

		String action = config.getSetting(AlgorithmSettings.ACTION);
		logger.debug("action: " + action);
		String algorithm = config.getSetting(AlgorithmSettings.ALGORITHM);
		logger.debug("algorithm: " + algorithm);

		// Using reflection to get constructor of class dynamically
		Class<?> cl;
		try {
			cl = Class.forName("kr.gaion.ceh.restapi.handler.Api" + algorithm);
			Constructor<?> constructor = cl.getConstructor();
			Object instance = constructor.newInstance();
			logger.info(
					"Successfully to schedule algorithm: " + "kr.gaion.ceh.restapi.handler.Api" + algorithm);

			// Using reflection to get method name of class dynamically
			Method method = null;
			method = cl.getMethod(action, new Class[] { IConfigurable.class });

			// start Spark Context
			SparkEsConnector.getJavaSparkContext();
			IResponseObject response = (IResponseObject) method.invoke(instance, config);
			logger.info("Algorithm executed successfully, response: " + new Gson().toJson(response));

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String sStackTrace = sw.toString(); // stack trace as a string
			logger.error("Algorithm executed failed, response: " + sStackTrace);
		}

	}

}
