package com.hanay.foundsystem.chat.bean;

import java.util.*;
import java.text.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.chat.commons.GlobalStrings;
import com.hanay.foundsystem.util.SharedPreferencesUtil;



public class ChatEntity {
	public static final String strSplitter = GlobalStrings.entityDivider;
	
	private int mType=0;
	private int mSenderId=0;
	private int mSenderAvatarId=0;
	private String mUserName="";
	private int mSex=0;
	private String mTime="";
	private String mContent="";
	private int mReceiverId=0;
	
//	public ChatEntity() {}
//	
//	public ChatEntity(int senderId, int avatarId, String name, int sex, String time, String content, 
//			int receiverId) {
//		this.mSenderId = senderId; 
//		this.mSenderAvatarId = avatarId;
//		this.mUserName = name;
//		this.mSex = sex;
//		this.mTime = time;
//		this.mContent = content;
//		this.mReceiverId = receiverId;
//	}
//	
//	public ChatEntity(int type, UserInfo sender, int receiverId, String sentence) {
//		this.mType = type;
//		this.mSenderId = sender.getId();
//		this.mSenderAvatarId = sender.getAvatarId();
//		this.mUserName = sender.getName();
//		this.mSex = sender.getSex();
//		this.mTime = genDate();
//		this.mContent = sentence;
//		this.mReceiverId = receiverId;
//	}
	
	public ChatEntity (String str0)
	{		
//		String[] sbArr0=str0.split(strSplitter);
//		
//		mType = Integer.parseInt(sbArr0[0]);
//		mSenderId = Integer.parseInt(sbArr0[1]);
//		mSenderAvatarId = Integer.parseInt(sbArr0[2]);		
//		mUserName = sbArr0[3];
//		mSex = Integer.parseInt(sbArr0[4]);
//		mTime = sbArr0[5];
//		mContent = sbArr0[6];
//		mReceiverId = Integer.parseInt(sbArr0[7]);
		
		JSONObject jsonobject=null;
		try {
			jsonobject = new JSONObject(str0).getJSONObject("info");
			mContent=jsonobject.getString("message");
			mSenderId=jsonobject.getInt("toUserId");
			mReceiverId=jsonobject.getInt("sendUserId");
			mTime=jsonobject.getString("time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mContent=str0;
			e.printStackTrace();
		}
		
	}
	public ChatEntity (String str0,int type)
	{		

		JSONObject jsonobject=null;
		try {
			jsonobject = new JSONObject(str0).getJSONObject("info");
			mContent=jsonobject.getString("message");
			mSenderId=jsonobject.getInt("sendUserId");
			mReceiverId=jsonobject.getInt("toUserId");
			mTime=jsonobject.getString("time");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			mContent=str0;
			e.printStackTrace();
		}
		
	}
	public String toString()
	{
//		String str=this.mType+strSplitter;
//		str+=this.mSenderId+strSplitter;
//		str+=this.mSenderAvatarId+strSplitter;
//		str+=this.mUserName+strSplitter;
//		str+=this.mSex+strSplitter;
//		str+=this.mTime+strSplitter;
//		str+=this.mContent+strSplitter;
//		str+=this.mReceiverId+strSplitter;
		String str="{\"method\":\"sendMessage\",\"info\":{\"sendUserId\":\""
				+ mSenderId
						+ "\",\"toUserId\":\""
						+ mReceiverId
						+ "\",\"time\":\""
						+ mTime
						+ "\",\"message\":\"" + mContent + "\"}}";
		return str;
	}
	
	public static String genDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		Date date = new Date();
		String timex=dateFormat.format(date);
		return timex;
	}

	public int getType()
	{
		return mType;
	}
	
	public void setType(int type0)
	{
		this.mType=type0;
	}
	
	public int getSenderId()
	{
		return mSenderId;
	}
	
	public void setSenderId(int senderId0)
	{
		this.mSenderId=senderId0;
	}
	
	public int getSenderAvatarid()
	{
	//	return senderAvatarId;
		if(mSex==0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public void setSenderAvatarId(int senderAvatarId0)
	{
		this.mSenderAvatarId=senderAvatarId0;
	}
	
	public String getName() {
		return mUserName;
	}

	public void setName(String nick) {
		this.mUserName = nick;
	}
	
	public int getSex() {
		return mSex;
	}

	public void setSex(int sex) {
		this.mSex = sex;
	}
	
	public String getTime() {
		return mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}
	
	public int getReceiverId()
	{
		return mReceiverId;
	}
	
	public void setReceiverId(int receiverId0)
	{
		this.mReceiverId=receiverId0;
	}
}