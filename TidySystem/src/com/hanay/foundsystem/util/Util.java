package com.hanay.foundsystem.util;

import com.hanay.foundsystem.base.BaseApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @author 李海红
 * @version 创建时间：2014-12-13
 * @description 工具类
 */

public class Util {
	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

	/**
	 * 点击软键盘外部使软键盘消失
	 */
	public static void setupUI(View view, final Activity activity) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {
			view.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Util.hideSoftKeyboard(activity);
					return false;
				}
			});
		}

		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				View innerView = ((ViewGroup) view).getChildAt(i);
				setupUI(innerView, activity);
			}
		}
	}

	/**
	 * 动态获取listView的高度(注意事项:listView的item的根布局一定要是LinearLayout)
	 * 
	 * @param listView
	 *            ListView组件
	 */
	public static void getTotalHeightofListView(ListView listView) {
		ListAdapter mAdapter = listView.getAdapter();
		if (mAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View mView = mAdapter.getView(i, null, listView);
			mView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//			mView.measure(0, 0);// 获取item的高度
			totalHeight += mView.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		Log.i("---", "listView的高度：" + params.height);
//		return params.height;
		int screenHeight = BaseApplication.height / 2;
		if (params.height < screenHeight) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			listView.setLayoutParams(layoutParams);
		} else {
			LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, screenHeight);
			listView.setLayoutParams(layoutParams2);
		}
//		listView.setLayoutParams(params);
//		listView.requestLayout();
	}

	/**
	 * 设置TextView部分字体颜色
	 * 
	 * @param textView
	 *            组件
	 * @param string
	 *            字符串
	 * @param color
	 *            颜色值
	 * @param start
	 *            开始位置
	 */
	public static void setTextColor(TextView textView, String string, int color, int start) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(string);
		ssb.setSpan(new ForegroundColorSpan(color), start, string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		textView.setText(ssb);
	}

}
