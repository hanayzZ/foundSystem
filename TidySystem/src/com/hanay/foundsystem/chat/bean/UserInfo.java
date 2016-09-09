package com.hanay.foundsystem.chat.bean;

import java.util.Calendar;

import com.hanay.foundsystem.chat.commons.GlobalStrings;


public class UserInfo {
	
	public static final String strSplitter = GlobalStrings.entityDivider;
	public static final int girl = 0;
	public static final int guy = 1;
	
	public int msgType = 3;
	
	private String mName = "";
	private int mId = 0;
	private int mSex = 0;
	private int mAvatarId = 0;
	private int mIsOnline = 0; // 0 for offline, 1 for online
	private int mBirthYear = 1970;
	private int mBirthMonth = 1;
	private int mBirthDay = 1;
	private String mSignupTime = "xx";
	private String mHometown = "xx";
	private String mCurLocation = "xx";
	
	public UserInfo(String name,int id,int sex,int year, int month, int day,int avatarId) {
		mName = new String(name);
		mId = id;
		mSex = sex;
		mBirthYear = year;
		mBirthMonth = month;
		mBirthDay = day;
		mAvatarId = avatarId;
	}
	
	public UserInfo(String st0)
	{
//		String[] sbArr0 = st0.split(strSplitter);
//		
//		this.mName = new String(sbArr0[0]);
//		this.mId = Integer.parseInt(sbArr0[1]);
//		this.mSex = Integer.parseInt(sbArr0[2]);
//		this.mAge = Integer.parseInt(sbArr0[3]);
//		this.mAvatarId = Integer.parseInt(sbArr0[3]);
//		this.mIsOnline = Integer.parseInt(sbArr0[4]);
//		this.mBirthYear = Integer.parseInt(sbArr0[5]);
//		this.mBirthMonth = Integer.parseInt(sbArr0[6]);
//		this.mBirthDay = Integer.parseInt(sbArr0[7]);
//		this.mSignupTime = sbArr0[8];
//		this.mHometown = sbArr0[9];
//		this.mCurLocation = sbArr0[10];
	}
	
	public String toString()
	{
//		String st0 = mName + strSplitter;
//		st0 += mId + strSplitter;
//		st0 += mSex + strSplitter;
//		st0 += mAge + strSplitter;
//		st0 += mAvatarId + strSplitter;
//		st0 += mIsOnline + strSplitter;
//		st0 += mBirthYear + strSplitter;
//		st0 += mBirthMonth + strSplitter;
//		st0 += mBirthDay + strSplitter;
//		st0 += mSignupTime + strSplitter;
//		st0 += mHometown + strSplitter;
//		st0 += mCurLocation + strSplitter;
//		
//		return st0;
		return "";
	}
	
	public String getName()
	{
		return mName;
	}
	
	public int getId()
	{
		return mId;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	public int getSex()
	{
		return mSex;
	}
	
	public void setSex(int sex) {
		mSex = sex;
	}
	
	public int getAge() {
		int age;
		Calendar calendar0 = Calendar.getInstance();
		int year = calendar0.get(Calendar.YEAR);
		int month = calendar0.get(Calendar.MONTH) + 1;  // because month starts from 0
		int day = calendar0.get(Calendar.DATE);

		age = year - mBirthYear;
		if(month < mBirthMonth) {
			age -= 1;
		} else if(month == mBirthMonth && day < mBirthDay) {
			age -= 1;
		}

		return age;
	}
	
	public int getAvatarId() {
		if(mSex==0) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public int getIsOnline() {
		return mIsOnline;
	}
	
	public void setIsOnline(int onOff) {
		mIsOnline = onOff;
	}
	
	public int getBirthYear() {
		return mBirthYear;
	}

	public void setBirthYear(int year) {
		mBirthYear = year;
	}

	public int getBirthMonth() {
		return mBirthMonth;
	}

	public void setBirthMonth(int month) {
		mBirthMonth = month;
	}

	public int getBirthDay() {
		return mBirthDay;
	}

	public void setBirthDay(int day) {
		mBirthDay = day;
	}
	
	public String getSignupTime() {
		return mSignupTime;
	}
	
	public String getHometown() {
		return mHometown;
	}
	
	public String getCurLocation() {
		return mCurLocation;
	}

}