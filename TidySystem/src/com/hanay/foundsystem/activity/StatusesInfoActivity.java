package com.hanay.foundsystem.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.adapter.CommentAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.bean.ReplyBean;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.customView.ListViewForScrollView;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;


/**
 * @author 李海红
 * @version 创建时间：2014-3-3
 * @description 详情包括某个状态下的评论
 */

public class StatusesInfoActivity extends BaseActivity implements OnClickListener{

	/** 返回*/
	private TextView commonTitleBar_tv_back;
	//private TextView tv_topicname;//主题名称
	public ImageView iv_head;//用户头像
	public TextView tv_username;//用户名
	public TextView tv_createtime;//发布时间
	public TextView tv_describe;//描述
	public ImageView iv_status_image;//一张图片
	/**进行评论*/
	private EditText et_sendmessage;
	private Button btn_send;
	/** 详情*/
	private StatusBean status;
	/**获取 评论*/
	private ReplyBean commentBean;
	private List<ReplyBean> commentlist = new ArrayList<ReplyBean>();
	private CommentAdapter commentAdapter;
	private ListViewForScrollView lv_status_comment;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView scrollview;
	/** 等待框 */
	private Dialog waitDialog;
	/** 无数据*/
	private TextView tv_tips;

	/** 根据memoryId获取该状态的评论*/
	private String  memoryId;
	/** 将评论内容提交到服务器*/
	private String userId,pid,comment;

	/** * page 当前页码 */
	private int page = 0;
	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_status_info;
	}

	@Override
	protected void setupViews() {
		commonTitleBar_tv_back = (TextView) findViewById(R.id.commonTitleBar_tv_back);
		commonTitleBar_tv_back.setOnClickListener(this);
		//tv_topicname=(TextView) findViewById(R.id.tv_topicname);
		iv_head=(ImageView)findViewById(R.id.iv_head);
		tv_username=(TextView)findViewById(R.id.tv_username);
		tv_createtime=(TextView) findViewById(R.id.tv_createtime);
		tv_describe=(TextView) findViewById(R.id.tv_describe);
		iv_status_image=(ImageView)findViewById(R.id.iv_status_image);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		lv_status_comment=(ListViewForScrollView) findViewById(R.id.lv_status_comment);
		mPullRefreshScrollView=(PullToRefreshScrollView) findViewById(R.id.scrollview);
		scrollview = mPullRefreshScrollView.getRefreshableView();
		et_sendmessage=(EditText) findViewById(R.id.et_sendmessage);
		btn_send=(Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
	}

	@Override
	protected void initialized() {
		waitDialog = DialogUtil.waitDialog(StatusesInfoActivity.this, "加载中...");
		//userId = SharedPreferencesUtil.getSharePreStr(getApplicationContext(), "userInfo", "userId");
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			memoryId = bundle.getString("memoryId");
			pid=bundle.getString("pid");
			//	data=bundle.getParcelable("statuslist");
			//	statuslist = data.getList();
			//	groupInfoAdapter = new StatusAdapter(StatusesInfoActivity.this,statuslist);
			//	lv_status_info.setAdapter(groupInfoAdapter);
			//memoryId=data.getMemoryId();
			waitDialog.show();
			GetInfoMemory(memoryId,page);
			GetMemoryReply(memoryId,page);//根据发表的状态memoryId获取该发表下的评论。
		}else{
			pid="0";
		}
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (mPullRefreshScrollView.isHeaderShown()) {// 下拉刷新
					page = 0;
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetMemoryReply(memoryId,page);//根据发表的状态memoryId获取该发表下的评论。
						}
					}, 1000);
				} else if (mPullRefreshScrollView.isFooterShown()) {// 上拉加载
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							GetMemoryReply(memoryId,page);//根据发表的状态memoryId获取该发表下的评论。
						}
					}, 1000);
				}
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commonTitleBar_tv_back:// 返回键
			finish();
			break;
		case R.id.btn_send:// 评论发送按钮
			comment=et_sendmessage.getText().toString();
			if(ValueUtil.isStrNotEmpty(comment)){
				System.out.println("userId"+userId+"memoryId"+memoryId+"pid"+pid+"comment"+comment);
				AddReply(userId,pid,memoryId,comment);
			}else{
				ToastUtil.ToastMessage(getApplicationContext(),"内容不能为空！");
			}
			break;

		default:
			break;
		}
	}
	/**
	 *  详情
	 * 获取某个状态的详情 
	 * @param string $type '数字'为某个具体记忆的ID
	 *  @param string $page 可选参数，当type为list时传，从0开始计算
	 * @param apiCallBack
	 */

	private void GetInfoMemory(String type, int page) {
		// TODO Auto-generated method stub
		Api.GetInfoMemory(type, page, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				// TODO Auto-generated method stub
				if (result.isValidate()) {
					waitDialog.dismiss();
					status = StatusBean.parse2(result.getMsg());
					userId=status.getUserId();
					//tv_topicname.setText(status.getTitle());
					ImageCacheUitl.displayNormalImageView(iv_head,status.getAvatar(),R.id.iv_head);
					//点击用户头像，跳到个人中心
					iv_head.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent =new Intent();
							intent.putExtra("mineFragment", "mineFragment");
							intent.setClass(getApplicationContext(), MainActivity.class);
							startActivity(intent);
							SharedPreferencesUtil.putSharePre(getApplicationContext(), "userInfo", "pusherId", status.getUserId());
						}
					});
					tv_username.setText(status.getUserName());
					tv_createtime.setText(status.getCtime()); 
					tv_describe.setText(status.getContent());
					if(ValueUtil.isStrNotEmpty(status.getPhoto())){
						iv_status_image.setVisibility(View.VISIBLE);
						ImageCacheUitl.displayNormalImageView(iv_status_image,status.getPhoto(),R.id.iv_status_image);
						iv_status_image.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// 点击放大图片
								Bundle bundle = new Bundle();
								bundle.putStringArrayList("data",(ArrayList<String>) status.getImglist());
								Intent intent =new Intent();
								intent.putExtras(bundle);
								intent.putExtra("ID",0);
								intent.setClass(getApplicationContext(), SimpleSampleActivity.class);
							    startActivity(intent);
							}
						});
					}else{
						iv_status_image.setVisibility(View.GONE);
					}
				
				
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				// TODO Auto-generated method stub

			}

			@Override
			public void fail(String error) {
				// TODO Auto-generated method stub
				waitDialog.dismiss();
			}
		});
	}
	/**
	 * 详情
	 * 根据发表的状态memoryId获取该发表下的评论。
	 * @param token
	 *  @param $memoryId 记忆ID 具体某个记忆下的评论
	 * @param apiCallBack
	 */
	private void GetMemoryReply(String memoryId,int page) {
		Api.GetMemoryReply(memoryId,page, new ApiCallBack() {
			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					waitDialog.dismiss();
					commentBean = ReplyBean.parse(result.getMsg());
					commentlist=commentBean.getReplylists();
					if (ValueUtil.isListNotEmpty(commentlist)) {
						commentAdapter = new CommentAdapter(StatusesInfoActivity.this,commentlist);
						lv_status_comment.setAdapter(commentAdapter);
						scrollview.smoothScrollBy(0, 0);// 设置默认显示顶部
					} 
					
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
			}

			@Override
			public void fail(String error) {
				waitDialog.dismiss();
				ToastUtil.ToastMessage(getContext(), error);
			}
		});
	}
	/**
     * 新建回复
     * @param $token
     * @param $userId 用户ID
     * @param $pid 回复谁，传userId，直接评论则传0
     * @param $memoryId 记忆ID
     * @param $content 回复的内容
     * @return 回复成功返回该回复的ID
     */
	private void AddReply(String userId,String pid,String memoryId,String content) {
		Api.AddReply(userId,pid,memoryId,content, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					et_sendmessage.setText("");
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  //隐藏软键盘            
					imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
					ToastUtil.ToastMessage(getContext(), "评论成功！");
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return true;
	}

}
