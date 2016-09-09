package com.hanay.foundsystem.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;


/**
 * @author 李海红
 * @version 创建时间：2014-12-23
 * @description 修改密码
 */

public class ChangePasswordActivity extends BaseActivity implements OnClickListener {
	private String userId;
	private Dialog waitDialog;

	private EditText et_oldPassword, et_newPassword, et_reEnterNewPassword;
	private Button btn_determine;
	private String oldPassword, newPassword, reEnterNewPassword;
	/**
	 * 提示框
	 */
	private Dialog determineDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_changepassword;
	}

	@Override
	protected void setupViews() {

		et_oldPassword = (EditText) findViewById(R.id.et_oldPassword);
		et_newPassword = (EditText) findViewById(R.id.et_newPassword);
		et_reEnterNewPassword = (EditText) findViewById(R.id.et_reEnterNewPassword);
		btn_determine = (Button) findViewById(R.id.btn_determine);
		btn_determine.setOnClickListener(this);
	}

	@Override
	protected void initialized() {
		userId = SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "userid");
		waitDialog = DialogUtil.waitDialog(ChangePasswordActivity.this, "正在提交...");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commonTitleBar_tv_back:// 返回
			finish();
			break;

		case R.id.btn_determine:// 确定
			oldPassword = et_oldPassword.getText().toString();
			newPassword = et_newPassword.getText().toString();
			reEnterNewPassword = et_reEnterNewPassword.getText().toString();
			if (ValueUtil.isStrNotEmpty(oldPassword) && ValueUtil.isStrNotEmpty(newPassword) && ValueUtil.isStrNotEmpty(reEnterNewPassword)) {
				if (compare(oldPassword) && compare(newPassword) && compare(reEnterNewPassword)) {
					if (newPassword.equals(reEnterNewPassword)) {
						NewPassword(userId,oldPassword, newPassword, reEnterNewPassword);
					} else {
						ToastUtil.ToastMessage(ChangePasswordActivity.this, "两次输入的密码不一致");
					}
				} else {
					ToastUtil.ToastMessage(ChangePasswordActivity.this, "请输入正确的密码");
				}
			} else {
				ToastUtil.ToastMessage(ChangePasswordActivity.this, "密码不能为空");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 发送修改密码的请求
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param rePassword
	 */
	private void NewPassword(String userId,String oldPassword, String newPassword, String rePassword) {
		Api.NewPassword(userId, oldPassword, newPassword, rePassword, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				waitDialog.dismiss();
				if (result.isValidate()) {
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "login", 0);
					SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "password", "");
					showDialog();
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				waitDialog.show();
			}

			@Override
			public void fail(String error) {
				waitDialog.dismiss();
				ToastUtil.ToastMessage(getContext(), error);
			}
		});
	}

	/**
	 * 修改密码成功弹出提示框重新登录
	 */
	private void showDialog() {
		View view = LayoutInflater.from(ChangePasswordActivity.this).inflate(R.layout.customdialog_determine, null);
		determineDialog = DialogUtil.alertDialog(view, ChangePasswordActivity.this);
		determineDialog.setCanceledOnTouchOutside(false);
		determineDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				} else {
					return false;
				}
			}
		});
		Button confirm = (Button) view.findViewById(R.id.btn_customDialog_determine);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	/**
	 * 判断字符串的长度是否为6-15位
	 */
	private boolean compare(String string) {
		if (string.length() > 5 && string.length() < 16) {
			return true;
		} else {
			return false;
		}
	}

}
