package com.hanay.foundsystem.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.adapter.TopicAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.base.CustomFontEditText;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.TopicBean;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SearchActivity extends BaseActivity implements OnClickListener, OnItemClickListener{
	/** 返回 */
	//private TextView commonTitleBar_tv_back;
	
	/** 无搜索结果 */
	private TextView tv_no;
	/** 搜索框 */
	private CustomFontEditText search;
	/** 查询 */
	private TextView query;
	/** 服务器获取最新话题的列表 */
	private List<TopicBean> lists = new ArrayList<TopicBean>();
	private TopicBean topicbean;
	private TopicAdapter adapter;
	/** 服务器获取的活动listview */
	private PullToRefreshListView lv_topic;

	/** 等待框 */
	private Dialog waitDialog;

	/** 向服务器请求数据 */
	private String searchContent;
	private int page=0;
	private Handler handler = new Handler();
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_search;
	}

	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub

		/** 返回按钮 */
		//commonTitleBar_tv_back = (TextView) findViewById(R.id.commonTitleBar_tv_back);
		//commonTitleBar_tv_back.setVisibility(View.VISIBLE);
		//commonTitleBar_tv_back.setOnClickListener(this);

		/** 搜索框 */
		search = (CustomFontEditText) findViewById(R.id.search);

		/** 搜索按钮 */
		query = (TextView) findViewById(R.id.query);
		query.setOnClickListener(this);

		/** 显示搜索内容的listView */
		lv_topic =(PullToRefreshListView) findViewById(R.id.lv_topic);
		lv_topic.setOnItemClickListener(SearchActivity.this);

		/** 搜索后无结果 */
		tv_no = (TextView) findViewById(R.id.activity_search_supervise_no);
	}

	@Override
	protected void initialized() {
		// TODO Auto-generated method stub
		waitDialog = DialogUtil.waitDialog(SearchActivity.this, "搜索中...");
		waitDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		lv_topic.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lv_topic.isHeaderShown()) {// 下拉刷新
					page = 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							SearchTopic(searchContent, page);
						}
					}, 1000);
				} else if (lv_topic.isFooterShown()) {// 上拉加载
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							page++;
							SearchTopic(searchContent, page);
						}
					}, 1000);
				}
			}
		});
		lv_topic.setOnItemClickListener(this);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		final TopicBean event = (TopicBean) parent.getItemAtPosition(position);
		Bundle bundle = new Bundle();
		bundle.putString("topicId",event.getTopicId());
		bundle.putString("title",event.getTitle());
		startActivity(TopicStatusesActivity.class, bundle);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.query:
			searchContent=search.getText().toString();
			if(ValueUtil.isStrNotEmpty(searchContent)){
				waitDialog.show();
				SearchTopic(searchContent, page);
			}else{
				ToastUtil.ToastMessage(getContext(), "搜索内容不能为空!");
			}
			break;
		default:
			break;
		}
	}
	private void SearchTopic(String searchContent, final int pagetype) {
		// TODO Auto-generated method stub

		Api.SearchTopic(searchContent,pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					waitDialog.dismiss();
					if (pagetype == 0) {// 第一页
						// 活动图片
						topicbean = TopicBean.parse(result.getMsg());
						lists = topicbean.getLists();
						if (ValueUtil.isListNotEmpty(lists)) {
							adapter = new TopicAdapter(SearchActivity.this,lists);
							lv_topic.setAdapter(adapter);
							//page++;
						} else {
							//lv_newTopic.setEmptyView(tv_noData);
						}
					} else {
						// 多页内容
						topicbean = TopicBean.parse(result.getMsg());
						lists.addAll(topicbean.getLists());
						adapter.notifyDataSetChanged();
						//page++;
					}
					lv_topic.onRefreshComplete();

				} else {
					lv_topic.setVisibility(View.GONE);
				}
			}

			@Override
			public void loading() {
				waitDialog.show();
			}

			@Override
			public void fail(String error) {
				waitDialog.dismiss();
				tv_no.setVisibility(View.VISIBLE);
				lv_topic.onRefreshComplete();

			}
		});

	}

}
