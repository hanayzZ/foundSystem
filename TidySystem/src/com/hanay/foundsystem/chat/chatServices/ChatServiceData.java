package com.hanay.foundsystem.chat.chatServices;

import java.util.ArrayList;
import java.util.List;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.bean.UserInfo;

import android.util.SparseArray;
import android.util.SparseIntArray;



public class ChatServiceData {

	private static ChatServiceData mChatServiceData;
	
	/* presumed maximum friends a user can have */
	private static final int maxFriends = 2000;

	/* List for friend chatting */
	private List<Integer> mFriendIds;
	private SparseArray<ArrayList<ChatEntity>> mMapFriendsEntity;
	private SparseArray<ArrayList<Boolean>> mMapFriendsSelf;
	private SparseIntArray mMapFriendsUnread;
	


	public static ChatServiceData getInstance() {
		if(mChatServiceData == null) {
			mChatServiceData = new ChatServiceData();
		}
		return mChatServiceData;
	}
	
	private ChatServiceData() {
		
		mFriendIds = new ArrayList<Integer>();
		mMapFriendsEntity = new SparseArray<ArrayList<ChatEntity>>();
		mMapFriendsSelf = new SparseArray<ArrayList<Boolean>>();
		mMapFriendsUnread = new SparseIntArray();
	}
	
	public void newUser(PrivateChatBean userInfo) {
		
		int id = Integer.parseInt(userInfo.getId());
		mFriendIds.add(id);
		mMapFriendsEntity.put(id, new ArrayList<ChatEntity>());
		mMapFriendsSelf.put(id, new ArrayList<Boolean>());
		mMapFriendsUnread.put(id, 0);
		
	}
	
	public void offLineUser(UserInfo userInfo) {
		int id = userInfo.getId();
		
		for(int i = 0;i < mFriendIds.size();i++) {
			if(mFriendIds.get(i).intValue() == id) {
				mFriendIds.remove(i);
				mMapFriendsEntity.remove(i);
				mMapFriendsSelf.remove(i);
				mMapFriendsUnread.removeAt(i);

				break;
			}
		}
	}
	
	public int getIdForPos(int pos) {
		int id = Integer.valueOf(mFriendIds.get(pos));
		return id;
	}
	
	public List<ChatEntity> getCurMsg(int type, int id) {
		if(type ==2) {
			return mMapFriendsEntity.get(id);
		} else {
			return null;
		}
	}
	
	public List<Boolean> getCurIsSelf(int type, int id) {
		if(type == 2) {
			return mMapFriendsSelf.get(id);
		} else {
			return null;
		}
	}
	
	public void increUnreadMsgs(int id) {
		mMapFriendsUnread.put(id, mMapFriendsUnread.get(id) + 1);
	}
	
	public void clearUnreadMsgs(int id) {
		mMapFriendsUnread.put(id, 0);
	}
	
	public void setUnreadMsgs(int id, int am) {
		mMapFriendsUnread.put(id, am);
	}
	
	public int getUnreadMsgs(int id) {
		return mMapFriendsUnread.get(id);
	}
	
	public static void closeChatServiceData() {
		mChatServiceData = null;
	}
}