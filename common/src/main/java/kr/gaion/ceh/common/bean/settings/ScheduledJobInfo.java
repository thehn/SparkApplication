package kr.gaion.ceh.common.bean.settings;

import java.util.Date;

import kr.gaion.ceh.common.config.ConfigBase;

/**
 * Contain all information about scheduled job
 * 
 * @author hoang
 *
 */
public class ScheduledJobInfo extends ConfigBase {

	public static final String INTERVAL_HOURLY = "hourly";
	public static final String INTERVAL_DAILY = "daily";
	public static final String INTERVAL_WEEKLY = "weekly";
	public static final String INTERVAL_MONTHLY = "monthly";
	public static final String INTERVAL_CRON = "cron";

	protected String title;
	protected String description;
	protected AlgorithmSettings algorithmSettings;
	protected Date nextFireTime;
	protected Date previousFireTime;
	protected Date startTime;
	protected String triggerState;

	/**
	 * Constructor
	 * 
	 * @param algorithmSettings
	 */
	public ScheduledJobInfo(AlgorithmSettings algorithmSettings) {
		this.algorithmSettings = algorithmSettings;
	}

	/**
	 * to get description about schedule
	 * 
	 * @return
	 */
	public String explainSchedule() {
		StringBuilder expression = new StringBuilder();

		// interval
		String interval = algorithmSettings.getInterval();
		switch (interval) {
		case INTERVAL_HOURLY: {
			expression.append(String.format("Every hour at %d minutes past the hour.", algorithmSettings.getMin()));
			break;
		}
		case INTERVAL_DAILY: {
			expression.append(String.format("Every day at %d:00", algorithmSettings.getHour()));
			break;
		}
		case INTERVAL_WEEKLY: {
			expression.append(
					String.format("Weekly on %s at %d:00", algorithmSettings.getWday(), algorithmSettings.getHour()));
			break;
		}
		case INTERVAL_MONTHLY: {
			expression.append(
					String.format("Monthly on %d at %d:00", algorithmSettings.getMday(), algorithmSettings.getHour()));
			break;
		}
		case INTERVAL_CRON: {
			expression.append(String.format("By cron expression: \"%s\"", algorithmSettings.getCronExpression()));
			break;
		}
		default: {
			break;
		}
		}

		return expression.toString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AlgorithmSettings getAlgorithmSettings() {
		return algorithmSettings;
	}

	public void setAlgorithmSettings(AlgorithmSettings algorithmSettings) {
		this.algorithmSettings = algorithmSettings;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public Date getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

}
