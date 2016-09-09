package com.hanay.foundsystem.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.app.AppManager;
import com.hanay.foundsystem.base.BaseFragmentActivity;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.bean.ChatEntity;
import com.hanay.foundsystem.chat.chatServices.ChatService;
import com.hanay.foundsystem.chat.chatServices.ChatServiceData;
import com.hanay.foundsystem.chat.chatServices.FriendListInfo;
import com.hanay.foundsystem.chat.util.DbSaveOldMsg;
import com.hanay.foundsystem.chat.util.UnsavedChatMsg;
import com.hanay.foundsystem.fragment.HomeFragment;
import com.hanay.foundsystem.fragment.MineFragment;
import com.hanay.foundsystem.fragment.MsgFragment;
import com.hanay.foundsystem.fragment.TopicFragment;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ValueUtil;


/**
 * @author      李海红
 * @date 2014-12-24
 * @description     主界面
 */

public class MainActivity extends BaseFragmentActivity implements OnClickListener, OnCheckedChangeListener {
	public static RadioGroup radioGroup;
	private RadioButton main_tab_home, main_tab_msg,main_tab_publish,main_tab_topic, main_tab_mine;

	private FrameLayout frameLayout;

	private Intent intent;

	private Fragment currentFragment;

	/** 空巷标签 */
	private HomeFragment homeFragment;
	/** 消息标签 */
	private MsgFragment msgFragment;
	private View parentView;
	/** 话题 */
	private TopicFragment plazaFragment;
	/** 我的标签 */
	private MineFragment mineFragment;

	/** 网络状态 */
	//private NetStateReceiver mNetStateReceiver;
	private String userId,pusherId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected void setupViews() {
		parentView = getLayoutInflater().inflate(R.layout.activity_main, null);
		radioGroup = (RadioGroup) findViewById(R.id.main_tab_group);
		frameLayout = (FrameLayout) findViewById(R.id.fl_fragmentContent);// 放置切换fragment的布局
		main_tab_home = (RadioButton) findViewById(R.id.main_tab_home);
		main_tab_msg = (RadioButton) findViewById(R.id.main_tab_msg);
		main_tab_publish=(RadioButton) findViewById(R.id.main_tab_publish);
		main_tab_topic = (RadioButton) findViewById(R.id.main_tab_topic);
		main_tab_mine = (RadioButton) findViewById(R.id.main_tab_mine);
	}

	@Override
	protected void initialized() {
		userId = SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "userId");
		main_tab_home.setOnCheckedChangeListener(this);
		main_tab_msg.setOnCheckedChangeListener(this);
		main_tab_topic.setOnCheckedChangeListener(this);
		main_tab_mine.setOnCheckedChangeListener(this);

		main_tab_home.performClick();// 设置APP进入显示首页标签

		main_tab_home.setOnClickListener(this);
		main_tab_msg.setOnClickListener(this);
		main_tab_publish.setOnClickListener(this);
		main_tab_topic.setOnClickListener(this);
		main_tab_mine.setOnClickListener(this);
		// 微聊
		Intent intentTemp = new Intent(MainActivity.this, ChatService.class);
		startService(intentTemp);

		/*	mNetStateReceiver = new NetStateReceiver();
		registerReceiver(mNetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		new AskForUnsendThread().start();
		 */
		//点击其他用户头像，进入个人中心
		pusherId=SharedPreferencesUtil.getSharePreStr(getApplicationContext(), "userInfo","pusherId" );
		if(ValueUtil.isStrNotEmpty(pusherId)){
			main_tab_mine.performClick();
		}
		/*String msgFragment=getIntent().getStringExtra("msgFragment");
		if(msgFragment!=null){
			main_tab_msg.performClick();
		}*/

	}

	public void onReadMsg() {
		DbSaveOldMsg.onInit(this);
		DbSaveOldMsg.getInstance();

		List<PrivateChatBean> friendList = FriendListInfo.getFriendListInfo().getFriendList();
		int myId = Integer.parseInt(ConnectedApp.getInstance().getUserInfo().getId());
		for (PrivateChatBean uu0 : friendList) {
			int id = Integer.parseInt(uu0.getId());
			ArrayList<ChatEntity> mapFriendsEntity = (ArrayList<ChatEntity>) ChatServiceData.getInstance().getCurMsg(2, id);
			ArrayList<Boolean> mapFriendsSelf = (ArrayList<Boolean>) ChatServiceData.getInstance().getCurIsSelf(2, id);
			DbSaveOldMsg.getInstance().getMsg(mapFriendsEntity, mapFriendsSelf, myId, id);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			// instantiateItem从FragmentManager中查找Fragment，找不到就getItem新建一个
			Fragment fragment = (Fragment) mFragmentPagerAdapter.instantiateItem(frameLayout, buttonView.getId());
			// setPrimaryItem设置隐藏和显示
			mFragmentPagerAdapter.setPrimaryItem(frameLayout, 0, fragment);
			// finishUpdate提交事务
			mFragmentPagerAdapter.finishUpdate(frameLayout);
		}
	}

	private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case R.id.main_tab_home:// 首页标签
				if (homeFragment == null) {
					homeFragment = new HomeFragment();
				}
				currentFragment = homeFragment;
				return homeFragment;

			case R.id.main_tab_msg:// 消息标签

				if (msgFragment == null) {
					msgFragment = new MsgFragment();
				}
				return msgFragment;

			case R.id.main_tab_topic:// 话题标签

				if (plazaFragment == null) {
					plazaFragment = new TopicFragment();
				}
				return plazaFragment;
			case R.id.main_tab_mine:// 我的标签
				if (mineFragment == null) {
					mineFragment = new MineFragment();
				}
				return mineFragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_tab_home:// 导航首页标签
			//SharedPreferencesUtil.putSharePre(getApplicationContext(), "userInfo", "pusherId","");
			switchContent(currentFragment, homeFragment);
			break;
		case R.id.main_tab_msg:// 导航消息标签
			//SharedPreferencesUtil.putSharePre(getApplicationContext(), "userInfo", "pusherId","");
			switchContent(currentFragment, msgFragment);
			break;
		case R.id.main_tab_publish:// 导航发布标签
			//SharedPreferencesUtil.putSharePre(getApplicationContext(), "userInfo", "pusherId","");
			if(ValueUtil.isStrNotEmpty(userId)){
				startActivity(UploadActivity.class);
			}else{
				startActivity(LoginActivity.class);
			}

			break;
		case R.id.main_tab_topic:// 导航话题标签
			//SharedPreferencesUtil.putSharePre(getApplicationContext(), "userInfo", "pusherId","");
			switchContent(currentFragment, plazaFragment);
			break;
		case R.id.main_tab_mine:// 导航我的标签
			switchContent(currentFragment, mineFragment);
			break;
		default:
			break;
		}
	}

	private void titlePopupWindow() {
		// TODO Auto-generated method stub
		int[] location = new int[2];
		parentView.getLocationOnScreen(location);
	}

	/**
	 * fragment的切换
	 */
	public void switchContent(Fragment from, Fragment to) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			transaction.hide(from).add(R.id.fl_fragmentContent, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
		}
		currentFragment = to;
	}

	/**
	 * 显示指定的fragment
	 */
	public void showContent(Fragment fragment) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.show(fragment).commit();
	}

	private boolean canClick = true;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!canClick) {
				return true;
			}
			showExitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	AlertDialog.Builder dialog;

	private void showExitDialog() {
		dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提示");
		dialog.setMessage("您确定要退出吗？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// stopService(intent);
				//saveOldMsgs();
				//DbSaveOldMsg.getInstance().close();
				//CloseAll.closeAll();
				AppManager.getAppManager().AppExit(getContext(), false);
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void saveOldMsgs() {
		ArrayList<PrivateChatBean> listOfUsers = (ArrayList<PrivateChatBean>) FriendListInfo.getFriendListInfo().getFriendList();
		SparseArray<ArrayList<ChatEntity>> mapOfEntity = UnsavedChatMsg.getInstance().getMapFriendsEntity();
		SparseArray<ArrayList<Boolean>> mapOfIsSelf = UnsavedChatMsg.getInstance().getMapFriendsSelf();

		DbSaveOldMsg.onInit(getApplicationContext());
		DbSaveOldMsg.getInstance();

		int myId = Integer.parseInt(ConnectedApp.getInstance().getUserInfo().getId());
		for (PrivateChatBean uu0 : listOfUsers) {
			int id = Integer.parseInt(uu0.getId());
			ArrayList<ChatEntity> listOfEntity = mapOfEntity.get(id);
			ArrayList<Boolean> listOfIsSelf = mapOfIsSelf.get(id);
			int size = listOfEntity.size();
			for (int i = 0; i < size; i++) {
				ChatEntity ent0 = listOfEntity.get(i);
				boolean isSelf = listOfIsSelf.get(i);
				DbSaveOldMsg.getInstance().saveMsg(myId, id, isSelf, ent0);
			}
		}
	}
}
