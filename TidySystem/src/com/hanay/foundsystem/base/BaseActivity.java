package com.hanay.foundsystem.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.hanay.foundsystem.app.AppManager;
import com.hanay.foundsystem.util.LogUtil;
import com.hanay.foundsystem.R;

/**
 * @author   李海红
 * @version 创建时间：2014-12-13
 * @description Activity的基类,对Activity类进行扩展
 */

public abstract class BaseActivity extends Activity {
	/**
	 * LOG打印标签
	 */
	private static final String TAG = BaseActivity.class.getSimpleName();

	/**
	 * 全局的Context
	 */
	protected Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		int layoutId = getLayoutId();
		if (layoutId != 0) {
			setContentView(layoutId);
			// 设置窗口背景
			getWindow().setBackgroundDrawableResource(R.color.white);
		}

		mContext = this.getApplicationContext();
		// ((BaseApplication) this.getApplication()).addActivity(this);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 向用户展示信息前的准备工作在这个方法里处理
		preliminary();
	}

	/**
	 * 向用户展示信息前的准备工作在这个方法里处理
	 */
	protected void preliminary() {
		// 初始化组件
		setupViews();

		// 初始化数据
		initialized();
	}

	/**
	 * 获取全局的Context
	 */
	public Context getContext() {
		return mContext;
	}

	/**
	 * 布局文件ID
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化组件
	 */
	protected abstract void setupViews();

	/**
	 * 初始化数据
	 */
	protected abstract void initialized();

	/**
	 * Debug输出Log信息
	 * 
	 * @param tag
	 * @param msg
	 */
	protected void debugLog(String msg) {
		LogUtil.d(TAG, msg);
	}

	/**
	 * Error输出Log信息
	 * 
	 * @param msg
	 */
	protected void errorLog(String msg) {
		LogUtil.e(TAG, msg);
	}

	/**
	 * Info输出Log信息
	 * 
	 * @param msg
	 */
	protected void showLog(String msg) {
		LogUtil.i(TAG, msg);
	}

	/**
	 * 长时间显示Toast提示(来自String)
	 * 
	 * @param message
	 */
	protected void showToast(String message) {
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast提示(来自res)
	 * 
	 * @param resId
	 */
	protected void showToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_LONG).show();
	}

	/**
	 * 短暂显示Toast提示(来自res)
	 * 
	 * @param resId
	 */
	protected void showShortToast(int resId) {
		Toast.makeText(mContext, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短暂显示Toast提示(来自String)
	 * 
	 * @param text
	 */
	protected void showShortToast(String text) {
		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 通过Class跳转界面
	 **/
	public void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * 含有Bundle通过Class跳转界面
	 **/
	public void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 通过Action跳转界面
	 **/
	public void startActivity(String action) {
		startActivity(action, null);
	}

	/**
	 * 含有Bundle通过Action跳转界面
	 **/
	public void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	/**
	 * 含有Bundle通过Class打开编辑界面
	 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
		// overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
		 * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
		 */
		StatService.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	/**
	 * 带有右进右出动画的退出
	 */
	@Override
	public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
	}

	/**
	 * 默认退出
	 */
	public void defaultFinish() {
		super.finish();
	}

}
