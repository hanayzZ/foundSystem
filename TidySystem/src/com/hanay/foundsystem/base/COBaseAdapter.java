package com.hanay.foundsystem.base;

import java.util.ArrayList;
import java.util.List;

import com.hanay.foundsystem.util.ValueUtil;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * @author 李海红
 * @version 创建时间：2014-12-13
 * @description Adapter的基类
 */

public abstract class COBaseAdapter<T> extends BaseAdapter {
	private List<T> dataList;

	public COBaseAdapter(List<T> dataList) {
		if (ValueUtil.isNotEmpty(dataList)) {
			this.dataList = dataList;
		} else {
			this.dataList = new ArrayList<T>();
		}
	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public abstract View getView(int position, View convertView, ViewGroup parent);

	public void notifyDataSetChanged(List<T> dataList) {
		this.dataList = dataList;
		notifyDataSetChanged();
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> list) {
		dataList = list;
	}

	public T getData(int index) {
		return dataList.get(index);
	}

}
