package com.hanay.foundsystem.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.adapter.StatusAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.bean.TopicBean;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * @author 李海红
 * @version 创建时间：2014-3-3
 */

public class TopicStatusesActivity extends BaseActivity implements OnClickListener{

	/** 返回*/
	private TextView commonTitleBar_tv_back;
	private TextView tv_topicname;//主题名称
	/** 无数据*/
	private TextView tv_tips;

	private TopicBean topicbean;
	private String title;
	/** 按话题分类发布*/
	private StatusBean data;
	private List<StatusBean> statuslist = new ArrayList<StatusBean>();;
	private StatusAdapter groupInfoAdapter;
	private PullToRefreshListView lv_topic_status;

	/**发布*/
	private View rl_publish;
	//private Button btn_photo,btn_send;
	private EditText et_sendmessage;

	/**'list' or '123' list为某个topic下或者某个用户的记忆列表 */
	private String topicId,memoryId;
	/** * page 当前页码,count 条目数 */
	private int page = 0;
	/** 等待框 */
	private Dialog waitDialog;
	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_topic_status;
	}

	@Override
	protected void setupViews() {
		commonTitleBar_tv_back = (TextView) findViewById(R.id.commonTitleBar_tv_back);
		tv_topicname=(TextView) findViewById(R.id.tv_topicname);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		lv_topic_status = (PullToRefreshListView) findViewById(R.id.lv_topic_status);
		rl_publish=findViewById(R.id.rl_publish);
		//btn_photo=(Button) findViewById(R.id.btn_photo);
		//btn_photo.setOnClickListener(this);
		et_sendmessage=(EditText) findViewById(R.id.et_sendmessage);
		et_sendmessage.setOnClickListener(this);
		//btn_send=(Button) findViewById(R.id.btn_send);
		//btn_send.setOnClickListener(this);
	}

	@Override
	protected void initialized() {
		waitDialog = DialogUtil.waitDialog(TopicStatusesActivity.this, "加载中...");
		Bundle bundle = getIntent().getExtras();
		topicId = bundle.getString("topicId");//话题界面传来的topicId
		title = bundle.getString("title");//话题界面传来的title
	
		if(ValueUtil.isStrNotEmpty(topicId)&&ValueUtil.isStrNotEmpty(title)){
			rl_publish.setVisibility(View.VISIBLE );
			commonTitleBar_tv_back.setVisibility(View.VISIBLE);
			commonTitleBar_tv_back.setOnClickListener(this);
			tv_topicname.setText(title);
			getTopic(topicId,"0", page);//让浏览次数加1
			waitDialog.show();
			GetTopicMemory("list",topicId,page);
			//tv_topicname.setText(status.getTitle());
		}
	
		lv_topic_status.setOnRefreshListener(new OnRefreshListener<ListView>() {

			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if (lv_topic_status.isHeaderShown()) {// 下拉刷新
					page = 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetTopicMemory("list",topicId,page);
						}
					}, 1000);
				} else if (lv_topic_status.isFooterShown()) {// 上拉加载
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetTopicMemory("list",topicId,page);
						}
					}, 1000);
				}
			}
		});

		lv_topic_status.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final StatusBean bean = (StatusBean) parent.getItemAtPosition(position);
				Bundle bundle = new Bundle();
				bundle.putString("memoryId",bean.getMemoryId());
				//bundle.putSerializable("statuslist", bean);
				startActivity(StatusesInfoActivity.class, bundle);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commonTitleBar_tv_back:
			finish();
			break;
		case R.id.et_sendmessage:
			Bundle bundle = new Bundle();
			bundle.putString("topicId",topicId);
			startActivity(UploadActivity.class,bundle);
			break;
		default:
			break;
		}
	}
	/**
	 * 浏览次数加一
	 */
	private void getTopic(final String type, String listType, final int pagetype) {
		Api.getTopic(type, listType, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {

				if (result.isValidate()) {
					topicbean = TopicBean.parse(result.getMsg());
					title=topicbean.getTitle();
				} else {
				}
			}

			@Override
			public void loading() {
				
			}

			@Override
			public void fail(String error) {
				ToastUtil.ToastMessage(getContext(), error);
			}
		});
	}
	/**
	 *  话题详情
	 * 获取某个话题下的记忆列表数据  
	 * @param string $type 'list' or '123' list为某个topic下或者某个用户的记忆列表，'数字'为某个具体记忆的ID
	 * @param string $topicId 可选参数，当type为list时传
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 * @param apiCallBack
	 */
	private void GetTopicMemory(String type,String topicId,final int page) {

		Api.GetTopicMemory(type,topicId,page, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					waitDialog.dismiss();
					tv_tips.setVisibility(View.GONE);
					if (page == 0) {// 第一页
						// 活动图片
						data = StatusBean.parse(result.getMsg());
						statuslist = data.getList();
						if (ValueUtil.isListNotEmpty(statuslist)) {
							groupInfoAdapter = new StatusAdapter(TopicStatusesActivity.this,statuslist);
							lv_topic_status.setAdapter(groupInfoAdapter);
						} else {
						}
					} else {
						// 多页内容
						data = StatusBean.parse(result.getMsg());
						statuslist.addAll(data.getList());
						groupInfoAdapter.notifyDataSetChanged();
					}
					lv_topic_status.onRefreshComplete();
				} else {
					tv_tips.setVisibility(View.VISIBLE);
					waitDialog.dismiss();
					lv_topic_status.setMode(Mode.PULL_FROM_START);
				}
				
			}

			@Override
			public void fail(String error) {
				waitDialog.dismiss();
				lv_topic_status.onRefreshComplete();
				tv_tips.setVisibility(View.VISIBLE);
				ToastUtil.ToastMessage(getContext(), error);
			}

			@Override
			public void loading() {
				waitDialog.show();
			}
		});
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		StatusBean data = (StatusBean) intent.getSerializableExtra("data");
		if(data!=null){
			statuslist.add(0, data);	
		}
		groupInfoAdapter = new StatusAdapter(TopicStatusesActivity.this,statuslist);
		lv_topic_status.setAdapter(groupInfoAdapter);
	}
}
