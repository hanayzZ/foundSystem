package com.hanay.foundsystem.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.app.AppManager;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.NetWorkUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.umeng.update.UmengUpdateAgent;


/**
 * @author 李海红
 * @version 创建时间：2015-1-17
 * @description 注册界面
 */

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private TextView tv_back;
	/** 用户名和密码 */
	private EditText et_userName,et_Phone, et_password,et_repassword;

	/** 注册 */
	private Button btn_register;

	/** 用户名和密码 */
	private String username, password, phone,repassword;

	/** 等待框 */
	private Dialog waitDialog;

/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setDeltaUpdate(true);// 增量更新
	}*/

	@Override
	protected int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	protected void setupViews() {
		tv_back=(TextView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		et_userName = (EditText) findViewById(R.id.et_userName);
		et_Phone=(EditText) findViewById(R.id.et_Phone);
		et_password = (EditText) findViewById(R.id.et_password);
		et_repassword=(EditText) findViewById(R.id.et_repassword);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
	}

	@Override
	protected void initialized() {

		waitDialog = DialogUtil.waitDialog(RegisterActivity.this, "注册中...");
		waitDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.btn_register:// 注册
			username = et_userName.getText().toString();
			phone=et_Phone.getText().toString();
			
			password = et_password.getText().toString();
			repassword=et_repassword.getText().toString();
			if (ValueUtil.isStrNotEmpty(username)){
				if(ValueUtil.isStrNotEmpty(phone)){
					if (isPhone(phone)) {
						if(ValueUtil.isStrNotEmpty(password)){
							if(password.length() > 5 && password.length() < 16){
								if(ValueUtil.isStrNotEmpty(repassword)){
									if (NetWorkUtil.isNetworkConnected(getContext())) {
										waitDialog.show();
										
										UserRegister(username, phone,password,repassword);
									} else {
										ToastUtil.ToastMessage(getContext(), "网络异常,请检查网络设置!");
									}
									
								}else{
									ToastUtil.ToastMessage(getContext(), "请重复输入密码");
								}
							}else{
								ToastUtil.ToastMessage(getContext(), "密码长度必须为6-15位");
							}
						}else{
							ToastUtil.ToastMessage(getContext(), "请输入密码");
						}
					}else{
						ToastUtil.ToastMessage(getContext(), "请输入正确的手机号");
					}
				}else{
					ToastUtil.ToastMessage(getContext(), "请输入手机号");
				}
			} else{
				ToastUtil.ToastMessage(getContext(), "请输入用户名");
			}
			break;
		default:
			break;
		}
}
//判断是否为手机号
	private boolean isPhone(String inputText) {
		Pattern p = Pattern.compile("^1(3|4|5|7|8|9)\\d{9}$");
		Matcher m = p.matcher(inputText);
		return m.matches();
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
	private void UserRegister(String username, String phone,String password,String repassword) {
		Api.UserRegister(username, phone,password,repassword, new ApiCallBack() {

			@Override
			public void loading() {
				waitDialog.show();
			}

			@Override
			public void succeed(Result result) {
				waitDialog.dismiss();
				if (result.isValidate()) {
					startActivity(LoginActivity.class);
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
				ToastUtil.ToastMessage(RegisterActivity.this, "再按一次退出程序");
			}
			currentTime = System.currentTimeMillis();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}
