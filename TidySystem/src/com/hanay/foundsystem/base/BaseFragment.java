package com.hanay.foundsystem.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

/**
 * @author  李海红
 * @version 创建时间：2014-12-18
 * @description Fragment的基类
 */

public abstract class BaseFragment extends Fragment {
	protected Context mContext;
	protected View view;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		int layoutId = getLayoutId();
		if (layoutId != 0) {
			view = inflater.inflate(layoutId, container, false);
		}

		mContext = getActivity();

		// 初始化组件
		setupViews(view);

		// 初始化数据
		initialized();

		return view;
	}

	/**
	 * 布局文件ID
	 */
	protected abstract int getLayoutId();

	/**
	 * 初始化组件
	 */
	protected abstract void setupViews(View view);

	/**
	 * 初始化数据
	 */
	protected abstract void initialized();

	/**
	 * 获取Context
	 */
	public Context getContext() {
		return mContext;
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
		intent.setClass(mContext, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
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
	}

	/**
	 * 含有Bundle通过Class打开编辑界面
	 **/
	public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(mContext, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}

	public void onResume() {
		super.onResume();
		/**
		 * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
		 */
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		/**
		 * Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
		 */
		StatService.onPause(this);
	}

}
