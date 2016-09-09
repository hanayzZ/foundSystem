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
import com.hanay.foundsystem.bean.TopicBean;


/**
 * @author 李海红
 * @version 创建时间：2015-3-19
 * @description 获取话题数据列表适配器
 */
public class TopicAdapter extends BaseAdapter {
	private Context context;
	private List<TopicBean> lists=new ArrayList<TopicBean>();
	private TopicBean event;
	public TopicAdapter( Context context,List<TopicBean> lists) {  
		this.context = context;  
		this.lists = lists; 
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		event =lists.get(position);
		ViewHolder holder=null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_topic,null);
			holder = new ViewHolder();
			holder.tv_topic = (TextView) convertView.findViewById(R.id.tv_topic);
			holder.tv_browse=(TextView) convertView.findViewById(R.id.tv_browse);
			convertView.setTag(holder);
		}
		else{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_topic.setText(event.getTitle());
		holder.tv_browse.setText(event.getCount()+"次浏览");
		return convertView;
	}
	private class ViewHolder {
		private TextView tv_topic;
		private TextView tv_browse;
	}
}
