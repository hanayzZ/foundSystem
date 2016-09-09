package com.hanay.foundsystem.chat.util;

import java.util.ArrayList;

import com.hanay.foundsystem.chat.bean.ChatEntity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class MessageDb {

	private static final String DbName = "oldmsgs.db";
	private static MessageDb mInstance;
	
	private Context mContext0;
	private SQLiteDatabase mSqlDb;

	private int mStartPoint = 0;
	private final int mReadLength = 5;
	
	public static MessageDb getInstance() {
		if(mInstance == null) {
			mInstance = new MessageDb();
		}
		return mInstance;
	}
	
	private MessageDb() {
		mSqlDb = mContext0.openOrCreateDatabase(DbName, Context.MODE_PRIVATE, null);
	}
	
	public void onInit(Context context0) {
		mContext0 = context0;
	}

	/******************************** above are for initialization ************************************/	
	
	// masterId is the id of client user, guestId is the id of user this guy is talking to
	public void saveMsg(int masterId, int guestId, boolean isSelfBool, ChatEntity entity) {
		int isSelf;
		if(isSelfBool) {
			isSelf = 1;
		} else {
			isSelf = 0;
		}
		mSqlDb.execSQL("create table if not exist _" +
						masterId + "_" +
						guestId +
						" (_index			int		primary key		auto_increment," +
						"isSelf				int," +
						"senderId			int," +
						"senderAvatarId		int," +
						"userName			varchar(100)," +
						"gender				int," +
						"time				varchar(100)," +
						"content			text," +
						"receiverId			int)"); 
		mSqlDb.execSQL("insert into _" + masterId + "_" + guestId + 
						" (isSelf,senderId,senderAvatarId,userName,gender,time,content,receiverId)" + 
						" values(?,?,?,?,?,?,?,?)",
						new Object[] {isSelf,entity.getSenderId(),entity.getSenderAvatarid(),
										entity.getName(),entity.getSex(),entity.getTime(),
										entity.getContent(),entity.getReceiverId()});
		
	}
	
	public void getMsg(ArrayList<ChatEntity> chatList, ArrayList<Boolean> boolList, int masterId, int guestId) {
		mSqlDb.execSQL("create table if not exist _" +
				masterId + "_" +
				guestId +
				" (_index			int		primary key		auto_increment," +
				"isSelf				int," +
				"senderId			int," +
				"senderAvatarId		int," +
				"userName			varchar(100)," +
				"gender				int," +
				"time				varchar(100)," +
				"content			text," +
				"receiverId			int)");
		Cursor curs = mSqlDb.rawQuery("SELECT * from _" + masterId + "_" + guestId + 
									" ORDER BY _index DESC LIMIT " + 
									mStartPoint + ", " + mReadLength, null);
		if(!curs.moveToFirst()) {
			return;
		}
		do {
			int isSelf = curs.getInt(curs.getColumnIndex("isSelf"));
			if(isSelf == 1) {
				boolList.add(0,true);
			} else {
				boolList.add(0,false);
			}
			//
			int senderId = curs.getInt(curs.getColumnIndex("senderId"));
			int senderAvatarId = curs.getInt(curs.getColumnIndex("senderAvatarId"));
			String userName = curs.getString(curs.getColumnIndex("userName"));
			int gender = curs.getInt(curs.getColumnIndex("gender"));
			String time = curs.getString(curs.getColumnIndex("time"));
			String content = curs.getString(curs.getColumnIndex("content"));
			int receiverId = curs.getInt(curs.getColumnIndex("receiverId"));
//			ChatEntity entity0 = new ChatEntity(senderId, senderAvatarId, userName, gender, time, 
//					content, receiverId);
//			chatList.add(0,entity0);
		} while(curs.moveToNext());
		
		curs.close();
	}
	
	public void close() {
		if(mSqlDb != null) {
			mSqlDb.close();
			mInstance = null;
		}
	}
	
}
