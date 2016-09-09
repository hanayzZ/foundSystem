package com.hanay.foundsystem.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.chatServices.ChatService;
import com.hanay.foundsystem.chat.chatServices.ChatServiceData;
import com.hanay.foundsystem.chat.chatter.ChatListviewAdapter;
import com.hanay.foundsystem.chat.util.DbSaveOldMsg;
import com.hanay.foundsystem.util.SharedPreferencesUtil;

/**
 * @author 李海红
 * @version 创建时间：2014-3-3
 * @description 聊天
 */
public class ChatActivity extends Activity {
	private static boolean mIsActive = false;

	private Handler mHandler;
	private PrivateChatBean mUserInfo;
	/* views contained in this activity */
	private ListView mListviewHistory = null;
	private EditText mEtInput = null;
	private Button mBtnSend = null;
	// private ImageView mImvReadymade=null;

	private ChatListviewAdapter mListviewAdapter;

	/* the top banner needs to maintain its size unchanged */
	private RelativeLayout mRlTop;
	private TextView mTvTop;
	private float dy,uy;
	// chuan
	private String curtime;
	// chuan
	/*
	 * records of current line amount and current height of the layout contains
	 * etDo
	 */
	private LinearLayout mLlEtDo;
	private int mHeightOfTextLayout = 0; // height of layout contains etDo in
	// pixel
	private int mCurLineAcc; // current line account

	/* PublicService and service connection defined here */
	private ChatService mPublicService;
	private ServiceConnection mServiceConnect = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder binder) {
			mPublicService = ((ChatService.ChatBinder) binder).getService();
			mPublicService.setBoundChatActivity(ChatActivity.this);
			new ToDisplayHistory(mHandler).start();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}
	};

	/* instantiate a TextWatcher to auto-tweak the height of input editText */
//	private TextWatcher mTextWatcher = new TextWatcher() {
//		public void afterTextChanged(Editable s) {
//			int LineAcc = ChatActivity.this.mEtInput.getLineCount();
//			if (LineAcc > ChatActivity.this.mCurLineAcc && LineAcc <= 7) {
//				// test codes
//				int topHeight = mRlTop.getHeight();
//
//				int lineIncre = LineAcc - ChatActivity.this.mCurLineAcc;
//				ChatActivity.this.mHeightOfTextLayout += lineIncre
//						* ChatActivity.this.mEtInput.getLineHeight();
//				LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
//						LinearLayout.LayoutParams.MATCH_PARENT,
//						LinearLayout.LayoutParams.WRAP_CONTENT);
//				imagebtn_params.height = ChatActivity.this.mHeightOfTextLayout;
//				ChatActivity.this.mLlEtDo.setLayoutParams(imagebtn_params);
//
//				ChatActivity.this.mCurLineAcc = LineAcc;
//				//
//				// // test codes
//				RelativeLayout.LayoutParams topBanner_params = new RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.MATCH_PARENT,
//						RelativeLayout.LayoutParams.WRAP_CONTENT);
//				topBanner_params.height = topHeight;
//				ChatActivity.this.mRlTop.setLayoutParams(topBanner_params);
//			}
//		}
//
//		public void onTextChanged(CharSequence s, int start, int before,
//				int count) {
//		}
//
//		public void beforeTextChanged(CharSequence s, int start, int count,
//				int after) {
//			ChatActivity.this.mHeightOfTextLayout = ChatActivity.this.mLlEtDo
//					.getHeight();
//		}
//	};
	private PrivateChatBean toUser;

	public PrivateChatBean getToUser() {
		return toUser;
	}

	public void setToUser(PrivateChatBean toUser) {
		this.toUser = toUser;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.chat);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		 ConnectedApp.getInstance().addItemIntoListActivity(this);
		toUser = (PrivateChatBean) getIntent().getSerializableExtra("toUser");
		mListviewHistory = (ListView) findViewById(R.id.listview);
		mEtInput = (EditText) findViewById(R.id.et_sendmessage);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		// mImvReadymade=(ImageView)findViewById(R.id.cb0ChatImvReadymade);
		mTvTop = (TextView) findViewById(R.id.cb0ChatFriendName);
		mTvTop.setText(toUser.getName());
		// pop this activity into listActivity define in ConnectedApp
		 mListviewHistory.setOnTouchListener(new View.OnTouchListener() {
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {
		 switch(event.getAction()) {
		 case MotionEvent.ACTION_UP:
			 uy=event.getY(); 
		 if(mListviewHistory.getFirstVisiblePosition() == 0&&uy-dy>350) {
//		  Toast.makeText(ChatActivity.this, "sorry top already",Toast.LENGTH_SHORT).show();
		  oldmsg();
		 }
		 break;
		 case MotionEvent.ACTION_DOWN:
			 dy=event.getY();
			 break;
		 default:
		 break;
		 }
		
		 return false;
		 }
		 });

		/* add TextWatcher to watch for text line account changing */
//		mLlEtDo = (LinearLayout) findViewById(R.id.btn_bottom);
		mCurLineAcc = 1;
		mRlTop = (RelativeLayout) findViewById(R.id.rl_layout);
//		mEtInput.addTextChangedListener(mTextWatcher);

		// mImvReadymade.setOnClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// ChatEmoticons chatEmoticon = new ChatEmoticons(ChatActivity.this,
		// mEtInput);
		// chatEmoticon.createExpressionDialog();
		// }
		// });

		mBtnSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// chuan
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
				Date curDate = new Date(System.currentTimeMillis());
				curtime = formatter.format(curDate);
				// chuan
				String st0 = mEtInput.getText().toString();
				mEtInput.setText("");

				// if (!st0.equals("")) {
				// test codes

				// }

				if (!st0.equals("")) {
//					int topHeight = mRlTop.getHeight();
//					LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
//							LinearLayout.LayoutParams.MATCH_PARENT,
//							LinearLayout.LayoutParams.WRAP_CONTENT);
//					imagebtn_params.height = mLlEtDo.getHeight()
//							- (mCurLineAcc - 1) * (mEtInput.getLineHeight());
//					mLlEtDo.setLayoutParams(imagebtn_params);
//					mCurLineAcc = 1;
//					// test codes
//					RelativeLayout.LayoutParams topBanner_params = new RelativeLayout.LayoutParams(
//							RelativeLayout.LayoutParams.MATCH_PARENT,
//							RelativeLayout.LayoutParams.WRAP_CONTENT);
//					topBanner_params.height = topHeight;
//					mRlTop.setLayoutParams(topBanner_params);
					String str = "{\"method\":\"sendMessage\",\"info\":{\"sendUserId\":\""
							+ SharedPreferencesUtil.getSharePreStr(
									ChatActivity.this, "userInfo", "userid")
							+ "\",\"toUserId\":\""
							+ toUser.getId()
							+ "\",\"time\":\""
							+ curtime
							+ "\",\"message\":\""
							+ st0 + "\"}}";
					mPublicService.sendMyMessage(str);
				}
			}
		});

		mHandler = new Handler() {

			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					mPublicService.chatActivityDisplayHistory();
					break;

				case 2:
					Toast.makeText(ChatActivity.this,
							"sucess to connect to Server", Toast.LENGTH_LONG)
							.show();
					break;
				case 3:
					Toast.makeText(ChatActivity.this,
							"failed to connect to Server", Toast.LENGTH_LONG)
							.show();
					break;

				}
			}
		};
	
		 
	}

	@Override
	protected void onResume() {
		super.onResume();
		try{
//		ConnectedApp.getInstance().setCurActivity(this);
			
			if(ConnectedApp.getInstance().getAllActivities().size()>1){
				ConnectedApp.getInstance().getAllActivities().get(0).finish();
				ConnectedApp.getInstance().getAllActivities().remove(0);
				
			}
		ChatActivity.mIsActive = true;
		Intent intentTemp = new Intent(ChatActivity.this, ChatService.class);
		bindService(intentTemp, mServiceConnect, Service.BIND_AUTO_CREATE);

		// if (ChatService.getInstance().getEnterFromNotification()) {
		// int id_x = ChatService.getInstance().getEnterFromNotificationId();
		// ChatService.getInstance().setType(2);
		// int id_x =Integer.parseInt(toUser.getId());
		// ChatService.getInstance().setId(id_x);
		 ChatServiceData.getInstance().clearUnreadMsgs(Integer.parseInt(toUser.getId()));
		 ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancel(Integer.parseInt(toUser.getId()));
		//
//		 try {
//		 ChatActivity preced = ConnectedApp.getInstance()
//		 .getChatActivity();
//		 preced.finish();
//		 } catch (Exception e) {
//			 e.printStackTrace();
//		 }
		// }

		ConnectedApp.getInstance().setChatActivity(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		try{
		ChatActivity.mIsActive = false;
		unbindService(mServiceConnect);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean getIsActive() {
		return mIsActive;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * update the history listview with the coming of new message
	 */
	public void updateListviewHistory(List<ChatEntity> msgs,
			List<Boolean> isSelf, int type, String name) {
		mListviewAdapter = new ChatListviewAdapter(this, this, msgs, isSelf,
				toUser);
		mListviewHistory.setAdapter(mListviewAdapter);
		mListviewHistory.setSelection(msgs.size() - 1);
		if(msgs.size()==0){
			oldmsg();
		}
		
	}

	public void chat_back(View view) {
		finish();
	}
private void  oldmsg(){
	 DbSaveOldMsg.onInit(ChatActivity.this);
	 int myId =Integer.parseInt( ConnectedApp.getInstance().getUserInfo().getId());
//	 int id = ChatService.getInstance().getId();
	 int id=Integer.parseInt(toUser.getId());
	 ArrayList<ChatEntity> mapFriendsEntity = (ArrayList<ChatEntity>)
	 ChatServiceData.getInstance().getCurMsg(2, id);
	 ArrayList<Boolean> mapFriendsSelf = (ArrayList<Boolean>)
	 ChatServiceData.getInstance().getCurIsSelf(2, id);
	
	 int incre = DbSaveOldMsg.getInstance().getMsg(mapFriendsEntity,
	 mapFriendsSelf, myId, id);
	
	 int selection = mListviewHistory.getSelectedItemPosition();
	 mListviewAdapter.notifyDataSetChanged();
	 mListviewHistory.setSelection(selection + incre);
}
}

class ToDisplayHistory extends Thread {
	private Handler mHandler;

	public ToDisplayHistory(Handler hl0) {
		mHandler = hl0;
	}

	@Override
	public void run() {
		try {
			sleep(15);
		} catch (Exception e) {
		}
		Message message = new Message();
		message.what = 1;
		mHandler.sendMessage(message);
	}
}
