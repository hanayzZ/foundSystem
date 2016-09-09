package com.hanay.foundsystem.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author
 * @version 创建时间：2014-12-13
 * @description 操作SharedPreferences的封装类
 */

public class SharedPreferencesUtil {

	/**
	 * @param mContext
	 *            上下文，来区别哪一个activity调用的
	 * @param whichSp
	 *            使用的SharedPreferences的名字
	 * @param field
	 *            SharedPreferences的哪一个字段
	 */

	/**
	 * 取出whichSp中field字段对应的string类型的值
	 */
	public static String getSharePreStr(Context mContext, String whichSp, String field) {
		SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(whichSp, 0);
		String s = sp.getString(field, "");// 如果该字段没对应值，则取出空字符串
		return s;
	}

	/**
	 * 取出whichSp中field字段对应的int类型的值
	 */
	public static int getSharePreInt(Context mContext, String whichSp, String field) {
		SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(whichSp, 0);
		int i = sp.getInt(field, 0);// 如果该字段没对应值，则取出0
		return i;
	}

	/**
	 * 保存string类型的value到whichSp中的field字段
	 */
	public static void putSharePre(Context mContext, String whichSp, String field, String value) {
		SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(whichSp, 0);
		sp.edit().putString(field, value).commit();
	}

	/**
	 * 保存int类型的value到whichSp中的field字段
	 */
	public static void putSharePre(Context mContext, String whichSp, String field, int value) {
		SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(whichSp, 0);
		sp.edit().putInt(field, value).commit();
	}

}
