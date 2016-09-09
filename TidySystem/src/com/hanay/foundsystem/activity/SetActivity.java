package com.hanay.foundsystem.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.chat.CloseAll;
import com.hanay.foundsystem.chat.util.DbSaveOldMsg;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.SharedPreferencesUtil;

/**
 * @author 李海红
 * @version 创建时间：2015-3-27
 * @description 设置
 */

public class SetActivity extends BaseActivity implements OnClickListener{

	/** 返回*/
	private TextView commonTitleBar_tv_back;

	/**修改密码、注销登陆*/
	private LinearLayout ll_changePWD, ll_logOut;

	/*** 注销登录提示框*/
	private Dialog logOutDialog;
	@Override
	protected int getLayoutId() {
		return R.layout.activity_set;
	}

	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		commonTitleBar_tv_back = (TextView) findViewById(R.id.commonTitleBar_tv_back);
		commonTitleBar_tv_back.setOnClickListener(this);
		//修改密码、注销登陆
		ll_changePWD = (LinearLayout)findViewById(R.id.ll_mineChangePassword);
		ll_changePWD.setOnClickListener(this);
		ll_logOut = (LinearLayout)findViewById(R.id.ll_mineLogOut);
		ll_logOut.setOnClickListener(this);
	}

	@Override
	protected void initialized() {
		// TODO Auto-generated method stub

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commonTitleBar_tv_back:// 返回键
			finish();
			break;
		case R.id.ll_mineChangePassword:// 修改密码
			startActivity(ChangePasswordActivity.class);
			break;
		case R.id.ll_mineLogOut:// 注销登录
			showLogOutDialog();
			break;
		default:
			break;
		}
	}
	/**
	 * 显示注销登录Dialog
	 */
	private void showLogOutDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.customdialog_logout, null);
		logOutDialog = DialogUtil.alertDialog(view,this);

		Button cancle = (Button) view.findViewById(R.id.btn_dialog_logout_cancle);
		Button confirm = (Button) view.findViewById(R.id.btn_dialog_logout_confirm);

		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logOutDialog.dismiss();
			}
		});

		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logOutDialog.dismiss();
		/*		DbSaveOldMsg.getInstance().close();
				CloseAll.closeAll();*/
				SharedPreferencesUtil.putSharePre(getContext(), "userInfo", "userId","");
				Intent intent = new Intent(SetActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}
