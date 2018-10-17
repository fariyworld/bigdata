package miscellaneous;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class DateUtil {

	private static final Logger LOG = Logger.getLogger(DateUtil.class);

	/**
	 * 获取运行时间, 格式 HH:mm:ss
	 * @param ms	long类型的timestamp 毫秒
	 * @return
	 */
	public static String getElapsedTimeByMilliSecond(long ms){
		return getElapsedTimeBySecond(ms/1000);
	}
	
	/**
	 * 获取运行时间, 格式 HH:mm:ss
	 * @param second	long类型的timestamp 秒
	 * @return
	 */
	public static String getElapsedTimeBySecond(long second){
		String elapsedTime = second/3600 + ":" + second/60%60 + ":" + second%60;
		LOG.info("elapsedTime == " + elapsedTime);
		return elapsedTime;
	}
	
	/**
	 * 
	 * @param date_str	日期字符串		20180929
	 * @param hour		小时（24时制）	15
	 * @return
	 */
	public static Calendar getCalendarByArgs(String date_str, String hour){
		Calendar calendar = Calendar.getInstance();
		calendar.set(
				Integer.parseInt(date_str.substring(0, 4)),//年 2018 
				Integer.parseInt(date_str.substring(4, 6)),//月 09 
				Integer.parseInt(date_str.substring(6, 8)),//日 29 
				Integer.parseInt(hour),//时 15 
				0, 0);//分; 秒
		
		return calendar;
	}
}
