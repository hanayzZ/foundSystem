package com.hanay.foundsystem.chat.bean;

import com.hanay.foundsystem.chat.commons.GlobalStrings;


public class TabMsgItemEntity {

	public static final int FrdReqNotifId = -1;
	
	/* for friend request processed result */
	public static final int FrdReqUnread = 0;
	public static final int FrdReqAccepted = 1;
	public static final int FrdReqDeclined = 2;
	
	public static final int NotReadYet = 0;
	public static final int ReadAlready = 1;
	
	private int mTalkerId;
	private int mImgId;
	private String mName;
	private String mSentence;
	private String mTime;
	
	private int mIsRead = NotReadYet;
	private int mFrdReqStatus = FrdReqUnread;
	
	public TabMsgItemEntity(int talkerId, int imgId, String name,
							String sentence, String time) {
		mTalkerId = talkerId;
		mImgId = imgId;
		mName = name;
		mSentence = sentence;
		mTime = time;
	}
	
	public TabMsgItemEntity(String str0) {
		String[] arr0 = str0.split(GlobalStrings.entityDivider);
		
		mTalkerId = Integer.parseInt(arr0[0]);
		mImgId = Integer.parseInt(arr0[1]);
		mName = arr0[2];
		mSentence = arr0[3];
		mTime = arr0[4];
		mIsRead = Integer.parseInt(arr0[5]);
		mFrdReqStatus = Integer.parseInt(arr0[6]);
	}
	
	public String toString() {
		String out = mTalkerId + GlobalStrings.entityDivider;
		out += mImgId + GlobalStrings.entityDivider;
		out += mName + GlobalStrings.entityDivider;
		out += mSentence + GlobalStrings.entityDivider;
		out += mTime + GlobalStrings.entityDivider;
		out += mIsRead + GlobalStrings.entityDivider;
		out += mFrdReqStatus + GlobalStrings.entityDivider;
		
		return out;
	}
	
	public int getTalkerId() {
		return mTalkerId;
	}
	
	public int getImgId() {
		return mImgId;
	}
	
	public String getSentence() {
		return mSentence;
	}
	
	public String getName() {
		return mName;
	}
	
	public String getTime() {
		return mTime;
	}
	
	public int getIsRead() {
		return mIsRead;
	}
	
	public void setIsRead(int isRead) {
		mIsRead = isRead;
	}
	
	public int getFrdReqStatus() {
		return mFrdReqStatus;
	}
	
	public void setFrdReqStatus(int status) {
		mFrdReqStatus = status;
	}
	
}
