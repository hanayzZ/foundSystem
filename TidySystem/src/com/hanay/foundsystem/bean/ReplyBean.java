package com.hanay.foundsystem.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 创建时间：2015-4-1
 * @description 被评论人和评论内容Bean
 *  "id": "1", 
            "user_id": "1", 
            "username": "hong", 
            "avatar": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150517122540_pic_user_cg.png", 
            "pid": "0", 
            "memory_id": "1", 
            "memory_photo": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150514223209_QQ截图20150514223128.png", 
            "content": "希望你可以找到", 
            "status": "1", 
            "ctime": "2015-05-15 09:54:24", 
            "pusername": ""
 */

public class ReplyBean implements Serializable  {

	private static final long serialVersionUID = 1L;
	private String id;
	private String userId;
	private String username;
	private String avatar;
	private String memoryId;
	private String memoryPhoto;
	private String content;
	private String status;
	private String ctime;
	private String pid;
	private String pusername;
	private List<ReplyBean> replylists;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}




	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}




	public String getMemoryId() {
		return memoryId;
	}


	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getMemoryPhoto() {
		return memoryPhoto;
	}


	public void setMemoryPhoto(String memoryPhoto) {
		this.memoryPhoto = memoryPhoto;
	}


	public String getCtime() {
		return ctime;
	}


	public void setCtime(String ctime) {
		this.ctime = ctime;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getPusername() {
		return pusername;
	}


	public void setPusername(String pusername) {
		this.pusername = pusername;
	}


	public List<ReplyBean> getReplylists() {
		return replylists;
	}


	public void setReplylists(List<ReplyBean> replylists) {
		this.replylists = replylists;
	}


	/** 解析 */
	public static ReplyBean parse(String string) {
		ReplyBean data = new ReplyBean();
		List<ReplyBean> lists = new ArrayList<ReplyBean>();
		try {
			// 返回json的数组
			JSONArray jsonArray = new JSONArray(string);
			for (int i = 0; i < jsonArray.length(); i++) {
				ReplyBean bean = new ReplyBean();
				JSONObject item = jsonArray.getJSONObject(i);
				bean.setAvatar(item.getString("avatar"));
				bean.setId(item.getString("id"));
				bean.setContent(item.getString("content"));
				bean.setMemoryId(item.getString("memory_id"));
				bean.setStatus(item.getString("status"));
				bean.setUserId(item.getString("user_id"));
				bean.setUsername(item.getString("username"));
				bean.setPid(item.getString("pid"));
				bean.setPusername(item.getString("pusername"));
				bean.setCtime(item.getString("ctime"));
				lists.add(bean);
				item = null;
			}
			data.setReplylists(lists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
	/** 解析 */
	public static ReplyBean parse2(String string) {
		ReplyBean data = new ReplyBean();
		List<ReplyBean> lists = new ArrayList<ReplyBean>();
		try {
			// 返回json的数组
			JSONArray jsonArray = new JSONArray(string);
			for (int i = 0; i < jsonArray.length(); i++) {
				ReplyBean bean = new ReplyBean();
				JSONObject item = jsonArray.getJSONObject(i);
				bean.setAvatar(item.getString("avatar"));
				bean.setId(item.getString("id"));
				bean.setContent(item.getString("content"));
				bean.setMemoryId(item.getString("memory_id"));
				bean.setStatus(item.getString("status"));
				bean.setUserId(item.getString("user_id"));
				bean.setUsername(item.getString("username"));
				bean.setPid(item.getString("pid"));
				bean.setPusername(item.getString("pusername"));
				bean.setMemoryPhoto(item.getString("memory_photo"));
				bean.setCtime(item.getString("ctime"));
				lists.add(bean);
				item = null;
			}
			data.setReplylists(lists);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}
