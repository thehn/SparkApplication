package kr.gaion.ceh.restapi.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DateBuilder;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.DateBuilder.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.gaion.ceh.common.bean.settings.AlgorithmSettings;
import kr.gaion.ceh.common.interfaces.IConfigurable;

/**
 * to create trigger for scheduling job
 * 
 * @author hoang
 *
 */
public class TriggerUtil {
	/**
	 * property
	 */
	protected IConfigurable config;
	private final static Logger logger = LoggerFactory.getLogger(TriggerUtil.class);
	/**
	 * mapping weekday values
	 */
	private static final Map<String, Integer> WEEKDAY_MAPPING = new HashMap<>();
	static {
		WEEKDAY_MAPPING.put("Sunday", SUNDAY);
		WEEKDAY_MAPPING.put("Monday", MONDAY);
		WEEKDAY_MAPPING.put("Tuesday", TUESDAY);
		WEEKDAY_MAPPING.put("Wednesday", WEDNESDAY);
		WEEKDAY_MAPPING.put("Thursday", THURSDAY);
		WEEKDAY_MAPPING.put("Friday", FRIDAY);
		WEEKDAY_MAPPING.put("Saturday", SATURDAY);
	}

	/*
	 * Constants
	 */
	public static final String INTERVAL_HOURLY = "hourly";
	public static final String INTERVAL_DAILY = "daily";
	public static final String INTERVAL_WEEKLY = "weekly";
	public static final String INTERVAL_MONTHLY = "monthly";
	public static final String INTERVAL_CRON = "cron";

	public TriggerUtil(IConfigurable config) {
		this.config = config;
	}

	/**
	 * to get trigger from setting values
	 * 
	 * @return
	 */
	public Trigger getTrigger() {

		String interval = config.getSetting(AlgorithmSettings.SCHEDULE_INTERVAL);
		Trigger trigger = null;

		switch (interval) {
		case INTERVAL_HOURLY: {
			int min = config.getSetting(AlgorithmSettings.SCHEDULE_MIN);
			DateBuilder.validateMinute(min);
			String cronExpression = String.format("0 %d * ? * *", min);
			trigger = newTrigger().withSchedule(cronSchedule(cronExpression)).startNow().build();
			break;
		}
		case INTERVAL_DAILY: {
			int hour = config.getSetting(AlgorithmSettings.SCHEDULE_HOUR);
			DateBuilder.validateHour(hour);
			trigger = newTrigger().withSchedule(dailyAtHourAndMinute(hour, 0)).build();
			break;
		}
		case INTERVAL_WEEKLY: {
			String wday = config.getSetting(AlgorithmSettings.SCHEDULE_WDAY);
			int hour = config.getSetting(AlgorithmSettings.SCHEDULE_HOUR);
			DateBuilder.validateHour(hour);
			int dayOfWeek = WEEKDAY_MAPPING.get(wday);
			trigger = newTrigger().withSchedule(weeklyOnDayAndHourAndMinute(dayOfWeek, hour, 0)).build();
			break;
		}
		case INTERVAL_MONTHLY: {
			int dayOfMonth = config.getSetting(AlgorithmSettings.SCHEDULE_MDAY);
			int hour = config.getSetting(AlgorithmSettings.SCHEDULE_HOUR);
			DateBuilder.validateHour(hour);
			DateBuilder.validateDayOfMonth(dayOfMonth);
			trigger = newTrigger().withSchedule(monthlyOnDayAndHourAndMinute(dayOfMonth, hour, 0)).build();
			break;
		}
		case INTERVAL_CRON: {
			String cronExpression = config.getSetting(AlgorithmSettings.SCHEDULE_CRON_EXP);
			trigger = newTrigger().withSchedule(cronSchedule(cronExpression)).startNow().build();
			break;
		}
		default: {
			logger.warn("interval value is invalid. please check it again!");
			break;
		}
		}

		return trigger;
	}

}
