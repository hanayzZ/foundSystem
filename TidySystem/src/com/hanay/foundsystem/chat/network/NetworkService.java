package com.hanay.foundsystem.chat.network;

import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

public class NetworkService {

	private static NetworkService mInstance;

	public static NetworkService getInstance() {
		if (mInstance == null) {
			mInstance = new NetworkService();
		}
		return mInstance;
	}

	private boolean mIsConnected;
	private NetConnect mNetCon;
	private ClientListenThread mListenThread;
	private ClientSendThread mSendThread;
	private Socket mSocket;
	private Context mContext;

	// here, it should always do nothing except set mIsConnected to false
	private NetworkService() {
		mIsConnected = false;
	}

	public void onInit(Context context) {
		mContext = context;
	}

	public void setupConnection() {
		mNetCon = new NetConnect();
		mNetCon.start();
		try {
			mNetCon.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mNetCon == null || !mNetCon.connectedOrNot()) {
			mIsConnected = false;
			return;
		} else {
			mSocket = mNetCon.getSocket();
			mIsConnected = true;
			startListen(mContext);

			if (mSocket != null) {
				sendHeartPacket();
				System.out.println("socket is not null");
			} else {
				System.out.println("socket is null");
			}
		}
	}

	private void sendHeartPacket() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						mSocket.sendUrgentData(0xFF);
						Log.i("sendUrgentData", System.currentTimeMillis() + "");
						Thread.sleep(10000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						break;
					} 

				}
			}

		}).start();

	}

	private void startListen(Context context0) {
		mListenThread = new ClientListenThread(context0, mSocket);
		mListenThread.start();

		mSendThread = new ClientSendThread();
	}

	public boolean getIsConnected() {
		return mIsConnected;
	}

	public void sendUpload(int type, String sentence) {
		// sendUpload(type + "");
		sendUpload(sentence);
	}

	/* synchronized so only one send action is happening at a time */
	private synchronized void sendUpload(String buff) {
		// buff = buff.replace("\n", GlobalStrings.replaceOfReturn);
		mSendThread.start(mSocket, buff);
	}

	public void closeConnection() {
		try {
			if (mListenThread != null) {
				mListenThread.closeBufferedReader();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (mSocket != null) {
				mSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mSocket = null;
		mIsConnected = false;
	}

}
