package com.hanay.foundsystem.chat.network;

import com.hanay.foundsystem.chat.commons.GlobalMsgTypes;

public class AskForUnsendThread extends Thread{
	
	@Override
	public void run() {
		try {
			sleep(500);
		} catch(Exception e) {}
		// to ask for unsend msgs
		NetworkService.getInstance().sendUpload(GlobalMsgTypes.msgAskForUnsendMsgs, "1"); // because here this msg is not used
	}

}
