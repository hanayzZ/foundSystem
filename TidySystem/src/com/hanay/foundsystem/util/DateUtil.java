package com.hanay.foundsystem.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author
 * @version 创建时间：2014-12-22
 * @description 时间转换工具类
 */

public class DateUtil {
	/**
	 * 截取字符串转换为格式为 yy-MM-dd
	 */
	public static String getTime(String string) {
		return string.substring(0, 10);
	}

	/**
	 * 格式化时间
	 * 
	 * @param type
	 *            例如：yyyy-MM-dd
	 * @param time
	 *            时间
	 */
	public static String SDFTime(String type, String time) {
		String makeDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(type, java.util.Locale.getDefault());
		Date d;
		try {
			d = sdf.parse(time);
			long l = d.getTime();
			makeDate = sdf.format(new Date(l));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return makeDate;
	}

	/**
	 * 将字符串转为时间戳
	 */
	public static String getLongTime(String string) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
		Date d;
		try {
			d = sdf.parse(string);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re_time;
	}

	/**
	 * 将字符串转为毫秒
	 */
	public static long getLongTimeMillis(String string) {
		long time = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
		Date d;
		try {
			d = sdf.parse(string);
			time = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 将字符串转为时间
	 */
	public static String getSDFTime(String string) {
		String time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
		Date d;
		try {
			d = sdf.parse(string);
			long l = d.getTime();
			time = sdf.format(new Date(l));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 比较两个时间的大小
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 */
	public static boolean compare(String startTime, String endTime) {
		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(startTime));
			c2.setTime(df.parse(endTime));
		} catch (java.text.ParseException e) {
		}
		int result = c1.compareTo(c2);
//		if (result == 0)
//			System.out.println("c1相等c2");
//		else if (result < 0)
//			System.out.println("c1小于c2");
//		else
//			System.out.println("c1大于c2");
		if (result <= 0)
			return true;
		else
			return false;
	}

	/**
	 * 与今天的时间进行比较
	 * 
	 * @param time
	 *            比较的时间
	 */
	public static int compare(String time) {
		int result = 2;
		String nowDate = CalendarUtil.getNowTime("yyyy-MM-dd");
		java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
		java.util.Calendar c1 = java.util.Calendar.getInstance();
		java.util.Calendar c2 = java.util.Calendar.getInstance();
		try {
			c1.setTime(df.parse(nowDate));
			c2.setTime(df.parse(time));
		} catch (java.text.ParseException e) {
		}
		int i = c1.compareTo(c2);
		if (i == 0) {
			result = 2;
		} else if (i < 0) {
			result = 1;
		}
		return result;
	}

}
