package com.hanay.foundsystem.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import com.hanay.foundsystem.activity.SearchActivity;
import com.hanay.foundsystem.activity.TopicStatusesActivity;
import com.hanay.foundsystem.adapter.TopicAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseFragment;
import com.hanay.foundsystem.base.CustomFontEditText;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.TopicBean;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author 李海红
 * @version 创建时间：2015-2-26
 * @description  导航标签(话题)
 */

public class TopicFragment extends BaseFragment implements OnClickListener{

	/** 搜索 */
	private CustomFontEditText search;

	/** 最新话题、最热话题 */
	private RadioGroup rg_supervise;
	private RadioButton rb_new,rb_hit;

	/** RadioButton底部蓝线 */
	private TextView main_navi_show;
	private int width;
	/** 话题Dialog*/
	//private Dialog topicDialog; 

	/** page 当前页码,count 条目数*/
	private int page;
	private TopicBean topicbean;

	/**type'数字'为某个具体记忆的ID */
	private String type;

	/** 服务器获取最新话题的列表 */
	private List<TopicBean> newlists = new ArrayList<TopicBean>();
	private TopicAdapter newAdapter;
	private PullToRefreshListView lv_newTopic;

	/** 服务器获取最热话题的列表数据 */
	private List<TopicBean> hitlists = new ArrayList<TopicBean>();
	private TopicAdapter hitAdapter;
	private PullToRefreshListView lv_hitTopic;

	/** 加载等待 */
	private ProgressBar progressBar;
	/** * 暂无最新话题*/
	private TextView noNewTopic;
	/** * 暂无最热话题*/
	private TextView noHitTopic;

	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_topic;
	}

	@Override
	protected void setupViews(View view) {
		/** 搜索框 */
		search = (CustomFontEditText) view.findViewById(R.id.search);
		search.setOnClickListener(this);
		rg_supervise = (RadioGroup) view.findViewById(R.id.rg_supervise);
		/**最新话题*/
		rb_new = (RadioButton) view.findViewById(R.id.rb_new);
		lv_newTopic = (PullToRefreshListView)view.findViewById(R.id.lv_newTopic);
		/**最热话题*/
		rb_hit = (RadioButton) view.findViewById(R.id.rb_hit);
		lv_hitTopic = (PullToRefreshListView)view.findViewById(R.id.lv_hitTopic);
		/** 获取屏幕宽度 */
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels / 2;
		/** 底部选中的蓝线 */
		main_navi_show = (TextView) view.findViewById(R.id.main_navi_show);
		/** 进度条 */
		progressBar = (ProgressBar) view.findViewById(R.id.pb_topic);

		noNewTopic = (TextView)view.findViewById(R.id.noNewTopic);
		noHitTopic= (TextView)view.findViewById(R.id.noHitTopic);

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
				case R.id.rb_new:
					lv_newTopic.setVisibility(View.VISIBLE);
					lv_hitTopic.setVisibility(View.GONE);
					rb_new.setTextSize(18);
					rb_hit.setTextSize(15);
					setNaviShow(main_navi_show, 0);
					if (ValueUtil.isListEmpty(newlists)) {
						progressBar.setVisibility(View.VISIBLE);
						getTopic("list","new", page);
					} else {
						newAdapter = new TopicAdapter(getActivity(), newlists);
						lv_newTopic.setAdapter(newAdapter);
					}
					break;
				case R.id.rb_hit:
					lv_hitTopic.setVisibility(View.VISIBLE);
					lv_newTopic.setVisibility(View.GONE);
					rb_hit.setTextSize(18);
					rb_new.setTextSize(15);
					setNaviShow(main_navi_show, 1);
					if (ValueUtil.isListEmpty(hitlists)) {
						progressBar.setVisibility(View.VISIBLE);
						getTopic("list","hot", page);
					} else {
						hitAdapter = new TopicAdapter(getActivity(), hitlists);
						lv_hitTopic.setAdapter(hitAdapter);
					}
					break;
				default:
					break;
				}	
			}
		});
		lv_newTopic.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lv_newTopic.isHeaderShown()) {// 下拉刷新
					page = 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							getTopic("list","new", page);
						}
					}, 1000);
				} else if (lv_newTopic.isFooterShown()) {// 上拉加载
					page++;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							
							getTopic("list","new", page);
						}
					}, 1000);
				}
			}
		});

		lv_hitTopic.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lv_hitTopic.isHeaderShown()) {// 下拉刷新
					page = 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							getTopic("list","hot", page);
						}
					}, 1000);
				} else if (lv_hitTopic.isFooterShown()) {// 上拉加载
					page++;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							
							getTopic("list","hot", page);
						}
					}, 1000);
				}
			}
		});

		lv_newTopic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final TopicBean event = (TopicBean) parent.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString("topicId",event.getTopicId());
				bundle.putString("title",event.getTitle());
				startActivity(TopicStatusesActivity.class, bundle);
			}
		});


		lv_hitTopic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final TopicBean event = (TopicBean) parent.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString("topicId",event.getTopicId());
				bundle.putString("title",event.getTitle());
				startActivity(TopicStatusesActivity.class, bundle);
			}
		});
		rb_new.setChecked(true);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			startActivity(SearchActivity.class);
			break;
		default:
			break;
		}
	}
	/**
	 * 创建话题Dialog
	 */
	@SuppressLint("InflateParams")
	/*	private void topicDialog( ) {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_choosetitle, null);
		topicDialog = DialogUtil.alertDialog(view, getContext());
		TextView cancel = (TextView) view.findViewById(R.id.tv_dialog__cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				topicDialog.dismiss();
			}
		});

	}
	 */
	/**
	 * 获取最新、最热话题、我的列表
	 * @param string $type 'list' or '123' 
	 * ----传'list'获取Topic列表，传'数字'则获取ID为这一数字的某个具体topic
	 *  @param string $listType 'new' 'hot' '43'  最新 最热 我的
	 *   ----可选参数，当type为list时传值，传'new'获取最新列表，传'hot'获取热门列表，
	 *   传'数字'则获取用户userId为这一数字的某个具体用户所发布的topic
	 *    @param string $page 列表分页
	 *    ----可选参数，当当type为list时传值，从0开始计数
	 * @param apiCallBack
	 */
	private void getTopic(String type, final String listType, final int pagetype) {
		Api.getTopic(type, listType, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if(listType.equals("new")){
					if(result.isValidate()){
						progressBar.setVisibility(View.GONE);
						if (pagetype == 0) {// 第一页
							// 活动图片
							topicbean = TopicBean.parse(result.getMsg());
							newlists = topicbean.getLists();
							if (ValueUtil.isListNotEmpty(newlists)) {
								newAdapter = new TopicAdapter(getActivity(),newlists);
								lv_newTopic.setAdapter(newAdapter);
								//page++;
							} else {
								//lv_newTopic.setEmptyView(tv_noData);
							}
						} else {
							// 多页内容
							topicbean = TopicBean.parse(result.getMsg());
							newlists.addAll(topicbean.getLists());
							newAdapter.notifyDataSetChanged();
							//page++;
						}
						lv_newTopic.onRefreshComplete();
					}else{
						noNewTopic.setVisibility(View.VISIBLE);
						lv_newTopic.setVisibility(View.GONE);
					}
				}else{
					if(result.isValidate()){
						progressBar.setVisibility(View.GONE);
						if (pagetype == 0) {// 第一页
							// 活动图片
							topicbean = TopicBean.parse(result.getMsg());
							hitlists = topicbean.getLists();
							if (ValueUtil.isListNotEmpty(hitlists)) {
								hitAdapter = new TopicAdapter(getActivity(),hitlists);
								lv_hitTopic.setAdapter(hitAdapter);
								//page++;
							} else {
								// lv_supervise.setEmptyView(tv_noData);
							}
						} else {
							// 多页内容
							topicbean = TopicBean.parse(result.getMsg());
							hitlists.addAll(topicbean.getLists());
							hitAdapter.notifyDataSetChanged();
							//page++;
						}
						lv_hitTopic.onRefreshComplete();
					}else{
						noHitTopic.setVisibility(View.VISIBLE);
						lv_hitTopic.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void loading() {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				lv_newTopic.onRefreshComplete();
				lv_hitTopic.onRefreshComplete();
				if(rb_new.isChecked()){	
					if(ValueUtil.isListNotEmpty(newlists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noNewTopic.setVisibility(View.VISIBLE);
						lv_hitTopic.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}else if(rb_hit.isChecked()){	
					if(ValueUtil.isListNotEmpty(hitlists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noHitTopic.setVisibility(View.VISIBLE);
						lv_newTopic.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}

			}
		});
	}
}
