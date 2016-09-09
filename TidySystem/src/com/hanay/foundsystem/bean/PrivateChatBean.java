package com.hanay.foundsystem.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author  李海红
 * @version 创建时间：2014-12-26
 * @description 私聊Bean
 */ 
public class PrivateChatBean implements Serializable{

	private static final long serialVersionUID = 9009694298619713846L;
	private String id;
	private String create_datetime;
	private String end_datetime;
	private String start_datetime;
	private String name;
	private String img;
	private List<PrivateChatBean> lists;
	private int flag;

	public String getId() {
		return id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreate_datetime() {
		return create_datetime;
	}

	public void setCreate_datetime(String create_datetime) {
		this.create_datetime = create_datetime;
	}

	public String getEnd_datetime() {
		return end_datetime;
	}

	public void setEnd_datetime(String end_datetime) {
		this.end_datetime = end_datetime;
	}

	public String getStart_datetime() {
		return start_datetime;
	}

	public void setStart_datetime(String start_datetime) {
		this.start_datetime = start_datetime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PrivateChatBean> getLists() {
		return lists;
	}

	public void setLists(List<PrivateChatBean> lists) {
		this.lists = lists;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	/** 解析 */
	// img,id,create_datetime,end_datetime,start_datetime,name
	public static PrivateChatBean parse(String string,String userId) {
		PrivateChatBean data = new PrivateChatBean();
		List<PrivateChatBean> lists = new ArrayList<PrivateChatBean>();
		try {
			JSONObject jsonObject = new JSONObject(string);
			data.setFlag(Integer.valueOf(jsonObject.getString("flag")));
			// 返回json的数组
			JSONArray jsonArray = jsonObject.getJSONArray("info");
			for (int i = 0; i < jsonArray.length(); i++) {
				PrivateChatBean bean = new PrivateChatBean();
				JSONObject item = jsonArray.getJSONObject(i);
				// 小组图片
				JSONArray arrayimg = item.getJSONArray("img");
				// 获取第一张图片
				bean.setImg(arrayimg.getString(0));
				bean.setId(item.getString("id"));
				bean.setCreate_datetime(item.getString("create_datetime"));
				bean.setEnd_datetime(item.getString("end_datetime"));
				bean.setStart_datetime(item.getString("start_datetime"));
				bean.setName(item.getString("name"));
				item = null;
				if(!userId.equals(bean.getId()))
					lists.add(bean);
			}
			data.setLists(lists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

}
