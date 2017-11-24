package kr.gaion.ceh.common.bean.settings;

import kr.gaion.ceh.common.config.ConfigBase;

public class ScheduleSettings extends ConfigBase {
	/*
	 * constants
	 */
	public static final String JOB_TITLE = "title";
	public static final String SCHEDULE_DESC = "description";
	public static final String SCHEDULE_INTERVAL = "interval";
	public static final String SCHEDULE_WDAY = "wday";
	public static final String SCHEDULE_CRON_EXP = "cronExpression";
	public static final String SCHEDULE_MIN = "min";
	public static final String SCHEDULE_HOUR = "hour";
	public static final String SCHEDULE_MDAY = "mday";

	/*
	 * properties
	 */
	protected String title;
	protected String description;
	protected String interval;
	protected String wday;
	protected String cronExpression;
	protected int min;
	protected int hour;
	protected int mday;

	/*
	 * getters and setters
	 */
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

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getWday() {
		return wday;
	}

	public void setWday(String wday) {
		this.wday = wday;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMday() {
		return mday;
	}

	public void setMday(int mday) {
		this.mday = mday;
	}

}
