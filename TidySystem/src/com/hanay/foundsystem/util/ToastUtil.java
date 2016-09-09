package com.hanay.foundsystem.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author
 * @version 创建时间：2014-12-13
 * @description Toast工具类
 */

public class ToastUtil {
	public static Toast toast;

	public static void ToastMessage(Context context, String msg) {
		if (toast == null) {
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
			toast.setDuration(Toast.LENGTH_SHORT);
		}
		toast.show();
	}

	public static void ToastMessage(Context context, int msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context context, String msg, int time) {
		Toast.makeText(context, msg, time).show();
	}

}
