package com.hanay.foundsystem.chat.chatServices;

import java.util.List;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.commons.GlobalStrings;
import com.hanay.foundsystem.chat.util.UnsavedChatMsg;

import android.util.SparseArray;



public class FriendListInfo {

	public static final String strSplitter = GlobalStrings.friendListDivider;
	
	/* single instance */
	private static FriendListInfo mFriendListInfo;
	
	/* list stores all users */
	private List<PrivateChatBean> mListOfFriends;
	private SparseArray<PrivateChatBean> mSparseArrayOfFriends;   // this is to facilitate the search
	
	/* retrieve single instance */
	public static FriendListInfo getFriendListInfo() {
		if(mFriendListInfo == null) {
			mFriendListInfo = new FriendListInfo();
		}
		return mFriendListInfo;
	}
	
	/* private constructor */
	private FriendListInfo() {
		mListOfFriends = InitData.getInitData().getListOfFriends();
		
		mSparseArrayOfFriends = new SparseArray<PrivateChatBean>();
		for(PrivateChatBean uu0 : mListOfFriends) {
			mSparseArrayOfFriends.put(Integer.parseInt(uu0.getId()), uu0);
		}
	}
	
	/*
	public void updateFriendList(String str0) {
		String[] strArr0 = str0.split(strSplitter);
		int type = Integer.parseInt(strArr0[0]);
		
		UserInfo userInfo = new UserInfo(strArr0[1]);
		if(type==1) {
			mListOfFriends.add(userInfo);
			ChatServiceData.getInstance().newUser(userInfo);
			UnsavedChatMsg.getInstance().newUser(userInfo);
		} else if(type==-1) {
			for(UserInfo ui0 : mListOfFriends) {
				if(ui0.getId() == userInfo.getId()) {
					mListOfFriends.remove(mListOfFriends.indexOf(ui0));
					ChatServiceData.getInstance().offLineUser(ui0);
					break;
				}
			}
		}
		
		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
			FriendListPage.getInstance().onFriendListUpdate();
		}
	}
	*/
	
	public void uponMakeNewFriend(PrivateChatBean uu0) {
		int newId =Integer.parseInt( uu0.getId());
		if(mSparseArrayOfFriends.get(newId) != null) {
			return;      // we need to be careful to avoid double insert
		}
		
		mListOfFriends.add(uu0);
		mSparseArrayOfFriends.put(Integer.parseInt(uu0.getId()), uu0);
		
		ChatServiceData.getInstance().newUser(uu0);
		UnsavedChatMsg.getInstance().newUser(uu0);
//		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
//			FriendListPage.getInstance().onFriendListUpdate();
//		}
	}
	
	public void friendGoOnAndOffline(PrivateChatBean uup, int onOff) {
		
//		int id = Integer.parseInt(uup.getId());
//		
//		PusherPhoneBean uux = mSparseArrayOfFriends.get(id);
//		uux.setIsOnline(onOff);
		
//		if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageFriendList) {
//			FriendListPage.getInstance().onFriendListUpdate();
//		}
	}
	
	public PrivateChatBean getUserFromId(int id) {
		
		return mSparseArrayOfFriends.get(id);
	}
	
	public List<PrivateChatBean> getFriendList() {
		return mListOfFriends;
	}
	
	public String getNameFromId(int id) {
		return mSparseArrayOfFriends.get(id).getName();
	}
	
	public static void closeFriendListInfo() {
		mFriendListInfo = null;
	}
	
	public boolean isIdFriend(int id) {
		if(mSparseArrayOfFriends.get(id) != null) {
			return true;
		} else {
			return false;
		}
	}
	
}
