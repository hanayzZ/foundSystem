package com.hanay.foundsystem.chat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.commons.GlobalMsgTypes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ClientListenThread extends Thread {
	private Context mContext0;
	private Socket mSocket0;

	private InputStreamReader mInStrRder0;
	private BufferedReader mBuffRder0;

	public ClientListenThread(Context par, Socket s) {
		this.mContext0 = par;
		this.mSocket0 = s;
	}

	public void run() {
		super.run();
		try {
			mInStrRder0 = new InputStreamReader(mSocket0.getInputStream());
			mBuffRder0 = new BufferedReader(mInStrRder0);
			String resp = null;
			Intent intent = null;
			while (true) {
				if ((resp = mBuffRder0.readLine()) != null) {

					intent = new Intent("com.tidy.tidySystem.MESSAGE_RECEIVED");
					intent.putExtra("com.tidy.msg_received", resp);
					intent.putExtra("com.tidy.msg_type", 2);
					mContext0.sendBroadcast(intent);
					intent = null;
					Log.i("sockettext", "AA==" + resp);

				}
			}
		} catch (IOException e) {
			Log.i("sockettext", "AAAAAAAAAAAAB");
			e.printStackTrace();
			PrivateChatBean uu0 = ConnectedApp.getInstance().getUserInfo();
			if (uu0 != null) {
				NetworkService.getInstance().closeConnection();
				NetworkService.getInstance().setupConnection();

				String message1 = "{\"method\":\"connection\",\"info\":{\"currentUserId\":\""
						+ uu0.getId() + "\"}}";
				NetworkService.getInstance().sendUpload(
						GlobalMsgTypes.msgBackOnline, message1);
			}
		}
	}

	public void uponReceivedMsg() {
		// NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgMsgReceived,
		// "xxxxx");
	}

	public void closeBufferedReader() {
		try {
			mBuffRder0 = null;
		} catch (Exception e) {
		}
	}
}
