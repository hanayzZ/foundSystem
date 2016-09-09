package com.hanay.foundsystem.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.activity.ChatActivity;
import com.hanay.foundsystem.activity.LoginActivity;
import com.hanay.foundsystem.activity.MainActivity;
import com.hanay.foundsystem.activity.SimpleSampleActivity;
import com.hanay.foundsystem.activity.StatusesInfoActivity;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseApplication;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;

/**
 * @author 李海红
 * @version 创建时间：2014-3-5
 * @description 记忆列表数据的适配
 */

public class StatusAdapter extends BaseAdapter{
	//网格图片适配器
	//private GridsAdapter adapter;
	//评论
	//private CommentAdapter commentAdapter;
	//	private List<ReplyBean> commentlist = new ArrayList<ReplyBean>();

	private Context context;
	private List<StatusBean> statuslists=new ArrayList<StatusBean>();
	//	private List<String> pathlists=new ArrayList<String>();
	private StatusBean bean;
	public static int flag = 0;
	public StatusAdapter(Context context,List<StatusBean> statuslists) {
		this.context=context;
		this.statuslists=statuslists;
	}
	/*
	public StatusAdapter(Context context,List<StatusBean> statuslists,List<ReplyBean> commentlist) {
		this.context=context;
		this.statuslists=statuslists;
		this.commentlist=commentlist;
	}
	 */
	public int getCount() {
		return statuslists.size();
	}

	public Object getItem(int position) {
		return statuslists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		bean=statuslists.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_status_info, null); 
			holder = new ViewHolder();
			//holder.tv_topicname=(TextView) convertView.findViewById(R.id.tv_topicname);
			holder.iv_head=(ImageView) convertView.findViewById(R.id.iv_head);
			holder.tv_username=(TextView) convertView.findViewById(R.id.tv_username);
			holder.tv_createtime=(TextView) convertView.findViewById(R.id.tv_createtime);
			holder.tv_describe=(TextView) convertView.findViewById(R.id.tv_describe);
			//	holder.gv_groupinfo=(GridView) convertView.findViewById(R.id.gv_groupinfo);
			holder.iv_status_image=(ImageView) convertView.findViewById(R.id.iv_status_image);
			holder.tv_comment=(TextView) convertView.findViewById(R.id.tv_comment);
			holder.tv_share=(TextView) convertView.findViewById(R.id.tv_share);
			holder.tv_favorite=(TextView) convertView.findViewById(R.id.tv_favorite);
			//holder.tv_chat=(TextView) convertView.findViewById(R.id.tv_chat);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.tv_topicname.setText(bean.getTitle());
		ImageCacheUitl.displayNormalImageView(holder.iv_head,bean.getAvatar(),R.id.iv_head);
		//点击用户头像，跳到个人中心
		holder.iv_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*	Intent intent =new Intent();
				intent.putExtra("userId", bean.getUserId());
				intent.setClass(context, MineFragment.class);
				context.startActivity(intent);*/
				Intent intent =new Intent();
				intent.setClass(context, MainActivity.class);
				context.startActivity(intent);
				SharedPreferencesUtil.putSharePre(context, "userInfo", "pusherId", bean.getUserId());
			}
		});
		holder.tv_username.setText(bean.getUserName());
		holder.tv_createtime.setText(bean.getCtime()); 
		holder.tv_describe.setText(bean.getContent());
		//pathlists=bean.getImglist();
		//	if(pathlists.size()==1){
		if(ValueUtil.isStrNotEmpty(bean.getPhoto())){
			holder.iv_status_image.setVisibility(View.VISIBLE);
			ImageCacheUitl.displayNormalImageView(holder.iv_status_image, bean.getPhoto(),R.id.iv_status_image);	
			holder.iv_status_image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// 点击放大图片
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("data",(ArrayList<String>) statuslists.get(position).getImglist());
					Intent intent =new Intent();
					intent.putExtras(bundle);
					intent.putExtra("ID",0);
					intent.setClass(context, SimpleSampleActivity.class);
					context.startActivity(intent);
				}
			});
		}else{
			holder.iv_status_image.setVisibility(View.GONE);
		}
	
		//	}
		/*else{
			adapter = new GridsAdapter(context, pathlists);
			holder.gv_groupinfo.setAdapter(adapter);
			holder.gv_groupinfo.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int id, long arg3) {
					//点击放大图片
					//将图片lists用budlle传过去
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("data",(ArrayList<String>) statuslists.get(position).getImglist());
					Intent intent =new Intent();
					intent.putExtras(bundle);
					intent.putExtra("ID",id);
					intent.setClass(context, SimpleSampleActivity.class);
					context.startActivity(intent);
				}
			});
		}*/
		//判断用户是否登陆
	     final String loginId = SharedPreferencesUtil.getSharePreStr(context, "userInfo", "userId");

			//登陆过，可以进行评论，收藏，私聊
			holder.tv_comment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (ValueUtil.isStrNotEmpty(loginId)) {
						// 点击评论，跳到详情界面
						Intent intent =new Intent();
						Bundle bundle = new Bundle();
						bundle.putString("memoryId",bean.getMemoryId());
						intent.putExtras(bundle);
						intent.setClass(context, StatusesInfoActivity.class);
						context.startActivity(intent);
					}else{
						//未登录跳到登陆界面
						Intent intent=new Intent(context,LoginActivity.class);
						context.startActivity(intent);
					}
				
					
				}
			});
			holder.tv_favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (ValueUtil.isStrNotEmpty(loginId)){
						if(flag==0){
							// 点击收藏，收藏到自己的收藏夹
							v.setBackgroundResource(R.drawable.ico_favorite);	
							FavoriteMemory(bean.getUserId(),bean.getMemoryId(),"1");
							flag = 1;
						}else{
							//取消收藏
							v.setBackgroundResource(R.drawable.ico_fav_p);	
							FavoriteMemory(bean.getUserId(),bean.getMemoryId(),"0");
							flag = 0;
						}

					}else{
						//未登录跳到登陆界面
						Intent intent=new Intent(context,LoginActivity.class);
						context.startActivity(intent);
					}
					}
			});
			/*holder.tv_chat.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// 点击私聊，跳到聊天界面
					Intent intent=new Intent(context,ChatActivity.class);
					intent.putExtra("toUser","1");
					//ChatService.getInstance().setEnterFromNotificationId(Integer.parseInt("1"));
					context.startActivity(intent);
				}
			});*/

		
		
		holder.tv_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 点击分享，跳到分享界面
				Intent intent=new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT,"寻物启示：");
				intent.putExtra(Intent.EXTRA_TEXT,bean.getContent());
				context.startActivity(Intent.createChooser(intent, "拾芥"));
			}
		});
		
		//	commentAdapter= new CommentAdapter(context, commentlist);
		return convertView;
	}
	protected void FavoriteMemory(String userId, String memoryId, final String type) {
		// TODO Auto-generated method stub
		Api.FavoriteMemory(userId, memoryId, type, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				// TODO Auto-generated method stub
				if(type.equals("1")){//收藏
					if (result.isValidate()) {
						ToastUtil.ToastMessage(context,"收藏成功！");
					} else {
						ToastUtil.ToastMessage(context, result.getMsg());
						flag = 1;
					}
				}else{//取消
					if (result.isValidate()) {
						ToastUtil.ToastMessage(context, "取消成功！");
					} else {
						ToastUtil.ToastMessage(context, result.getMsg());
						flag = 2;
					}
					
				}

			}

			@Override
			public void loading() {
				// TODO Auto-generated method stub

			}

			@Override
			public void fail(String error) {
				// TODO Auto-generated method stub
				ToastUtil.ToastMessage(context,error);
			}
		});
	}
	public class ViewHolder {
		//public TextView tv_topicname;//主题名称
		public ImageView iv_head;//用户头像
		public TextView tv_username;//用户名
		public TextView tv_createtime;//发布时间
		public TextView tv_describe;//描述
		//public GridView gv_groupinfo;//多张图片
		public ImageView iv_status_image;//一张图片
		public TextView tv_comment;//评论
		public TextView tv_share;//分享
		public TextView tv_favorite;//收藏
		//public TextView tv_chat;//私聊
	}
	//图片网格布局的适配
	public class GridsAdapter extends BaseAdapter {
		private Context context;
		private List<String> pathlist=new ArrayList<String>();
		public GridsAdapter(Context context, List<String> pathlist) {
			this.context=context;
			this.pathlist=pathlist;
		}

		public int getCount() {
			return pathlist.size();
		}
		public Object getItem(int position) {
			return pathlist.get(position);
		}

		public long getItemId(int position) {
			return position;
		}
		public View getView(int position,View convertView, ViewGroup parent) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_groupinfo_grida, null);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(BaseApplication.width * 2 / 7, BaseApplication.width * 2 / 7 );
				params.gravity = Gravity.CENTER;
				holder.image.setLayoutParams(params);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageCacheUitl.displayNormalImageView(holder.image,pathlist.get(position), R.id.item_grida_image);
			return convertView;
		}
		public class ViewHolder {
			public ImageView image;
		}
	}


}
