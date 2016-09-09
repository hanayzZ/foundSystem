package com.hanay.foundsystem.chat.chatServices;

import java.util.ArrayList;
import java.util.List;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.util.UnsavedChatMsg;



public class InitData extends Thread{

	/* singleton instance */
	private static InitData mInitData;
	
	private PrivateChatBean mUserInfo;
	private boolean msg3Recev;
	
	private List<PrivateChatBean> mListOfFriends;
	private boolean msg5Recev;
	
	public static InitData getInitData() {
		if(mInitData == null) {
			mInitData = new InitData();
		}
		return mInitData;
	}
	
	private InitData() {
		msg3Recev = true;
		msg5Recev = true;
		mListOfFriends = new ArrayList<PrivateChatBean>();
	}
	
	@Override
	public void run() {
		while(!(msg3Recev && msg5Recev));
	}
	
	public void msg3Arrive(String str) {
		mUserInfo = new PrivateChatBean();
		mUserInfo.setId(str);
		
//		msg3Recev = true; 
//		if(mUserInfo.getId() < 0) {  // invalid username or password, no friendlist will be sent
//			msg5Recev = true;
//		}
	}
	
	public void msg5Arrive(List<PrivateChatBean> list) {
	
		
		
		ChatServiceData mChatServiceData = ChatServiceData.getInstance();
		PrivateChatBean userInfo;
		for(int p=0;p<list.size();p++) {
			 userInfo = list.get(p);
			mListOfFriends.add(userInfo);
			mChatServiceData.newUser(userInfo);
			UnsavedChatMsg.getInstance().newUser(userInfo);
			userInfo=null;
		}
		msg5Recev = true;
	}
	
	public PrivateChatBean getUserInfo() {
		return mUserInfo;
	}
	
	public List<PrivateChatBean> getListOfFriends() {
		return mListOfFriends;
	}
	
	// to close mInitData in order to avoid same thread being started twice
	public static void closeInitData() {
		mInitData = null;
	}
}
