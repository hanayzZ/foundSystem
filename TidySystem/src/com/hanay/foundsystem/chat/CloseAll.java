package com.hanay.foundsystem.chat;

import com.hanay.foundsystem.chat.chatServices.ChatServiceData;
import com.hanay.foundsystem.chat.chatServices.FriendListInfo;
import com.hanay.foundsystem.chat.chatServices.InitData;
import com.hanay.foundsystem.chat.network.NetworkService;


public class CloseAll {
	
	public static void closeAll() {
		ConnectedApp.getInstance().clearAll();
		
		try {
			NetworkService.getInstance().closeConnection();
		} catch (Exception e) {}
		try {
			InitData.closeInitData();
			FriendListInfo.closeFriendListInfo();
			ChatServiceData.closeChatServiceData();
		} catch (Exception e) {}
	}
	
}
