package kr.gaion.ceh.restapi.handler;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.response.ResponseBase;
import kr.gaion.ceh.common.bean.response.ResponseType;
import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.EsIndexInfo;
import kr.gaion.ceh.common.bean.settings.ModelInfo;
import kr.gaion.ceh.common.bean.settings.ScheduledJobInfo;
import kr.gaion.ceh.common.interfaces.IResponseObject;
import kr.gaion.ceh.restapi.MainEntry;
import kr.gaion.ceh.restapi.scheduler.SchedulerManagement;
import kr.gaion.ceh.restapi.spark.SparkEsConnector;
import kr.gaion.ceh.restapi.util.ModelUtil;
import kr.gaion.ceh.restapi.util.FileUploadHelper;

/**
 * This is main class To handle specific request from Web server
 */
public class MainRequestHandler {

	// private static List<String> listUploadedFiles = new ArrayList<>();

	/**
	 * This is flag, to mark that the API already run or not To prevent the parallel
	 * running, cause exception
	 */
	private final static Logger logger = LoggerFactory.getLogger(MainRequestHandler.class);

	private static Gson gson;
	static {
		gson = new Gson();
	}

	/**
	 * Main function - to mapping the requests from client
	 * 
	 * @param args
	 *            port number
	 */
	public void run(int port) {

		port(port);

		/*
		 * To test connection
		 */
		get("/test-connection", "application/json", (request, response) -> {
			ResponseBase msg = new ResponseBase(ResponseType.MESSAGE);
			msg.setMessage("Connection was established, enjoy!");

			return msg;
		}, new JsonTransformer());

		/*
		 * to get list of scheduled jobs
		 */
		get("/list-schedules", "application/json", (request, response) -> {

			try {
				Scheduler scheduler = SchedulerManagement.getScheduler();
				Set<JobKey> listJobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals("algorithm_group"));
				List<String> listJobTitle = new ArrayList<>();
				for (JobKey jk : listJobKeys) {
					listJobTitle.add(jk.getName());
				}
				return listJobTitle;

			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());

		/*
		 * to schedule job
		 */
		post("/schedule", "application/json", (request, response) -> {

			try {
				JsonObject json = gson.fromJson(request.body(), JsonObject.class);
				String jobTitle = json.get("jobTitle").getAsString();
				String action = json.get("action").getAsString();
				logger.info("Job title: " + jobTitle);
				logger.info("Action: " + action);

				switch (action) {
				case Constants.SCHEDULE_ACTION_GET_INFO: {
					ScheduledJobInfo jobInfo = SchedulerManagement.getScheduledJobInfo(jobTitle);
					return jobInfo;
				}
				case Constants.SCHEDULE_ACTION_DELETE_JOB: {
					SchedulerManagement.deleteJob(jobTitle);
					return true;
				}
				case Constants.SCHEDULE_ACTION_PAUSE_JOB: {
					return SchedulerManagement.pauseJob(jobTitle);
				}
				case Constants.SCHEDULE_ACTION_RESUME_JOB: {
					return SchedulerManagement.resumeJob(jobTitle);
				}
				default: {
					return String.format("Action %s is not supported", action);
				}
				}

			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());
		
		/*
		 * to reschedule the job
		 */
		post("/reschedule", "application/json", (request, response) -> {
			AlgorithmSettings config = gson.fromJson(request.body(), AlgorithmSettings.class);
			try {
				return SchedulerManagement.rescheduleJob(config);
			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());
		
		
		/*
		 * to get saved models
		 */
		post("/get-models", "application/json", (request, response) -> {

			try {
				JsonObject json = gson.fromJson(request.body(), JsonObject.class);
				String algorithmName = json.get("algorithm").getAsString();
				logger.info("Algorithm name: " + algorithmName);

				ModelInfo modelInfo = new ModelInfo(algorithmName);
				String[] listModels = modelInfo.getSavedModels();

				return listModels;
			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());

		/*
		 * To delete existed model
		 */
		post("/delete-model", "application/json", (request, response) -> {

			try {
				JsonObject json = gson.fromJson(request.body(), JsonObject.class);
				String algorithmName = json.get("algorithm").getAsString();
				String modelName = json.get("modelName").getAsString();
				logger.info(String.format("Request deleting mode for algorithm name: %s, model name: %s", algorithmName,
						modelName));

				ModelUtil modelInfo = new ModelUtil(algorithmName, MainEntry.restAppConfig);
				modelInfo.deleteModel(modelName);

				return true;
			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());

		/*
		 * to get model info
		 */
		post("/model-info", "application/json", (request, response) -> {

			try {
				JsonObject json = gson.fromJson(request.body(), JsonObject.class);
				String algorithmName = json.get("algorithm").getAsString();
				String modelName = json.get("modelName").getAsString();
				logger.info("Algorithm name: " + algorithmName);
				ModelInfo modelInfo = new ModelInfo(algorithmName);
				modelInfo = modelInfo.loadFromJson(modelName);
				return modelInfo.loadFromJson(modelName);
			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}

		}, new JsonTransformer());

		/*
		 * Receive single file from Web server
		 */
		post("/file-upload", "application/json", (request, response) -> {
			try {
				// Convert from JSON to object
				logger.info("get data by bytes");
				byte[] bytes = request.bodyAsBytes();

				/*// Creating the directory to store file
				logger.info("Creating the directory to store file");
				File sparkFile = Utilities.makeTempleFile();
				String fileFullPath = sparkFile.getAbsolutePath();

				logger.info("Wrting file: " + fileFullPath);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(sparkFile));
				stream.write(bytes);
				stream.close();
				// listUploadedFiles.add(sparkFile.getPath());
				return fileFullPath;*/
				return FileUploadHelper.receiveDataFromClient(bytes);
			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			}
		}, new JsonTransformer());

		/*
		 * To index data from file to ElasticSearch
		 */
		post("/index-data", "application/json", (request, response) -> {

			try {
				logger.info("Index to es-search: " + request.body());
				EsIndexInfo indexInfo = gson.fromJson(request.body(), EsIndexInfo.class);
				List<String> listUploadedFiles = Arrays.asList(indexInfo.getListUploadedFiles().split(","));

				if (listUploadedFiles.size() == 0) {
					return "There was not uploaded data, please upload data first.";
				}

				logger.info("Object value: " + indexInfo.toJson());
				logger.info("Index request is handling ..");
				SparkEsConnector.getJavaSparkContext();
				ApiIndexToEs.indexCsvFileToEs(indexInfo, listUploadedFiles);

				return "Index successfully";

			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			} finally {
				// SparkEsConnector.stopJavaSparkContext();
			}
		}, new JsonTransformer());

		/*
		 * Handle all requests for running ML algorithm
		 */
		post("/ml-execute", "application/json", (request, response) -> {
			try {
				logger.info("Request: " + request.body());
				IResponseObject sparkResponse = null;
				ApiAlgorithmHandler algorithmHandler = null;

				logger.info("Algorithm is handling ..");
				algorithmHandler = new ApiAlgorithmHandler(request.body());
				algorithmHandler.executeJob();
				sparkResponse = algorithmHandler.getResponse();
				logger.info("Algorithm handled.");

				return sparkResponse;

			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			} finally {
				// SparkEsConnector.stopJavaSparkContext();
			}
		}, new JsonTransformer());

		/*
		 * TODO handle search request
		 */
		post("/es-search", "application/json", (request, response) -> {
			try {
				// get query from request
				JsonObject json = gson.fromJson(request.body(), JsonObject.class);
				String query = json.get("query").getAsString();
				logger.info("Request for es-search: " + query);
				List<Map<String, Object>> returnVal = null;

				logger.info("Search request is handling ..");
				returnVal = ApiGetDataFromEs.search(query);

				return returnVal;

			} catch (Exception e) {
				return e.getMessage() + ". Cause: " + e.getCause();
			} finally {
				// SparkEsConnector.stopJavaSparkContext();
			}
		}, new JsonTransformer());

	}
}
