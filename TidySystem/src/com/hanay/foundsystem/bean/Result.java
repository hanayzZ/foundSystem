package com.hanay.foundsystem.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.hanay.foundsystem.util.ValueUtil;


public class Result implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean validate;
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	/**
	 * 返回结果处理
	 */
	public static Result standardParse(String string) {
		Result result = new Result();
		if (ValueUtil.isStrNotEmpty(string)) {
			try {
				JSONObject jsonObject = new JSONObject(string);
				if (jsonObject.getInt("status") == 1) {// 返回正确结果
					result.setValidate(true);
					result.setMsg(jsonObject.getString("info"));
				} else if (jsonObject.getInt("status") == 0) {// 返回错误结果
					result.setValidate(false);
					result.setMsg(jsonObject.getString("info"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				result.setValidate(false);
				result.setMsg("解析出错");
				return result;
			}
		} else {
			result.setValidate(false);
			result.setMsg("连接超时,请重试");
		}
		return result;
	}

	/**
	 * 返回结果处理
	 */
	public static Result parse(String string) {
		Result result = new Result();
		if (ValueUtil.isStrNotEmpty(string)) {
			try {
				JSONObject jsonObject = new JSONObject(string);
				if (jsonObject.getInt("status") == 1) {// 返回正确结果
					result.setValidate(true);
					result.setMsg(jsonObject.getString("info"));
				} else if (jsonObject.getInt("status") == 0) {// 返回错误结果
					result.setValidate(false);
					result.setMsg(jsonObject.getString("info"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				result.setValidate(false);
				result.setMsg("解析出错");
				return result;
			}
		} else {
			result.setValidate(false);
			result.setMsg("啊哦,出错了!");
		}
		return result;
	}

}
