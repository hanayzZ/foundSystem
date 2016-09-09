package com.hanay.foundsystem.volley;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanay.foundsystem.bean.Result;


/**
 * @author 李海红
 * @date 2014-3-12
 * @description Volley工具类
 */

public class VolleyUtils {
	/**
	 * 超时时间
	 */
	private static final int TIME_OUT = 10000;
	private static RequestQueue mRequestQueue;

	public static void initVolley(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/**
	 * 使用post请求数据
	 * 
	 * @param url
	 *            请求链接
	 * @param parms
	 *            请求参数
	 * @param listener
	 *            请求结果监听
	 */
	public static void postVolley(String url, final Map<String, String> parms, final VolleyCallBack listener) {
		listener.httpLoading();
		try {
//			Log.i("TAG", url);
			StringRequest stringRequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {// 请求成功

						@Override
						public void onResponse(String string) {
							listener.httpSucceed(Result.parse(string));
						}
					}, new Response.ErrorListener() {// 请求失败

						@Override
						public void onErrorResponse(VolleyError volleyError) {
							Log.i("TAG", volleyError.toString());
							String error = volleyError.toString();
							String strError = null;
							if (error.contains("TimeoutError"))
								strError = "请求超时,请重新连接";
							else if (error.contains("ServerError"))
								strError = "服务器响应出错";
							else if (error.contains("NullPointerException"))
								strError = "NullPointerException";
							else
								strError = "网络中断,请检查网络连接是否可用";
							listener.httpFail(strError);
						}
					}) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {// post参数
					return parms;
				}
			};
			// 设置唯一标示
			// stringRequest.setTag(tag);
			// 设置超时连接
			stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			// 将请求放入队列中
			mRequestQueue.add(stringRequest);
		} catch (Exception e) {
			listener.httpFail("请求失败");
		}
	}

	/**
	 * 统计请求
	 */
	public static void statRequest(String url) {
		StringRequest stringRequest = new StringRequest(Method.GET, url, new Response.Listener<String>() {// 请求成功
					@Override
					public void onResponse(String string) {

					}
				}, new Response.ErrorListener() {// 请求失败

					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				});
		// 设置超时连接
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		// 将请求放入队列中
		mRequestQueue.add(stringRequest);
	}
	/**
	 * 上传图片到服务器
	 */
	public static void post(String url, Map<String, String> params, Map<String, File> files,final VolleyCallBack listener){
		listener.httpLoading();
		try {
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(10 * 1000);
		conn.setConnectTimeout(10 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false);
		conn.setRequestMethod("POST"); // Post方式
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
		outStream.write(sb.toString().getBytes());

		// 发送文件数据
		if (files != null)
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"" + "uploadImg" + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
				Log.i("site", file.getValue().getName() + "" + file.getKey());
				sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
				sb1.append(LINEND);
				Log.i("site", sb1.toString());
				outStream.write(sb1.toString().getBytes());
				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				is.close();
				outStream.write(LINEND.getBytes());
			}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();

		// 得到响应码
		int res = conn.getResponseCode();
		InputStream in = conn.getInputStream();
		InputStreamReader isReader = new InputStreamReader(in);
		BufferedReader bufReader = new BufferedReader(isReader);
		String line = null;
		StringBuilder sb2 = new StringBuilder();

		if (res == 200) {
			while ((line = bufReader.readLine()) != null) {
				sb2.append(line);
			}
		}

		outStream.close();
		conn.disconnect();
		//return sb2.toString();
		} catch (Exception e) {
			listener.httpFail("请求失败");
		}
	}
}
