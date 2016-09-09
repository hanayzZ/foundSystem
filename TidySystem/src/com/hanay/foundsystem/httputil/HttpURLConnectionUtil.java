package com.hanay.foundsystem.httputil;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import com.hanay.foundsystem.util.ValueUtil;

/**
 * @author 李海红
 * @date 2014-8-13
 * @description 访问网络的类
 */

public class HttpURLConnectionUtil {

	/**
	 * 发送网络请求,超时时间为10s
	 */
	public static String doPost(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		String responseContent = null;
		try {
			urlConn = sendPost(reqUrl, parameters, "POST", 10000, 10000);
			if (urlConn != null) {
				responseContent = getContent(urlConn);
				if (ValueUtil.isStrNotEmpty(responseContent)) {
					return responseContent.trim();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}

	/**
	 * 发送网络请求,超时时间为30s
	 */
	public static String doLoginPost(String reqUrl, Map<String, String> parameters) {
		HttpURLConnection urlConn = null;
		String responseContent = null;
		try {
			urlConn = sendPost(reqUrl, parameters, "POST", 30000, 30000);
			if (urlConn != null) {
				responseContent = getContent(urlConn);
				if (ValueUtil.isStrNotEmpty(responseContent)) {
					return responseContent.trim();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} finally {
			if (urlConn != null) {
				urlConn.disconnect();
				urlConn = null;
			}
		}
	}

	/**
	 * 获取HttpURLConnection对象
	 */
	private static HttpURLConnection sendPost(String reqUrl, Map<String, String> parameters, String postOrGet, int connectTimeout, int readTimeout) {
		HttpURLConnection urlConn = null;
		try {
			String params = generatorParamString(parameters);
			URL url = new URL(reqUrl);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setRequestMethod(postOrGet);
			urlConn.setUseCaches(false);
			urlConn.setConnectTimeout(connectTimeout);// （单位：毫秒）jdk
			urlConn.setReadTimeout(readTimeout);// （单位：毫秒）jdk 1.5换成这个,读操作超时
			urlConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			urlConn.setDoOutput(true);
			byte[] b = params.getBytes();
			urlConn.getOutputStream().write(b, 0, b.length);
			urlConn.getOutputStream().flush();
			urlConn.getOutputStream().close();
		} catch (Exception e) {
			return null;
		}
		return urlConn;
	}

	/**
	 * 获取服务器返回的结果
	 */
	private static String getContent(HttpURLConnection urlConn) {
		try {
			String responseContent = null;
			InputStream in = urlConn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();
			rd.close();
			in.close();
			return responseContent;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将parameters中数据转换成用"&"链接的http请求参数形式
	 */
	public static String generatorParamString(Map<String, String> parameters) {
		StringBuffer params = new StringBuffer();
		if (parameters != null) {
			for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				String value = parameters.get(name);
				params.append(name + "=");
				try {
					params.append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e.getMessage(), e);
				} catch (Exception e) {
					String message = String.format("'%s'='%s'", name, value);
					throw new RuntimeException(message, e);
				}
				if (iter.hasNext())
					params.append("&");
			}
		}
		return params.toString();
	}

}