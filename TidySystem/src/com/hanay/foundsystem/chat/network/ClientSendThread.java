package com.hanay.foundsystem.chat.network;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.chatServices.ChatService;
import com.hanay.foundsystem.chat.chatServices.ChatServiceData;

import android.util.Log;


public class ClientSendThread {

	private Socket mSocket;
	private String mStrToSend;
	public synchronized void start(Socket socket, String str0) {
		this.mSocket = socket;
		this.mStrToSend = str0;

		try {
			OutputStreamWriter ost0 = new OutputStreamWriter(
					mSocket.getOutputStream());
			ost0.write(mStrToSend + "\n");
			ost0.flush();
			updatesend(str0,0);
			Log.d("mStrToSend = " + mStrToSend,
					"++++++++++++++++++++++++++"
							+ "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		} catch (Exception e) {
			e.printStackTrace();
			updatesend(str0,3);
//			NetworkService.getInstance().closeConnection();
//			NetworkService.getInstance().setupConnection();
//    		PusherPhoneBean uu0 = ConnectedApp.getInstance().getUserInfo();
//    		String message1="{\"method\":\"connection\",\"info\":{\"currentUserId\":\""+uu0.getId()+"\"}}";
//    		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgBackOnline, message1);
			Log.d("cannot send to ",
					"++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}
	}
	
	private void  updatesend(String str0,int type){
		
		ChatEntity msgEntity = new ChatEntity(str0);
		int id = msgEntity.getSenderId();
		List<ChatEntity> list = ChatServiceData.getInstance().getCurMsg(2,
				id);
		if (list != null) {
			for (int i = list.size(); i > 0; i--) {

				if (msgEntity.getTime().equals(list.get(i - 1).getTime())) {
						ChatServiceData.getInstance().getCurMsg(2, id)
						.get(i - 1).setType(type);
					ChatService.getInstance().chatActivityDisplayHistory();

					break;
				}
			}
		}
		
		
	}
}
