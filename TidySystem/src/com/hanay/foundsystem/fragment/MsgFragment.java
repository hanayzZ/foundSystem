package com.hanay.foundsystem.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.activity.LoginActivity;
import com.hanay.foundsystem.activity.RegisterActivity;
import com.hanay.foundsystem.activity.StatusesInfoActivity;
import com.hanay.foundsystem.adapter.PrivateChatAdapter;
import com.hanay.foundsystem.adapter.ReplyAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseFragment;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.bean.ReplyBean;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.chat.chatServices.ChatService;
import com.hanay.foundsystem.chat.chatServices.InitData;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * @author 李海红
 * @version 创建时间：2014-3-5
 * @description  导航标签(消息)
 */

public class MsgFragment extends BaseFragment implements OnClickListener,OnItemClickListener{

	/** 评论、私信 */
	private RadioGroup rg_supervise;
	private RadioButton rb_reply, rb_msg;
	/** RadioButton底部蓝线 */
	private TextView main_navi_show;
	private int width;

	/**未登陆*/
	LinearLayout ll_out;
	Button btn_register;
	Button btn_login;
	/**提示*/
	private TextView tv_tip;


	/** 私信 */
	private PrivateChatBean bean;
	private List<PrivateChatBean> chatlist;
	private static  PrivateChatAdapter adapter;
	private PullToRefreshListView lv_PrivateChat;

	/** 评论 */
	private ReplyBean replyBean;
	private List<ReplyBean> replyList;
	private ReplyAdapter replyAdapter;
	private PullToRefreshListView lv_reply;
	/** * userId获取该用户的所有评论 */
	private String userId;
	//Calendar calendar;
	/** * page 当前页码*/
	private int page = 0;
	/** 加载等待 */
	private ProgressBar progressBar;
	/** * 暂无消息*/
	private TextView noMsg;

	private Handler handler = new Handler();
	public static PrivateChatAdapter getAdapter() {
		return adapter;
	}
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_msg;
	}

	@Override
	protected void setupViews(View view) {
		tv_tip=(TextView) view.findViewById(R.id.tv_tip);
		ll_out= (LinearLayout) view.findViewById(R.id.ll_out);
		btn_register=(Button) view.findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		btn_login=(Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		rg_supervise = (RadioGroup) view.findViewById(R.id.rg_supervise);
		rb_reply = (RadioButton) view.findViewById(R.id.rb_reply);
		rb_msg = (RadioButton) view.findViewById(R.id.rb_msg);

		/** 获取屏幕宽度 */
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels / 2;
		/** 底部选中的蓝线 */
		main_navi_show = (TextView) view.findViewById(R.id.main_navi_show);

		/**默认私聊*/
		lv_PrivateChat = (PullToRefreshListView)view.findViewById(R.id.lv_PrivateChat);
		noMsg = (TextView)view.findViewById(R.id.noMsg);
		//noMsg.setOnClickListener(this);
		lv_reply=(PullToRefreshListView) view.findViewById(R.id.lv_reply);
		/** 进度条 */
		progressBar = (ProgressBar) view.findViewById(R.id.pb_msg);
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
		userId = SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "userId");
		//判断用户是否登陆
		if(userId!=null&&userId.length()>0){
			rg_supervise.setVisibility(View.VISIBLE);
			//main_navi_show.setVisibility(View.VISIBLE);
			ll_out.setVisibility(View.GONE);
			tv_tip.setVisibility(View.GONE);
			rg_supervise.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.rb_reply:
						lv_reply.setVisibility(View.VISIBLE);
						lv_PrivateChat.setVisibility(View.GONE);
						rb_reply.setTextSize(18);
						rb_msg.setTextSize(15);
						setNaviShow(main_navi_show, 0);
						if (ValueUtil.isListEmpty(replyList)) {
							progressBar.setVisibility(View.VISIBLE);
							GetMyReply(userId,page);
						} else {
							replyAdapter = new ReplyAdapter(getActivity(), replyList);
							lv_reply.setAdapter(replyAdapter);
						}
						break;
						/*	case R.id.rb_msg:
						lv_PrivateChat.setVisibility(View.VISIBLE);
						lv_reply.setVisibility(View.GONE);
						rb_msg.setTextSize(18);
						rb_reply.setTextSize(15);
						setNaviShow(main_navi_show, 1);
						if (ValueUtil.isListEmpty(chatlist)) {
							progressBar.setVisibility(View.VISIBLE);
							//getChatList(userId, page);	
						} else {
							adapter = new PrivateChatAdapter(getActivity(), chatlist);
							lv_PrivateChat.setAdapter(adapter);
						}
						break;*/
					default:
						break;
					}	
				}
			});
			lv_reply.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final ReplyBean event = (ReplyBean) parent.getItemAtPosition(position);
					Bundle bundle = new Bundle();
					bundle.putString("memoryId",event.getMemoryId());
					bundle.putString("userId", event.getUserId());
					bundle.putString("pid", event.getPid());
					startActivity(StatusesInfoActivity.class, bundle);
				}
			});
			lv_PrivateChat.setOnItemClickListener(this);

			lv_reply.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (lv_reply.isHeaderShown()) {// 下拉刷新
						page = 0;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								GetMyReply(userId,page);
							}
						}, 1000);
					} else if (lv_reply.isFooterShown()) {// 上拉加载
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								//page++;
								GetMyReply(userId,page);
							}
						}, 1000);
					}
				}
			});
			lv_PrivateChat.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (lv_PrivateChat.isHeaderShown()) {// 下拉刷新
						page = 0;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getChatList(userId, page);	
							}
						}, 1000);
					} else if (lv_PrivateChat.isFooterShown()) {// 上拉加载
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getChatList(userId, page);	
							}
						}, 1000);
					}
				}
			});
			rb_reply.setChecked(true);

		}else{
			ll_out.setVisibility(View.VISIBLE);
			tv_tip.setVisibility(View.VISIBLE);
			rg_supervise.setVisibility(View.GONE);
			//main_navi_show.setVisibility(View.GONE);
		}
	}

	/**
	 * 根据userId获取该用户的所有评论。
	 * @param token
	 *  @param $userId 用户ID 我的评论
	 *  @param $page 分页 从0开始
	 * @param apiCallBack
	 */
	private void GetMyReply(final String userId, final int pagetype) {
		Api.GetMyReply(userId, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					progressBar.setVisibility(View.GONE);
					if(pagetype==0){
						replyBean = ReplyBean.parse2(result.getMsg());
						replyList=replyBean.getReplylists();
						if (ValueUtil.isListNotEmpty(replyList)) {
							replyAdapter= new ReplyAdapter(getContext(), replyList);
							lv_reply.setAdapter(replyAdapter);
							//page++;
						}else{
							//lv_newTopic.setEmptyView(tv_noData);
						}

					}else{
						//多页
						replyBean = ReplyBean.parse(result.getMsg());
						replyList.addAll(replyBean.getReplylists());
						replyAdapter.notifyDataSetChanged();
						//page++;
					}
					lv_reply.onRefreshComplete();
				} else {
					progressBar.setVisibility(View.GONE);
					noMsg.setVisibility(View.VISIBLE);
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				//noMsg.setVisibility(View.VISIBLE);
				lv_reply.onRefreshComplete();
				/*	if(rb_reply.isChecked()){	
					if(ValueUtil.isListNotEmpty(replyList)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						lv_PrivateChat.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}else if(rb_msg.isChecked()){	
					if(ValueUtil.isListNotEmpty(chatlist)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						lv_reply.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}*/


			}
		});
	}
	/**获取私信列表*/
	private void getChatList(final String userId,final int pagetype) {
		Api.getChatList(userId, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					if (pagetype == 0) {
						progressBar.setVisibility(View.GONE);
						bean = PrivateChatBean.parse(result.getMsg(),userId);
						chatlist=bean.getLists();
						if (ValueUtil.isListNotEmpty(chatlist)) {
							adapter = new PrivateChatAdapter(getContext(), chatlist);
							lv_PrivateChat.setAdapter(adapter);
							lv_PrivateChat.setOnItemClickListener(MsgFragment.this);
							InitData initdata=InitData.getInitData();
							initdata.msg5Arrive(chatlist);
							//page++;
						} else {
							//lv_newTopic.setEmptyView(tv_noData);
						}

					}else{
						//多页显示
						bean = PrivateChatBean.parse(result.getMsg(),userId);
						chatlist.addAll(bean.getLists());
						adapter.notifyDataSetChanged();
						//page++;
					}

					lv_PrivateChat.onRefreshComplete();
				} else {
					noMsg.setVisibility(View.VISIBLE);
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				noMsg.setVisibility(View.VISIBLE);
				lv_PrivateChat.onRefreshComplete();
				if(rb_reply.isChecked()){	
					if(ValueUtil.isListNotEmpty(replyList)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						lv_PrivateChat.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}else if(rb_msg.isChecked()){	
					if(ValueUtil.isListNotEmpty(chatlist)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						lv_reply.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_register://注册
			startActivity(RegisterActivity.class);
			break;
		case R.id.btn_login://登陆
			startActivity(LoginActivity.class);
			break;
			/*	case R.id.noMsg:
			noMsg.setVisibility(View.GONE);
			if (rb_reply.isChecked()) {
				progressBar.setVisibility(View.VISIBLE);
				GetMyReply(userId,page);
			} else if (rb_msg.isChecked()) {
				progressBar.setVisibility(View.VISIBLE);
				getChatList(userId, page);	
			} 
			break;*/
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent=new Intent(getActivity(),ChatActivity.class);
		intent.putExtra("toUser", chatlist.get(position));
		//ChatService.getInstance().setEnterFromNotificationId(Integer.parseInt(chatlist.get(position).getId()));
		startActivity(intent);

	}
	@Override
	public void onResume() {
		if(adapter!=null)
			adapter.notifyDataSetChanged();
		super.onResume();
	}

}
