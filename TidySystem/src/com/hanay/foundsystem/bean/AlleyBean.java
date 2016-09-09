package com.hanay.foundsystem.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 李海红   
 * @version 创建时间：2015-5-4
 * @description 空巷广告图
 *  "id": "1", 
            "content": "你应该是一场梦，我应该是一阵风。", 
            "img": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150513211304_home_pageview.jpg", 
            "status": "1", 
            "priority": "1", 
            "ctime": "2015-05-13 21:13:04", 
            "mtime": "2015-05-13 21:13:05"
 */
public class AlleyBean {
	private String id;
	private String content;
	private String img;
	private String status;
	private String priority;
	private String ctime;
	private String mtime;
	private List<AlleyBean>  alleylists;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getCtime() {
		return ctime;
	}
	public void setCtime(String ctime) {
		this.ctime = ctime;
	}
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	public List<AlleyBean> getAlleylists() {
		return alleylists;
	}
	public void setAlleylists(List<AlleyBean> alleylists) {
		this.alleylists = alleylists;
	}
	/** 解析 */
	public static AlleyBean parse(String string) {

		AlleyBean data = new AlleyBean();
		List<AlleyBean> list = new ArrayList<AlleyBean>();
		try {
			JSONArray array=new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				AlleyBean bean = new AlleyBean();
				JSONObject object = array.getJSONObject(i);
				bean.setId(object.getString("id"));
				bean.setContent(object.getString("content"));
				bean.setCtime(object.getString("ctime"));
				bean.setMtime(object.getString("mtime"));
				bean.setPriority(object.getString("priority"));
				bean.setStatus(object.getString("status"));
				bean.setImg(object.getString("img"));
				list.add(bean);
				object = null;
			}
			data.setAlleylists(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}



}
