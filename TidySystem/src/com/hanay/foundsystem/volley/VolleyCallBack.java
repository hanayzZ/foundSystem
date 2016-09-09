package com.hanay.foundsystem.volley;

import com.hanay.foundsystem.bean.Result;

/**
 * @author
 * @date 2014-12-17
 * @description Volley回调
 */

public interface VolleyCallBack {

	/**
	 * 请求中
	 */
	void httpLoading();

	/**
	 * 请求成功
	 */
	void httpSucceed(Result result);

	/**
	 * 请求失败
	 */
	void httpFail(String error);

}
