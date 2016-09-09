package com.hanay.foundsystem.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseFragment;
import com.hanay.foundsystem.bean.AlleyBean;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;

/**
 * @author 李海红
 * @version 创建时间：2015-5-11
 * @description  广告滑动页的第二个页面
 */
public class PageTwoFragment extends BaseFragment implements OnClickListener{


	private ImageView pagerview;
	private TextView content;

	/**空巷广告图*/
	private AlleyBean alleyBean;
	private List<AlleyBean> alleylists=new ArrayList<AlleyBean>();
	/** 加载等待 */
	private ProgressBar progressBar;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_pagone;
	}

	@Override
	protected void setupViews(View view) {
		pagerview = (ImageView) view.findViewById(R.id.pagerview);
		content= (TextView) view.findViewById(R.id.content);
		/** 进度条 */
		progressBar = (ProgressBar) view.findViewById(R.id.pb_supervise);
	}

	@Override
	protected void initialized() {
		if (ValueUtil.isListEmpty(alleylists)) {
			GetAlley();
		} else{
			content.setText("      "+alleylists.get(2).getContent());
			ImageCacheUitl.displayNormalImageView(pagerview,alleylists.get(2).getImg(), R.id.pagerview);
		}
	
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
	/**
	 * 向服务器请求空巷广告图
	 */
	private void GetAlley( ) {

		Api.GetAlley(new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					progressBar.setVisibility(View.GONE);
					alleyBean = AlleyBean.parse(result.getMsg());
					alleylists = alleyBean.getAlleylists();
					content.setText(alleylists.get(2).getContent());
					ImageCacheUitl.displayNormalImageView(pagerview,alleylists.get(2).getImg(), R.id.pagerview);
				}
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				ToastUtil.ToastMessage(getContext(), error);
			}

			@Override
			public void loading() {

			}
		});

	}
}
