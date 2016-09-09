package com.hanay.foundsystem.chat.util;

import java.util.ArrayList;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.bean.UserInfo;

import android.util.SparseArray;


public class UnsavedChatMsg {

	private static UnsavedChatMsg mInstance;

	/* List for friend chatting */
	private SparseArray<ArrayList<ChatEntity>> mMapFriendsEntity;
	private SparseArray<ArrayList<Boolean>> mMapFriendsSelf;
	
	public static UnsavedChatMsg getInstance() {
		if(mInstance == null) {
			mInstance = new UnsavedChatMsg();
		}
		return mInstance;
	}
	
	private UnsavedChatMsg() {
		mMapFriendsEntity = new SparseArray<ArrayList<ChatEntity>>();
		mMapFriendsSelf = new SparseArray<ArrayList<Boolean>>();
	}
	
	public void newUser(PrivateChatBean userInfo) {
		int id = Integer.parseInt(userInfo.getId());
		mMapFriendsEntity.put(id, new ArrayList<ChatEntity>());
		mMapFriendsSelf.put(id, new ArrayList<Boolean>());
	}
	
	public void newMsg(int type, int id, ChatEntity entity0, boolean isSelf0) {
		mMapFriendsEntity.get(id).add(entity0);
		mMapFriendsSelf.get(id).add(isSelf0);
	}
	
	public SparseArray<ArrayList<ChatEntity>> getMapFriendsEntity() {
		return mMapFriendsEntity;
	}
	
	public SparseArray<ArrayList<Boolean>> getMapFriendsSelf() {
		return mMapFriendsSelf;
	}
	
	public static void closeChatServiceData() {
		mInstance = null;
	}
}