package com.hanay.foundsystem.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hanay.foundsystem.base.BaseApplication;
import com.hanay.foundsystem.R;


/**
 * @author
 * @version 创建时间：2014-12-13
 * @description Dialog工具类
 */

public class DialogUtil {

	/**
	 * 自定义AlertDialog
	 */
	public static Dialog alertDialog(View view, Context context) {
		Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setGravity(Gravity.CENTER);
		int width = SharedPreferencesUtil.getSharePreInt(context, "PhoneInfo", "width");
		dialog.getWindow().setLayout((int) (width * 0.8), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// 设置EditText可以弹出软键盘
		dialog.getWindow().setContentView(view);
		return dialog;
	}

	/**
	 * 自定义AlertDialog
	 */
	public static Dialog alertDialog(View view, Activity activity) {
		Dialog dialog = new AlertDialog.Builder(activity).create();
		dialog.show();
		dialog.getWindow().setGravity(Gravity.CENTER);
		int width = SharedPreferencesUtil.getSharePreInt(activity.getApplicationContext(), "PhoneInfo", "width");
		dialog.getWindow().setLayout((int) (width * 0.8), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// 设置EditText可以弹出软键盘
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.getWindow().setContentView(view);
		return dialog;
	}

	/**
	 * 自定义消息的等待框
	 */
	public static Dialog waitDialog(Context context, String string) {
		Dialog waitDialog = new Dialog(context, R.style.CustomDialog);
		waitDialog.setCanceledOnTouchOutside(false);
		waitDialog.setContentView(R.layout.custom_waitdialog);
		TextView textView = (TextView) waitDialog.findViewById(R.id.tv_customDialogContent);
		textView.setText(string);
		waitDialog.getWindow().setGravity(Gravity.CENTER);
		int width = SharedPreferencesUtil.getSharePreInt(context, "PhoneInfo", "width");
		waitDialog.getWindow().setLayout((int) (width * 0.8), android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		return waitDialog;
	}

	/**
	 * 自定义AlertDialog(高度为屏幕一半)
	 */
	public static Dialog alertDialogHalfHeight(View view, Context context) {
		Dialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		dialog.getWindow().setGravity(Gravity.CENTER);
		int width = SharedPreferencesUtil.getSharePreInt(context, "PhoneInfo", "width");
		dialog.getWindow().setLayout((int) (width * 0.8), BaseApplication.height / 2);
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);// 设置EditText可以弹出软键盘
		dialog.getWindow().setContentView(view);
		return dialog;
	}


}
