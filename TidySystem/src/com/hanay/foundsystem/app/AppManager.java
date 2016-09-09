package com.hanay.foundsystem.app;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * @author 李海红
 * @version 创建时间：2014-12-13
 * @description 应用程序Activity管理类:用于Activity的管理和应用程序退出
 */

public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {

	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity(堆栈中最后一个压入的)
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity(堆栈中最后一个压入的)
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 * 
	 * @param context
	 *            上下文
	 * @param isBackground
	 *            是否开开启后台运行
	 */
	@SuppressWarnings("deprecation")
	public void AppExit(Context context, Boolean isBackground) {
		try {
			Intent intent = new Intent();
			intent.setAction("com.hanay.foundsystem.NotificationService");
			context.stopService(intent);
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
		} catch (Exception e) {

		} finally {
			// 注意,如果有后台程序运行，请不要支持此句子
			if (!isBackground) {
//				System.exit(0);
			}
		}
	}

}
