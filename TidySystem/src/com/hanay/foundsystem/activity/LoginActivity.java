package com.hanay.foundsystem.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.app.AppManager;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.UserBean;
import com.hanay.foundsystem.chat.ConnectedApp;
import com.hanay.foundsystem.chat.chatServices.InitData;
import com.hanay.foundsystem.util.DESUtils;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.NetWorkUtil;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.Util;
import com.hanay.foundsystem.util.ValueUtil;
import com.umeng.update.UmengUpdateAgent;


/**
 * @author 李海红
 * @version 创建时间：2014-12-17
 * @description 登录界面
 */

public class LoginActivity extends BaseActivity implements OnClickListener {
	//private TextView tv_back;
	/** 用户名和密码 */
	private EditText et_userName, et_password;

	/** 是否记住密码 */
	private CheckBox cb_login;

	/** 登录 */
	private Button btn_login;

	/**用户bean*/
	private UserBean userBean;
	/** 用户名和密码 */
	private String username, password;

	/** 等待框 */
	private Dialog waitDialog;

	/** 判断是否记住密码:0代表不记住密码,1代表记住密码 */
	private int flag;
	private  PrivateChatBean mUserInfo;

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setDeltaUpdate(true);// 增量更新
	}
*/
	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	protected void setupViews() {
		Util.setupUI(findViewById(R.id.ll_loginActivity), LoginActivity.this);
		//tv_back=(TextView) findViewById(R.id.tv_back);
		//tv_back.setOnClickListener(this);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_password = (EditText) findViewById(R.id.et_password);
		cb_login = (CheckBox) findViewById(R.id.cb_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
	}

	@Override
	protected void initialized() {

		flag = SharedPreferencesUtil.getSharePreInt(getContext(), "userInfo", "isRE");
		waitDialog = DialogUtil.waitDialog(LoginActivity.this, "登录中...");
		waitDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});

		// 默认显示上次输入的用户名
		String username = SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "username");
		if (ValueUtil.isNotEmpty(username)) {
			et_userName.setText(username);
		}
		// 是否显示密码
		if (flag == 1) {
			cb_login.setChecked(true);
			try {
				et_password.setText(DESUtils.deCrypto(SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "password")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	/*	case R.id.tv_back:
			finish();
			break;*/
		case R.id.btn_login:// 登录
			username = et_userName.getText().toString();
			password = et_password.getText().toString();

			if (ValueUtil.isStrNotEmpty(username) && ValueUtil.isStrNotEmpty(password)) {
				SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "username", username);
				if (cb_login.isChecked()) {
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "isRE", 1);
					try {
						SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "password", DESUtils.enCrypto(password));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "isRE", 0);
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "password", "");
				}
				if (NetWorkUtil.isNetworkConnected(getContext())) {
					waitDialog.show();
					UserLogin(username, password);
				} else {
					ToastUtil.ToastMessage(getContext(), "网络异常,请检查网络设置!");
				}
			
			} else {
				ToastUtil.ToastMessage(getContext(), "用户名或密码不能为空");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 发送登录请求
	 * 
	 * @param token
	 * @param username
	 *            用户名
	 * @param userPwd
	 *            密码
	 */
	private void UserLogin(String username, String userPwd) {
		Api.UserLogin(username, userPwd, new ApiCallBack() {

			@Override
			public void loading() {
				waitDialog.show();
			}

			@Override
			public void succeed(Result result) {
				waitDialog.dismiss();
				if (result.isValidate()) {
					userBean = UserBean.parse(result.getMsg());
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "userId", userBean.getUserid());
					tryLogin();
					startActivity(MainActivity.class);
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void fail(String error) {
				waitDialog.dismiss();
				ToastUtil.ToastMessage(getContext(), error);
			}
		});
	}

	private long currentTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - currentTime < 2000) {
				AppManager.getAppManager().AppExit(getContext(), false);
			} else {
				ToastUtil.ToastMessage(LoginActivity.this, "再按一次退出程序");
			}
			currentTime = System.currentTimeMillis();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	public void tryLogin() {

	/*	CloseAll.closeAll();
		NetworkService.getInstance().onInit(this);
		NetworkService.getInstance().setupConnection();

		if (NetworkService.getInstance().getIsConnected()) {


			String message1 = "{\"method\":\"connection\",\"info\":{\"currentUserId\":\""
					+ SharedPreferencesUtil.getSharePreStr(LoginActivity.this,
							"userInfo", "userId") + "\"}}";
			NetworkService.getInstance().sendUpload(
					GlobalMsgTypes.msgHandShake, message1);
			Toast.makeText(LoginActivity.this,
					"sucess to connect to Server", Toast.LENGTH_LONG)
					.show();

		} else {
			NetworkService.getInstance().closeConnection();
			Toast.makeText(LoginActivity.this,
					"failed to connect to Server", Toast.LENGTH_LONG)
					.show();

			return;
		}*/
		//
		InitData initData = InitData.getInitData();
		initData.msg3Arrive(SharedPreferencesUtil.getSharePreStr(this,
				"userInfo", "userId"));

		ConnectedApp connected_app0 = ConnectedApp.getInstance();
		//		 connected_app0.setConnect(mNetCon);
		connected_app0.setUserInfo(initData.getUserInfo());
		connected_app0.clearListActivity();
		connected_app0.instantiateListActivity();

	}

}
