/**
 * in order to transfer non-primitive information, like socket, class, etc.
 * we need a global class to store these information, that is a child of
 * Application as we are implementing here, you can always access global information
 * stored in ConnectedApp as below:
 *    ConnectedApp connected_app0  =  (ConnectedApp)getApplication();
 * one thing to remember, register ConnectedApp in the manifest file
 */

package com.hanay.foundsystem.chat;

import java.util.ArrayList;
import java.util.List;

import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.bean.PrivateChatBean;

import android.app.Activity;


public  class  ConnectedApp {
	
	private static ConnectedApp mInstance;
	
     private PrivateChatBean mUserInfo;
     
     private Activity mCurActivity;
     private List<Activity> allActivities; 
     
     public List<Activity> getAllActivities() {
		return allActivities;
	}

	public void setAllActivities(List<Activity> allActivities) {
		this.allActivities = allActivities;
	}

	private ChatActivity mChatActivity;
//     private FrdRequestNotifActivity mFrdRequestNotifActivity;

     public static ConnectedApp getInstance() {
    	 if(mInstance == null) {
    		 mInstance = new ConnectedApp();
    	 }
    	 return mInstance;
     }
     
     private ConnectedApp() {
    	 
     }
      
     public PrivateChatBean getUserInfo() {
    	 return mUserInfo;
     }
     
     public void setUserInfo(PrivateChatBean userInfo) {
    	 mUserInfo = userInfo;
     }

     public Activity getCurActivity() {
    	 return mCurActivity;
     }
     
     public void setCurActivity(Activity act0) {
    	 mCurActivity = act0;
     }
    
     public void instantiateListActivity()
     {
    	 allActivities =new ArrayList<Activity>();
     }
     
     public void clearListActivity()
     {
    	 if (allActivities != null) {
	    	 for (Activity act : allActivities) {
	    		 act.finish();
	    	 }
	    	 
	    	 allActivities.clear();
    	 }
     }
     
     public void addItemIntoListActivity(Activity act0)
     {
    	 allActivities.add(act0);
     }
     
     public ChatActivity getChatActivity() {
    	 return mChatActivity;
     }
     
     public void setChatActivity(ChatActivity ca0) {
    	 mChatActivity = ca0;
     }
     
//     public FrdRequestNotifActivity getFrdRequestNotifActivity() {
//    	 return mFrdRequestNotifActivity;
//     }
//     
//     public void setFrdRequestNotifActivity(FrdRequestNotifActivity ll) {
//    	 mFrdRequestNotifActivity = ll;
//     }
     
     public void clearAll() {
    	 clearListActivity();
         mUserInfo = null;
         mCurActivity = null;
         allActivities = null; 
         mInstance = null;
     }
}
