package com.hanay.foundsystem.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author  李海红
 * @version 创建时间：2015-5-11
 * @description  话题Bean
 *  "id": "1", 
            "user_id": "1", 
            "username": "hong", 
            "avatar": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150516174213_QQ截图20150514223128.png", 
            "title": "寻找丢失的宠物", 
            "count": "0", 
            "ctime": "2015-05-13 22:37:23"
 */

public class TopicBean {


	private String topicId;
	private String title;
	private String count;
	private List<TopicBean> lists;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<TopicBean> getLists() {
		return lists;
	}

	public void setLists(List<TopicBean> lists) {
		this.lists = lists;
	}

	/** 解析 */
	// img,id,create_datetime,end_datetime,start_datetime,name
	public static TopicBean parse(String string) {
		TopicBean data = new TopicBean();
		List<TopicBean> lists = new ArrayList<TopicBean>();
		try {
			// 返回json的数组
			JSONArray jsonArray =new JSONArray(string);
			for (int i = 0; i < jsonArray.length(); i++) {
				TopicBean bean = new TopicBean();
				JSONObject item = jsonArray.getJSONObject(i);
				bean.setTopicId(item.getString("id"));
				bean.setTitle(item.getString("title"));
				bean.setCount(item.getString("count"));
				lists.add(bean);
				item = null;
			}
			data.setLists(lists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}
