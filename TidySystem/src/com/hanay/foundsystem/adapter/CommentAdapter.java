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
 * 评论适配器
 */
public class CommentAdapter extends BaseAdapter{

	private Context context;
	private List<ReplyBean> commentlist = new ArrayList<ReplyBean>();
	private ReplyBean replyBean;
	public CommentAdapter(Context context, List<ReplyBean> commentlist) {
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
			holder.tv_pushername=(TextView) convertView.findViewById(R.id.tv_pushername);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (ValueUtil.isNotEmpty(replyBean)) {
			ImageCacheUitl.displayNormalImageView(holder.iv_avatar, replyBean.getAvatar(), R.id.iv_avatar);
			/*if(replyBean.getPid()==replyBean.getPid()){
				holder.name.setText(replyBean.getUsername());
			}else{
				holder.name.setText(replyBean.getPusername());
			}*/
			holder.tv_content.setText(replyBean.getContent());
			holder.tv_commentTime.setText(replyBean.getCtime());
			if(replyBean.getPid().equals("0")){
				holder.name.setText(replyBean.getPusername());
				holder.tv_pushername.setVisibility(View.GONE);
			}else{
				holder.name.setText(replyBean.getUsername());
				holder.tv_pushername.setVisibility(View.VISIBLE);
				holder.tv_pushername.setText("回复："+replyBean.getPusername());
			}
			
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView iv_avatar;
		TextView name;
		TextView tv_content;
		TextView tv_commentTime;
		TextView tv_pushername;
	}


}
