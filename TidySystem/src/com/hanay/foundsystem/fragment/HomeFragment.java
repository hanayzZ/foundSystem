package com.hanay.foundsystem.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.activity.StatusesInfoActivity;
import com.hanay.foundsystem.adapter.PageViewAdapter;
import com.hanay.foundsystem.adapter.StatusAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseFragment;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * @author 李海红
 * @version 创建时间：2014-2-8
 * @description 导航标签(空巷)
 */

public class HomeFragment extends BaseFragment implements OnClickListener {

	private RadioGroup rg_supervise;
	/** 空巷、拾忆*/
	private RadioButton rb_viewPage,rb_seek;

	/** RadioButton底部蓝线 */
	private TextView main_navi_show, tv_noUpdate;
	private int width;

	/**空巷*/
	
	/**ViewPager实现图片的切换*/
	private ViewPager mViewPager;
	/**空巷广告图图片的的适配*/
	private PageViewAdapter mAdapter;
	/**空巷所有广告图和介绍*/
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>( ); 
	/** 当前页卡编号*/
	private int currIndex = 0;
	
	/** 拾忆 */
	private StatusBean statusbean;
	private List<StatusBean> seeklists=new ArrayList<StatusBean>();
	private StatusAdapter seekAdapter;
	private PullToRefreshListView lv_supervise_seek;

	/** 加载等待 */
	private ProgressBar progressBar;

	/**list显示所有用户发布的状态、 显示数量 不传则默认为3 */
	private String limit,memoryId;
	/** * page 当前页码*/
	private int page = 0;

	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_home;
	}
	@Override
	protected void setupViews(View view) {
		/** 获取屏幕宽度 */
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels / 2;
		rg_supervise = (RadioGroup) view.findViewById(R.id.rg_supervise);
		/** 底部选中的蓝线 */
		main_navi_show = (TextView) view.findViewById(R.id.main_navi_show);

		/**空巷*/
		rb_viewPage = (RadioButton) view.findViewById(R.id.rb_viewPage);
	
		/**拾忆*/
		rb_seek = (RadioButton) view.findViewById(R.id.rb_seek);
		lv_supervise_seek = (PullToRefreshListView) view.findViewById(R.id.lv_supervise_seek);

		/** 无活动 */
		tv_noUpdate = (TextView) view.findViewById(R.id.tv_noUpdate);
		tv_noUpdate.setOnClickListener(this);
		/** 进度条 */
		progressBar = (ProgressBar) view.findViewById(R.id.pb_supervise);
		InitViewPager(view);
	}

	/** 设置选中下划线 */
	private void setNaviShow(View view, int index) {
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
		params.leftMargin = index * width;
		params.width = width;
		view.setLayoutParams(params);
	}

	@Override
	protected void initialized() {
	
		rg_supervise.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_viewPage:
					rb_viewPage.setTextSize(18);
					rb_seek.setTextSize(15);
					tv_noUpdate.setVisibility(View.GONE);
					mViewPager.setVisibility(View.VISIBLE);
					lv_supervise_seek.setVisibility(View.GONE);
					setNaviShow(main_navi_show, 0);
					InitViewPager(view);
					break;
				case R.id.rb_seek:
					rb_seek.setTextSize(18);
					rb_viewPage.setTextSize(15);
					tv_noUpdate.setVisibility(View.GONE);
					lv_supervise_seek.setVisibility(View.VISIBLE);
					mViewPager.setVisibility(View.GONE);
					setNaviShow(main_navi_show, 1);
					if (ValueUtil.isListEmpty(seeklists)) {
						GetMemory("list","0",page);
					} else {
						seekAdapter = new StatusAdapter(getActivity(), seeklists);
						lv_supervise_seek.setAdapter(seekAdapter);
					}
					break;

				}
			}
		});

		lv_supervise_seek.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lv_supervise_seek.isHeaderShown()) {// 下拉刷新
					page= 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetMemory("list","0",page);
						}
					}, 1000);
				} else if (lv_supervise_seek.isFooterShown()) {// 上拉加载
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetMemory("list","0",page);
						}
					}, 1000);
				}
			}
		});

		lv_supervise_seek.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final StatusBean event = (StatusBean) parent.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString("memoryId",event.getMemoryId());
				startActivity(StatusesInfoActivity.class, bundle);
			}
		});
		rb_viewPage.setChecked(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_noUpdate:
			tv_noUpdate.setVisibility(View.GONE);
			if (rb_viewPage.isChecked()) {
				InitViewPager(view);
			} else if (rb_seek.isChecked()) {
				progressBar.setVisibility(View.VISIBLE);
				GetMemory("list","0",page);
			} 
			break;

		default:
			break;
		}
	}
	/**
	 *  首页
	 * 获取所有的记忆列表数据  
	 * @param string $type 'list' or '123' list为某个topic下或者某个用户的记忆列表，'数字'为某个具体记忆的ID
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 */
	private void GetMemory(String type,String topicId,final int pagetype) {

		Api.GetTopicMemory(type,topicId,pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					if (pagetype == 0) {// 第一页
						progressBar.setVisibility(View.GONE);
						// 活动图片
						statusbean = StatusBean.parse(result.getMsg());
						seeklists = statusbean.getList();
						if (ValueUtil.isListNotEmpty(seeklists)) {
							seekAdapter = new StatusAdapter(getActivity(),seeklists);
							lv_supervise_seek.setAdapter(seekAdapter);
							page++;
						} else {
						}
					} else {
						// 多页内容
						statusbean = StatusBean.parse(result.getMsg());
						seeklists.addAll(statusbean.getList());
						seekAdapter.notifyDataSetChanged();
						page++;
					}

				} else {
					lv_supervise_seek.setMode(Mode.PULL_FROM_START);
				}
				lv_supervise_seek.onRefreshComplete();
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				lv_supervise_seek.onRefreshComplete();
				ToastUtil.ToastMessage(getContext(), error);
			}

			@Override
			public void loading() {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
	}

	/**
	 * 初始化Fragment
	 */
	private void InitViewPager(View parentView){
	
		mViewPager =(ViewPager) view.findViewById(R.id.pager); 	/**空巷的滑动页*/
		
		fragments.add(new PageOneFragment()); 
		fragments.add(new PageTwoFragment()); 
		fragments.add(new PageThreeFragment());
		mAdapter = new PageViewAdapter(getChildFragmentManager() , fragments);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currIndex = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}


}