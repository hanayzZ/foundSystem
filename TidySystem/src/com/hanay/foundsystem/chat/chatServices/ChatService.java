/**
 * the background Service that process the message sending by sendMyMessage()
 * and coming message by newMsgArrive()
 * 
 * it also manage the message queue for ChatActivity
 */

package com.hanay.foundsystem.chat.chatServices;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.network.NetConnect;
import com.hanay.foundsystem.chat.network.NetworkService;
import com.hanay.foundsystem.chat.util.UnsavedChatMsg;
import com.hanay.foundsystem.fragment.MsgFragment;
import com.hanay.foundsystem.util.SharedPreferencesUtil;


public class ChatService extends Service {

	private static ChatService mInstance;

	private ChatActivity mChatActv = null;
	private ChatMsgReceiver mMsgReceiver = null;
	private ChatBinder mBinder = new ChatBinder();

	private int mCurType = 0; // 0 for public room (default), 1 for group room,
	// 2 for friend chatting
	private PrivateChatBean mMyUserInfo;

	private int mFriendId;

	private NetConnect mNetCon;

	private boolean mEnterFromNotification;
	private int mEnterFromNotificationId;
	private int role_id;

	public static ChatService getInstance() {
		return mInstance;
	}

	public class ChatBinder extends Binder {
		public ChatService getService() {
			return ChatService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		role_id = SharedPreferencesUtil.getSharePreInt(getApplicationContext(),
				"userInfo", "role_id");
		mFriendId = -1;

		mInstance = this;

		IntentFilter ifter = new IntentFilter(
				"com.tidy.tidySystem.MESSAGE_RECEIVED");
		mMsgReceiver = new ChatMsgReceiver(this);
		ChatService.this.registerReceiver(mMsgReceiver, ifter);

		mMyUserInfo = ConnectedApp.getInstance().getUserInfo();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ChatService.this.unregisterReceiver(mMsgReceiver);
		NetworkService.getInstance().closeConnection();
		mInstance = null;
	}

	public boolean getEnterFromNotification() {
		return mEnterFromNotification;
		// return true;
	}

	public void setEnterFromNotification(boolean b) {
		mEnterFromNotification = b;
	}

	public int getEnterFromNotificationId() {
		return mEnterFromNotificationId;
	}

	public void setEnterFromNotificationId(int id) {
		mEnterFromNotificationId = id;
	}

	public void setBoundChatActivity(ChatActivity act0) {
		mChatActv = act0;
	}

	public void setType(int type) {
		mCurType = type;
	}

	public void setId(int id) {
		mFriendId = id;
	}

	public int getId() {
		return mFriendId;
	}

	public void sendMyMessage(String st0) {
		// if(mCurType == GlobalMsgTypes.msgFromFriend) {
		// ChatEntity ent0 = new ChatEntity(mCurType, mMyUserInfo,
		// mFriendId, st0);
		// 修改1.29
		// ChatEntity ent0 = new ChatEntity(st0);
		newMsgArrive(st0, true);
		NetworkService.getInstance().sendUpload(mCurType, st0);
		
		// }
	}

	public void newMsgArrive(String str0, boolean isSelf) {
		ChatEntity msgEntity = null;
		if (isSelf) {
			msgEntity = new ChatEntity(str0);
		} else {
			msgEntity = new ChatEntity(str0, 0);
		}

		// int type = msgEntity.getType();
		// id is the id of the one client is talking to, regardless of him/her
		// being sender or receiver

		int id = msgEntity.getSenderId();
		int type = 2;
		// mFriendId=id;
		// if (isSelf) {
		// id = mFriendId;
		// }
		if (mChatActv != null)
			setId(Integer.parseInt(mChatActv.getToUser().getId()));

		if (id == 0) {
			// id = Integer.parseInt(SharedPreferencesUtil.getSharePreStr(
			// getApplicationContext(), "userInfo", "userid"));
			return;
		}

		ChatServiceData.getInstance().getCurMsg(type, id).add(msgEntity);
		ChatServiceData.getInstance().getCurIsSelf(type, id).add(isSelf);

		chatActivityDisplayHistory();
		// DbSaveOldMsg.onInit(getApplicationContext());
		// DbSaveOldMsg.getInstance().saveMsg(Integer.parseInt(InitData.getInitData().getUserInfo().getId()),
		// id, isSelf, msgEntity);

		UnsavedChatMsg.getInstance().newMsg(type, id, msgEntity, isSelf);

		if (!ChatActivity.getIsActive() || mFriendId != id) {
			onUpdateNotification(id, msgEntity.getContent());
			ChatServiceData.getInstance().increUnreadMsgs(id);
			switch (role_id) {
			case 1:
				break;
			case 2:
				if (MsgFragment.getAdapter() != null)
					MsgFragment.getAdapter()
							.notifyDataSetChanged();
				break;
			case 3:
				break;
			case 4:
				break;
			case 6:
				if (MsgFragment.getAdapter() != null)
					MsgFragment.getAdapter().notifyDataSetChanged();
				break;
			}

		}

		// if(!ChatActivity.getIsActive() || mFriendId != id) {
		// MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id),
		// msgEntity.getContent(), msgEntity.getTime(), true);
		// } else {
		// MainTabMsgPage.getInstance().onUpdateByUserinfo(FriendListInfo.getFriendListInfo().getUserFromId(id),
		// msgEntity.getContent(), msgEntity.getTime(), false);
		// }

		// if(MainBodyActivity.getCurPage() == MainBodyActivity.mPageMsg) {
		// MainTabMsgPage.getInstance().onUpdateView();
		// }
	}

	public void chatActivityDisplayHistory() {
		if (mChatActv == null) {
			return;
		}
		setId(Integer.parseInt(mChatActv.getToUser().getId()));
		if (ChatActivity.getIsActive()) {
			if (mFriendId > 0) {
				List<ChatEntity> msgs = ChatServiceData.getInstance()
						.getCurMsg(2, mFriendId);
				List<Boolean> isSelf = ChatServiceData.getInstance()
						.getCurIsSelf(2, mFriendId);

				// if(mCurType == GlobalMsgTypes.msgFromFriend) {
				// mChatActv.updateListviewHistory(msgs,isSelf,mCurType,FriendListInfo.getFriendListInfo().
				// getNameFromId(mFriendId));
				if (msgs != null)
					mChatActv.updateListviewHistory(msgs, isSelf, mCurType, "");
				// }
			}
		}
	}

	public void onUpdateNotification(int id, String sentence) {

		CharSequence tickerText = sentence;
		long when = System.currentTimeMillis();
		Notification mNotification = new Notification(R.drawable.ic_launcher,
				tickerText, when);

		mNotification.flags = Notification.FLAG_NO_CLEAR;
		mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		// if(!isForeGround()) {
		mNotification.defaults |= Notification.DEFAULT_SOUND;
		mNotification.defaults |= Notification.DEFAULT_VIBRATE;
		mNotification.defaults |= Notification.DEFAULT_LIGHTS;
		
		
		// }
		mNotification.contentView = null;

		Intent intent = null;
		if (id < 0) {
			return;
		} else {
			intent = new Intent(getApplicationContext(), ChatActivity.class);
			ChatService.getInstance().setEnterFromNotification(true);
			ChatService.getInstance().setEnterFromNotificationId(id);
		}
		for (PrivateChatBean c : InitData.getInitData().getListOfFriends()) {

			if (Integer.parseInt(c.getId()) == id) {
				intent.putExtra("toUser", c);
			}
		}

		PendingIntent contentIntent = PendingIntent.getActivity(
				getApplicationContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.setLatestEventInfo(getApplicationContext(), "小伙伴",
				sentence, contentIntent);
		NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(id, mNotification);
	}
}
