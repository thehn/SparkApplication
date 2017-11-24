package kr.gaion.ceh.restapi.scheduler;

import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.bean.settings.ScheduledJobInfo;
import kr.gaion.ceh.common.interfaces.IConfigurable;

/**
 * to manage Scheduler
 * 
 * @author hoang
 *
 */
public class SchedulerManagement {

	/**
	 * Scheduler
	 */
	private static Scheduler scheduler;

	/**
	 * logger
	 */
	private final static Logger logger = LoggerFactory.getLogger(SchedulerManagement.class);
	static {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			logger.error(e.getStackTrace().toString());
		}
	}

	/**
	 * get Scheduler instance
	 * 
	 * @return
	 */
	public static Scheduler getScheduler() {
		return scheduler;
	}

	/**
	 * to get scheduled job information by job name
	 * 
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	public static ScheduledJobInfo getScheduledJobInfo(String jobName) throws SchedulerException {

		ScheduledJobInfo schedInfo = null;
		JobKey targetJobKey = findJobKeyByName(jobName);

		// found
		if (targetJobKey != null) {
			// get JobDetail
			JobDetail jobDetail = scheduler.getJobDetail(targetJobKey);

			// get algorithm settings
			AlgorithmSettings algorithmSettings = (AlgorithmSettings) jobDetail.getJobDataMap().get("config");
			schedInfo = new ScheduledJobInfo(algorithmSettings);

			// Job's info
			schedInfo.setTitle(jobName);
			schedInfo.setDescription(jobDetail.getDescription());

			// get trigger detail (TODO - just take first trigger out)
			List<? extends Trigger> listTriggers = scheduler.getTriggersOfJob(targetJobKey);
			schedInfo.setStartTime(listTriggers.get(0).getStartTime());
			schedInfo.setPreviousFireTime(listTriggers.get(0).getPreviousFireTime());
			schedInfo.setNextFireTime(listTriggers.get(0).getNextFireTime());

			// trigger state
			TriggerState triggerState = scheduler.getTriggerState(listTriggers.get(0).getKey());
			schedInfo.setTriggerState(triggerState.toString());
		}
		// not found
		else {
			// it will return null
		}

		return schedInfo;
	}

	/**
	 * to get scheduled job information by job key
	 * 
	 * @param targetJobKey
	 * @return
	 * @throws SchedulerException
	 */
	public static ScheduledJobInfo getScheduledJobInfo(JobKey targetJobKey) throws SchedulerException {
		ScheduledJobInfo schedInfo = null;
		// Get JobDetail
		JobDetail jobDetail = scheduler.getJobDetail(targetJobKey);

		// Get algorithm settings
		AlgorithmSettings algorithmSettings = (AlgorithmSettings) jobDetail.getJobDataMap().get("config");
		schedInfo = new ScheduledJobInfo(algorithmSettings);

		// Job's info
		schedInfo.setTitle(targetJobKey.getName());
		schedInfo.setDescription(jobDetail.getDescription());

		// Get trigger detail (TODO - just take first trigger out)
		List<? extends Trigger> listTriggers = scheduler.getTriggersOfJob(targetJobKey);
		schedInfo.setStartTime(listTriggers.get(0).getStartTime());
		schedInfo.setPreviousFireTime(listTriggers.get(0).getPreviousFireTime());
		schedInfo.setNextFireTime(listTriggers.get(0).getNextFireTime());

		// Trigger state
		TriggerState triggerState = scheduler.getTriggerState(listTriggers.get(0).getKey());
		schedInfo.setTriggerState(triggerState.toString());

		return schedInfo;
	}

	/**
	 * to delete specified Job
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static void deleteJob(String jobName) throws SchedulerException {
		JobKey targetJobKey = findJobKeyByName(jobName);
		scheduler.deleteJob(targetJobKey);
	}

	/**
	 * to pause specified job
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static ScheduledJobInfo pauseJob(String jobName) throws SchedulerException {
		JobKey targetJobKey = findJobKeyByName(jobName);
		scheduler.pauseJob(targetJobKey);
		return getScheduledJobInfo(targetJobKey);
	}

	/**
	 * to resume specified paused job
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static ScheduledJobInfo resumeJob(String jobName) throws SchedulerException {
		JobKey targetJobKey = findJobKeyByName(jobName);
		scheduler.resumeJob(targetJobKey);
		return getScheduledJobInfo(targetJobKey);
	}

	/**
	 * to reschedule job by new trigger
	 * 
	 * @param jobName
	 * @param config
	 * @return
	 * @throws SchedulerException
	 */
	public static ScheduledJobInfo rescheduleJob(IConfigurable config) throws SchedulerException {
		String jobName = config.getSetting(AlgorithmSettings.JOB_TITLE);
		TriggerUtil triggerUtil = new TriggerUtil(config);
		JobKey targetJobKey = findJobKeyByName(jobName);
		List<? extends Trigger> listTrigger = scheduler.getTriggersOfJob(targetJobKey);
		// TODO - get first element of list triggers
		TriggerKey triggerKey = listTrigger.get(0).getKey();
		scheduler.rescheduleJob(triggerKey, triggerUtil.getTrigger());
		
		// update scheduler detail
		AlgorithmSettings algo = (AlgorithmSettings)(scheduler.getJobDetail(targetJobKey).getJobDataMap().get("config"));
		algo.setInterval(config.getSetting(AlgorithmSettings.SCHEDULE_INTERVAL));
		algo.setHour(config.getSetting(AlgorithmSettings.SCHEDULE_HOUR));
		algo.setMin(config.getSetting(AlgorithmSettings.SCHEDULE_MIN));
		algo.setWday(config.getSetting(AlgorithmSettings.SCHEDULE_WDAY));
		algo.setMday(config.getSetting(AlgorithmSettings.SCHEDULE_MDAY));
		algo.setCronExpression(config.getSetting(AlgorithmSettings.SCHEDULE_CRON_EXP));

		return getScheduledJobInfo(targetJobKey);
	}

	/**
	 * to find a job by name
	 * 
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	private static JobKey findJobKeyByName(String jobName) throws SchedulerException {
		Set<JobKey> listJobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals("algorithm_group"));
		for (JobKey jobKey : listJobKeys) {
			if (jobKey.getName().equals(jobName)) {
				return jobKey;
			}
		}

		// not found
		return null;
	}

}
