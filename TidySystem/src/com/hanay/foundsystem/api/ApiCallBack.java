package com.hanay.foundsystem.api;

import com.hanay.foundsystem.bean.Result;

/**
 * @author sky
 * @version 创建时间：2015-1-4
 * @description 接口调用回调
 */

public interface ApiCallBack {

	/**
	 * 请求中
	 */
	void loading();

	/**
	 * 请求成功
	 */
	void succeed(Result result);

	/**
	 * 请求失败
	 */
	void fail(String error);

}
