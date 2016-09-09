package com.hanay.foundsystem.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
/**
 * @author 李海红
 * @version 创建时间：2015-5-7
 * @description 启动图
 */
public class Activity_QD extends Activity {
	String token;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qd);

		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				intent= new Intent();
				token=SharedPreferencesUtil.getSharePreStr(getBaseContext(), "Token", "token");
				if(token==null){
					intent.setClass(Activity_QD.this, Activity_HY.class);
					Activity_QD.this.startActivity(intent);
					Activity_QD.this.finish();
				}else {
					intent.setClass(Activity_QD.this, MainActivity.class);
					Activity_QD.this.startActivity(intent);
					Activity_QD.this.finish();
				}

			}

		}, 2300);
	}

}
