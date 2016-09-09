package com.hanay.foundsystem.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 李海红   
 * @version 创建时间：2014-12-31
 * @description 用户发布的状态Bean
 *         "id": "1", 
            "user_id": "1", 
            "username": "hong", 
            "avatar": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150510200928_default_avatar.png", 
            "topic_id": "1", 
            "title": "寻找爱犬", 
            "photo": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150512144923_gg.jpg", 
            "content": "今天下午2点，在苏州市工业园区丢失爱犬，望看到的人能联系我，非常感谢", 
            "ctime": "2015-05-12 14:49:23"
 */
public class StatusBean implements Serializable {
	private static final long serialVersionUID = 3032082888437287995L;
	private String memoryId;//状态Id
	private String userId;//用户id
	private String userName;//用户名
	private String  avatar;//头像
	private String  topicId;//话题Id
	private List<String> imglist;// 图片路径lists
	private String photo;// 图片
	private String content;// 内容描述
	private String ctime;// 上传时间
	private List<StatusBean> list;


	public String getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(String memoryId) {
		this.memoryId = memoryId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<String> getImglist() {
		return imglist;
	}

	public void setImglist(List<String> imglist) {
		this.imglist = imglist;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public List<StatusBean> getList() {
		return list;
	}

	public void setList(List<StatusBean> list) {
		this.list = list;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** 解析 */
	public static StatusBean parse(String string) {

		StatusBean data = new StatusBean();
		List<StatusBean> list = new ArrayList<StatusBean>();
		try {
			JSONArray array=new JSONArray(string);
			for (int i = 0; i < array.length(); i++) {
				StatusBean bean = new StatusBean();
				JSONObject object = array.getJSONObject(i);
				bean.setMemoryId(object.getString("id"));
				bean.setUserId(object.getString("user_id"));
				bean.setUserName(object.getString("username"));
				bean.setAvatar(object.getString("avatar"));
				bean.setTopicId(object.getString("topic_id"));
				bean.setContent(object.getString("content"));
				bean.setCtime(object.getString("ctime"));
				bean.setPhoto(object.getString("photo"));
				List<String> imglist = new ArrayList<String>();
				imglist.add(object.getString("photo"));
				bean.setImglist(imglist);
				/*	// 小组图片
				JSONArray arrayimg = object.getJSONArray("img");
				List<String> imglist = new ArrayList<String>();
				for (int j = 0; j < arrayimg.length(); j++) {
					imglist.add(arrayimg.getString(j));
				}
				bean.setImglist(imglist);*/

				list.add(bean);
				object = null;
			}
			data.setList(list);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	/** 解析上传数据时，从服务器获取的数据 */
	public static StatusBean parse2(String string) {
		StatusBean bean = new StatusBean();
		try {
			JSONObject object=new JSONObject(string);
			bean.setMemoryId(object.getString("id"));
			bean.setUserId(object.getString("user_id"));
			bean.setUserName(object.getString("username"));
			bean.setAvatar(object.getString("avatar"));
			bean.setTopicId(object.getString("topic_id"));
			//bean.setTitle(object.getString("title"));
			bean.setContent(object.getString("content"));
			bean.setCtime(object.getString("ctime"));
			bean.setPhoto(object.getString("photo"));
			object = null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
