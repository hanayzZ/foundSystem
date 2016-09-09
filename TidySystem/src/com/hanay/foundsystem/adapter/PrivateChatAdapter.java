package com.hanay.foundsystem.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.base.COBaseAdapter;
import com.hanay.foundsystem.bean.PrivateChatBean;
import com.hanay.foundsystem.util.ValueUtil;


/**
 * @author 李海红
 * @version 创建时间：2015-3-20
 * @description  私信列表的适配器
 */

public class PrivateChatAdapter extends COBaseAdapter<PrivateChatBean> {
	private Context context;

	public PrivateChatAdapter(Context context, List<PrivateChatBean> dataList) {
		super(dataList);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_private_chat, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.mov = (TextView) convertView.findViewById(R.id.tv_tel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PrivateChatBean data = getData(position);
		/*if(ChatServiceData.getInstance().getUnreadMsgs(Integer.parseInt(data.getId()))>0){
			holder.name.setTextColor(context.getResources().getColor(R.color.red));
		}else{
			holder.name.setTextColor(context.getResources().getColor(R.color.black));
		}
		 */
		if (ValueUtil.isNotEmpty(data)) {
			holder.name.setText(data.getName());
			holder.mov.setText("最新一条信息");
		
		}

		return convertView;
	}

	final static class ViewHolder {
		TextView name;
		TextView mov;
	}

}
