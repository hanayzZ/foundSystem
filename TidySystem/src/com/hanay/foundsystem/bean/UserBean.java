package com.hanay.foundsystem.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 创建时间：2015-4-2
 * @description 登陆接口返回参数
 *   "id": "1", 
        "phone": "2147483647", 
        "username": "hong", 
        "password": "e10adc3949ba59abbe56e057f20f883e", 
        "avatar": "http://emptylane-uploads.stor.sinaapp.com/uploads/20150510200928_default_avatar.png", 
        "ctime": "0000-00-00 00:00:00", 
        "lastlogin": "2015-05-14 22:49:27", 
        "login_count": "4", 
        "mtime": "2015-05-14 22:49:27", 
        "status": "1"
 */

public class UserBean {
	private String userid;
	private String username;
	private String phone;
	private String password;
	private String  avatar;//头像
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/** 解析 */
	public static UserBean parse(String string) {
		UserBean bean = new UserBean();
		try {
			JSONObject object = new JSONObject(string);
			bean.setUserid(object.getString("id"));
			bean.setAvatar(object.getString("avatar"));
			bean.setPassword(object.getString("password"));
			bean.setUsername(object.getString("username"));
			bean.setPhone(object.getString("phone"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return bean;
	}
}
