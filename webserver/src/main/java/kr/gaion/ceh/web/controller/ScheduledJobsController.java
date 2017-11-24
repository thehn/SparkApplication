package kr.gaion.ceh.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.ScheduleSettings;
import kr.gaion.ceh.common.bean.settings.ScheduledJobInfo;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.config.AppSettingUtil;

@Controller
public class ScheduledJobsController {

	private static Gson gson = new Gson();
	final static Logger logger = LoggerFactory.getLogger(ScheduledJobsController.class);

	/**
	 * to display all scheduled jobs
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/scheduledJobs")
	public String showScheduledJobs(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, BaseController.webConfig);
		RestTemplate restClient = new RestTemplate();
		String response = null;
		String url = null;
		List<List<String>> listAllInfo = new ArrayList<List<String>>();
		List<String> subList = null;

		// get all scheduled jobs
		url = Utilities.getUri(BaseController.webConfig, Constants.API_GET_SCHEDULED_JOBS);
		response = restClient.getForObject(url, String.class);
		List<String> listJobTitle = gson.fromJson(response, List.class);

		url = Utilities.getUri(BaseController.webConfig, Constants.API_SCHEDULES);
		for (String jobTitle : listJobTitle) {
			HashMap<String, String> customRequest = new HashMap<>();
			customRequest.put("jobTitle", jobTitle);
			customRequest.put("action", Constants.SCHEDULE_ACTION_GET_INFO);
			String json = gson.toJson(customRequest);
			response = restClient.postForObject(url, json, String.class);
			ScheduledJobInfo scheduledJobInfo = gson.fromJson(response, ScheduledJobInfo.class);
			subList = buildDataRowObject(scheduledJobInfo);
			listAllInfo.add(subList);
		}

		model.addAttribute("listAllInfo", gson.toJson(listAllInfo));

		return "page_schedule";
	}

	/**
	 * to request reschedule
	 * 
	 * @param scheduleSettings
	 * @return
	 */
	@RequestMapping(value = "/rescheduledJobs", method = RequestMethod.POST)
	@ResponseBody
	public List<String> rescheduledJobs(@ModelAttribute("scheduleSettings") ScheduleSettings scheduleSettings, BindingResult result) {
		RestTemplate restClient = new RestTemplate();
		String url = null;
		String json = null;
		String response = null;
		ScheduledJobInfo scheduledJobInfo = null;

		// send request to get current trigger state
		json = gson.toJson(scheduleSettings);
		url = Utilities.getUri(BaseController.webConfig, Constants.API_RESCHEDULES);
		response = restClient.postForObject(url, json, String.class);
		scheduledJobInfo = gson.fromJson(response, ScheduledJobInfo.class);
		List<String> data = buildDataRowObject(scheduledJobInfo);

		return data;
	}

	/**
	 * to change state of scheduled job
	 * 
	 * @param jobTitle
	 * @return
	 */
	@RequestMapping(value = "/changeScheduleState", method = RequestMethod.POST)
	@ResponseBody
	public String changeScheduleState(@RequestParam("jobTitle") String jobTitle) {
		RestTemplate restClient = new RestTemplate();
		HashMap<String, String> customRequest = new HashMap<>();
		String url = null;
		String json = null;
		String response = null;
		String status = null;
		ScheduledJobInfo scheduledJobInfo = null;

		// send request to get current trigger state
		customRequest.put("jobTitle", jobTitle);
		customRequest.put("action", Constants.SCHEDULE_ACTION_GET_INFO);
		json = gson.toJson(customRequest);
		url = Utilities.getUri(BaseController.webConfig, Constants.API_SCHEDULES);
		response = restClient.postForObject(url, json, String.class);
		scheduledJobInfo = gson.fromJson(response, ScheduledJobInfo.class);
		status = scheduledJobInfo.getTriggerState();

		if (status.equals("NORMAL")) {
			customRequest.put("action", Constants.SCHEDULE_ACTION_PAUSE_JOB);
		} else if (status.equals("PAUSED")) {
			customRequest.put("action", Constants.SCHEDULE_ACTION_RESUME_JOB);
		} else {
			// TODO - currently do not support
		}

		// rend request to update trigger state
		json = gson.toJson(customRequest);
		response = restClient.postForObject(url, json, String.class);
		scheduledJobInfo = gson.fromJson(response, ScheduledJobInfo.class);
		// List<String> data = buildDataRowObject(scheduledJobInfo);

		return scheduledJobInfo.getTriggerState();
	}

	/**
	 * to delete specified scheduled job
	 * 
	 * @param jobTitle
	 * @return
	 */
	@RequestMapping(value = "/deleteSchedule", method = RequestMethod.POST)
	@ResponseBody
	public String deleteSchedule(@RequestParam("jobTitle") String jobTitle) {
		RestTemplate restClient = new RestTemplate();
		HashMap<String, String> customRequest = new HashMap<>();
		customRequest.put("jobTitle", jobTitle);
		customRequest.put("action", Constants.SCHEDULE_ACTION_DELETE_JOB);
		String url = Utilities.getUri(BaseController.webConfig, Constants.API_SCHEDULES);
		String json = gson.toJson(customRequest);
		String response = restClient.postForObject(url, json, String.class);

		return response;
	}

	/**
	 * to build data for each row of table, respectively each scheduled job
	 * 
	 * @param scheduledJobInfo
	 * @return
	 */
	private List<String> buildDataRowObject(ScheduledJobInfo scheduledJobInfo) {
		List<String> subList = null;
		subList = new ArrayList<>();
		subList.add(scheduledJobInfo.getTitle());
		subList.add(scheduledJobInfo.getAlgorithmSettings().getAlgorithm());
		subList.add(scheduledJobInfo.getAlgorithmSettings().getAction());
		subList.add(scheduledJobInfo.getDescription() != null ? scheduledJobInfo.getDescription() : "");
		subList.add(scheduledJobInfo.explainSchedule());
		subList.add(scheduledJobInfo.getStartTime() != null ? scheduledJobInfo.getStartTime().toString() : "");
		subList.add(scheduledJobInfo.getPreviousFireTime() != null ? scheduledJobInfo.getPreviousFireTime().toString()
				: "");
		subList.add(scheduledJobInfo.getNextFireTime() != null ? scheduledJobInfo.getNextFireTime().toString() : "");
		subList.add(scheduledJobInfo.getTriggerState());

		return subList;
	}
}
