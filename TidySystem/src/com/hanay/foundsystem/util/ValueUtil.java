package com.hanay.foundsystem.util;

import java.util.List;

import android.content.Context;

/**
 * @author
 * @version 创建时间：2014-12-13
 * @description 工具类
 */

public class ValueUtil {

	public static boolean isStrEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return true;
		} else {
			value = value.replaceAll(" ", "").trim();
			if (null == value || "".equals(value.trim())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isStrNotEmpty(String value) {
		if (null == value || "".equals(value.trim())) {
			return false;
		} else {
			value = value.replaceAll(" ", "").trim();
			if (null == value || "".equals(value.trim())) {
				return false;
			}
		}
		return true;
	}

	public static boolean isListNotEmpty(List<?> noteList) {
		return null != noteList && noteList.size() > 0;
	}

	public static boolean isListEmpty(List<?> noteList) {
		return null == noteList || noteList.size() == 0;
	}

	public static boolean isNotEmpty(Object object) {
		return null != object;
	}

	public static boolean isEmpty(Object object) {
		return null == object;
	}

	public static String getHtmlParamText(String text, String reg) {
		String result = "";
		if (ValueUtil.isStrEmpty(text)) {
			return result;
		}
		String[] texts = text.split("\\" + reg);
		for (int i = 0; i < texts.length; i++) {
			result += "<p>";
			result += texts[i];
			result += "</p>";
		}
		return result;
	}

	public static String getString(Context context, int resId) {
		return null == context ? "" : context.getString(resId);
	}

}
