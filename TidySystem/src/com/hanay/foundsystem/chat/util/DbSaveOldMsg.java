package com.hanay.foundsystem.chat.util;

import java.util.ArrayList;
import java.util.List;

import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.bean.FrdReqNotifItemEntity;
import com.hanay.foundsystem.chat.bean.TabMsgItemEntity;
import com.hanay.foundsystem.chat.commons.GlobalInts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseIntArray;


public class DbSaveOldMsg {

	private static final String DbName = "oldmsgs.db";
	private static DbSaveOldMsg mInstance;
	private static Context mContext0;
	
	private SQLiteDatabase mSqlDb;

	private int mStartPoint;
	private final int mReadLength = 10;
	private SparseIntArray mStartPointArr;
	
	public static DbSaveOldMsg getInstance() {
		if(mInstance == null) {
			mInstance = new DbSaveOldMsg();
		}
		return mInstance;
	}
	
	private DbSaveOldMsg() {		
		mSqlDb = mContext0.openOrCreateDatabase(mContext0.getFilesDir().getParent() +
									"/databases/" + DbName, Context.MODE_PRIVATE, null);
		mStartPointArr = new SparseIntArray();
	}
	
	public static void onInit(Context context0) {
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
		String strOfEntity = entity.toString();
		
		mSqlDb.execSQL("create table if not exists oldMsg_" +
						masterId + "_" +
						guestId +
						" (_index			integer		primary key, " +
						"isSelf				int, " +
						"strEntity			text)"); 
		mSqlDb.execSQL("insert into oldMsg_" + masterId + "_" + guestId + 
						" (isSelf,strEntity)" + 
						" values(?,?)",
						new Object[] {isSelf,strOfEntity});
		
	}
	
	public int getMsg(ArrayList<ChatEntity> chatList, ArrayList<Boolean> boolList, int masterId, int guestId) {
		int key = getKeyFromMasterGuestId(masterId, guestId);
		mStartPoint = mStartPointArr.get(key);
		
		mSqlDb.execSQL("create table if not exists oldMsg_" +
				masterId + "_" +
				guestId +
				" (_index			integer		primary key, " +
				"isSelf				int, " +
				"strEntity			text)"); 
		Cursor curs = mSqlDb.rawQuery("SELECT * from OldMsg_" + masterId + "_" + guestId + 
									" ORDER BY _index DESC LIMIT " + 
									mStartPoint + ", " + mReadLength, null);
		if(!curs.moveToFirst()) {
			return 0;
		} else {
			mStartPoint += curs.getCount();
			mStartPointArr.put(key, mStartPoint);
		}
		
		do {
			String strEnt0 = curs.getString(curs.getColumnIndex("strEntity"));
			ChatEntity ent0 = new ChatEntity(strEnt0);
			chatList.add(0,ent0);
			int isSelf = curs.getInt(curs.getColumnIndex("isSelf"));
			if(isSelf == 1) {
				boolList.add(0,true);
			} else {
				boolList.add(0,false);
			}
		} while(curs.moveToNext());
		
		int cursGetcount = curs.getCount();
		curs.close();
		return cursGetcount;
	}
	
	public int getKeyFromMasterGuestId(int masterId, int guestId) {
		int key = masterId * GlobalInts.MaxFriendNumber * 2 + guestId;
		return key;
	} 
	
	/************************************ for TabMsgItems ***************************************/
	// masterId is the id of client user, guestId is the id of user this guy is talking to
		public void saveTabMsgItem(int id, List<TabMsgItemEntity> listOfEnt0) {			
			mSqlDb.execSQL("create table if not exists oldTabMsg_" +
							id +
							" (_index			integer		primary key, " +
							"strEntity			text)"); 
			
			mSqlDb.execSQL("delete from oldTabMsg_" + id);
			
	 		for(TabMsgItemEntity ent0 : listOfEnt0) {
				String strOfEntity = ent0.toString();
				mSqlDb.execSQL("insert into oldTabMsg_" + id + 
								" (strEntity)" + 
								" values(?)",
								new Object[] {strOfEntity});
			}
		}
		
		public void getTabMsgItem(int id, ArrayList<TabMsgItemEntity> msgItemList) {
			mSqlDb.execSQL("create table if not exists oldTabMsg_" +
					id +
					" (_index			integer		primary key, " +
					"strEntity			text)");
			
			Cursor curs = mSqlDb.rawQuery("SELECT * from OldTabMsg_" + id + 
										" ORDER BY _index DESC", null);
			if(!curs.moveToFirst()) {
				return;
			}
			
			do {
				String strEnt0 = curs.getString(curs.getColumnIndex("strEntity"));
				TabMsgItemEntity ent0 = new TabMsgItemEntity(strEnt0);
				
				msgItemList.add(0, ent0);
			} while(curs.moveToNext());
			
			curs.close();
		}
	
	/************************************ for TabMsgItems ***************************************/
		
		
	/************************************ for Frd Req Notifications **************************************/
	public void saveFrdReqNotif(int id, List<FrdReqNotifItemEntity> listOfEnt0) {			
		mSqlDb.execSQL("create table if not exists oldFrdReqNotif_" +
						id +
						" (_index			integer		primary key, " +
						"strEntity			text)"); 
		
		mSqlDb.execSQL("delete from oldFrdReqNotif_" + id);
		
		for(FrdReqNotifItemEntity ent0 : listOfEnt0) {
			String strOfEntity = ent0.toString();
			mSqlDb.execSQL("insert into oldFrdReqNotif_" + id + 
							" (strEntity)" + 
							" values(?)",
							new Object[] {strOfEntity});
		}
	}
	
	public void getFrdReqNotif(int id, ArrayList<FrdReqNotifItemEntity> msgItemList) {
		mSqlDb.execSQL("create table if not exists oldFrdReqNotif_" +
				id +
				" (_index			integer		primary key, " +
				"strEntity			text)");
		
		Cursor curs = mSqlDb.rawQuery("SELECT * from oldFrdReqNotif_" + id + 
									" ORDER BY _index DESC", null);
		if(!curs.moveToFirst()) {
			return;
		}
		
		do {
			String strEnt0 = curs.getString(curs.getColumnIndex("strEntity"));
			FrdReqNotifItemEntity ent0 = new FrdReqNotifItemEntity(strEnt0);
			
			msgItemList.add(0, ent0);
		} while(curs.moveToNext());
		
		curs.close();
	}
	/************************************ for Frd Req Notifications **************************************/	
	
	
	/****************************** unread messages ***************************************************/
	public void saveUnreadMsgs(int myId, int friendId, int unreadAm) {			
		mSqlDb.execSQL("create table if not exists oldUnreadMsgs_" +
						myId +
						" (_friendId			integer, " +
						"unreadAmount			integer)"); 
		
			mSqlDb.execSQL("delete from oldUnreadMsgs_" + myId + 
							" where _friendId=?",
							new Object[] {friendId});
			
			mSqlDb.execSQL("insert into oldUnreadMsgs_" + myId + 
					" (_friendId,unreadAmount) values(?,?)",
					new Object[] {friendId,unreadAm});
	}
	
	public int getUnreadMsgs(int myId, int friendId) {
		mSqlDb.execSQL("create table if not exists oldUnreadMsgs_" +
				myId +
				" (_friendId			integer, " +
				"unreadAmount			integer)");
		
		Cursor curs = mSqlDb.rawQuery("SELECT * from oldUnreadMsgs_" + myId + 
				" where _friendId=" + friendId, null);
		if(!curs.moveToFirst()) {
			return 0;
		}
		
		int num = curs.getInt(curs.getColumnIndex("unreadAmount"));
		return num;
	}
	/****************************** unread messages ***************************************************/
	
	public void close() {
		if(mSqlDb != null) {
			mSqlDb.close();
			mInstance = null;
		}
	}
	
}