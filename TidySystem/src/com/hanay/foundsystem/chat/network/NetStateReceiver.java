package com.hanay.foundsystem.chat.network;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.commons.GlobalMsgTypes;


public class NetStateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {        	
            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(currentNetworkInfo.isConnected()) {
            	System.out.println("connected again");
            	
            	if(!NetworkService.getInstance().getIsConnected()) {
            		NetworkService.getInstance().setupConnection();
            		
            		PrivateChatBean uu0 = ConnectedApp.getInstance().getUserInfo();
//            		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgBackOnline, uu0.toString());
            		
            		//修改1.29
            		String message1="{\"method\":\"connection\",\"info\":{\"currentUserId\":\""+uu0.getId()+"\"}}";
            		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgBackOnline, message1);
            		
            		
            	}
            } else {
            	NetworkService.getInstance().closeConnection();
            }
        }
	
}