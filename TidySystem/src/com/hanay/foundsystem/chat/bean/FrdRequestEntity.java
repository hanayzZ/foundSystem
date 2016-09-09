package com.hanay.foundsystem.chat.bean;

import com.hanay.foundsystem.chat.commons.GlobalStrings;


public class FrdRequestEntity {

	private static final int idRefuseFriendship = 22;
	private static final int idAcceptFriendship = 23;
	
	private int mStatus;
	private UserInfo mRequester;
	private UserInfo mRequestee;
	
	public FrdRequestEntity(UserInfo requester, UserInfo requestee) {
		mStatus = idRefuseFriendship;
		mRequester = requester;
		mRequestee = requestee;
	}
	
	public FrdRequestEntity(String str0) {
		String[] arr0 = str0.split(GlobalStrings.friendshipRequestDivider);
		mStatus = Integer.parseInt(arr0[0]);
		mRequester = new UserInfo(arr0[1]);
		mRequestee = new UserInfo(arr0[2]);
	}
	
	public String toString() {
		String str0 = mStatus + GlobalStrings.friendshipRequestDivider;
		str0 += mRequester.toString() + GlobalStrings.friendshipRequestDivider;
		str0 += mRequestee.toString() + GlobalStrings.friendshipRequestDivider;
		return str0;
	}
	
	public boolean isAceepted() {
		if(mStatus == idAcceptFriendship) {
			return true;
		}
		return false;
	}
	
	public void accept() {
		mStatus = idAcceptFriendship;
	}
	
	public void decline() {
		mStatus = idRefuseFriendship;
	}
	
	public UserInfo getRequester() {
		return mRequester;
	}
	
	public UserInfo getRequestee() {
		return mRequestee;
	}
	
}
