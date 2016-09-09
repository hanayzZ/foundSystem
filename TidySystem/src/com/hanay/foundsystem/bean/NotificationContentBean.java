package com.hanay.foundsystem.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author  李海红
 * @version 创建时间：2015-1-12
 * @description
 */

public class NotificationContentBean {
	private int id;
	private String memory_id;
	private String count;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMemory_id() {
		return memory_id;
	}


	public void setMemory_id(String memory_id) {
		this.memory_id = memory_id;
	}


	public String getCount() {
		return count;
	}


	public void setCount(String count) {
		this.count = count;
	}


	/** 解析 */
	public static List<NotificationContentBean> parse(String json) {
		List<NotificationContentBean> list = new ArrayList<NotificationContentBean>();
		try {
			JSONArray array = new JSONArray(json);
			for (int i = 0; i < array.length(); i++) {
				NotificationContentBean bean = new NotificationContentBean();
				JSONObject jsonObject = array.getJSONObject(i);
				bean.setMemory_id(jsonObject.getString("memory_id"));
				bean.setCount(jsonObject.getString("reply_count"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
