package com.hanay.foundsystem.api;

import java.util.HashMap;

import org.json.JSONObject;

import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.httputil.HttpURLConnectionUtil;
import com.hanay.foundsystem.util.NetWorkUtil;

import android.content.Context;


/**
 * @author
 * @version 创建时间：2015-4-2
 * @description 接口的调用
 */

public class ApiClient {
	public static String hostName = "http://emptylane.sinaapp.com/api";

	/**
	 * 无网络连接
	 */
	public static Result net_is_not_connect() {
		Result result = new Result();
		result.setValidate(false);
		result.setMsg("网络异常,请检查网络设置!");
		return result;
	}

	/**
	 * 登录接口
	 * 
	 * 以后调用接口就按照这样的格式去实现,方法名就是调用接口的名字(方法名第一个字母小写)
	 */
	public static Result checkAccess(Context context, String token, String userName, String userPwd) {
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("token", token);
		HashMap<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("username", userName);
		userInfo.put("password", userPwd);
		parms.put("userInfo", new JSONObject(userInfo).toString());
		if (NetWorkUtil.isNetworkConnected(context)) {
			return Result.parse(HttpURLConnectionUtil.doLoginPost(hostName + "/UserLogin", parms));
		} else {
			return net_is_not_connect();
		}
	}

}
