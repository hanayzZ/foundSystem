package com.hanay.foundsystem.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.bean.ReplyBean;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.ValueUtil;

/**
 * @author 李海红
 * @version 创建时间：2015-3-20
 * @description  评论列表的适配器
 */

public class ReplyAdapter extends BaseAdapter {

	private Context context;
	private List<ReplyBean> commentlist = new ArrayList<ReplyBean>();
	private ReplyBean replyBean;
	public ReplyAdapter(Context context, List<ReplyBean> commentlist) {
		this.commentlist=commentlist;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentlist.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return commentlist.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		replyBean=commentlist.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_show_person, parent, false);
			holder.iv_avatar=(ImageView) convertView.findViewById(R.id.iv_avatar);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_commentTime=(TextView) convertView.findViewById(R.id.tv_commentTime);
			holder.iv_photo=(ImageView) convertView.findViewById(R.id.iv_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (ValueUtil.isNotEmpty(replyBean)) {
			ImageCacheUitl.displayNormalImageView(holder.iv_avatar, replyBean.getAvatar(), R.id.iv_avatar);
			//if(replyBean.getPid()==replyBean.getUserId()&&replyBean.getPid().isEmpty()){
				//评论人是本人的话，评论人显示自己的名字和头像
			//	holder.name.setText(replyBean.getUsername());
			//}else{
				//评论人不是自己的话，显示评论人的名字和头像
				//holder.name.setText(replyBean.getPusername());
			//}
			holder.tv_content.setText(replyBean.getContent());
			holder.tv_commentTime.setText(replyBean.getCtime());
			holder.tv_pushername=(TextView) convertView.findViewById(R.id.tv_pushername);
			if(replyBean.getPid().equals("0")){
				holder.name.setText(replyBean.getPusername());
				//holder.name.setText("hanayzZ");
				holder.tv_pushername.setVisibility(View.GONE);
			}else{
				holder.name.setText(replyBean.getUsername());
				holder.tv_pushername.setVisibility(View.VISIBLE);
				holder.tv_pushername.setText("回复："+replyBean.getPusername());
			}
			holder.iv_photo.setVisibility(View.VISIBLE);
			ImageCacheUitl.displayNormalImageView(holder.iv_photo,replyBean.getMemoryPhoto(), R.id.iv_photo);

		}

		return convertView;
	}

	public class ViewHolder {
		ImageView iv_avatar;
		TextView name;
		TextView tv_content;
		TextView tv_commentTime;
		TextView tv_pushername;
		ImageView iv_photo;
	}


}
