package com.hanay.foundsystem.imageutils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * @author 李海红
 * @version 创建时间：2014-12-19
 * @description 存放所有的list在最后退出时一起关闭
 */

public class PublicWay {
	public static List<Activity> activityList = new ArrayList<Activity>();

	public static int num = 1;
}
