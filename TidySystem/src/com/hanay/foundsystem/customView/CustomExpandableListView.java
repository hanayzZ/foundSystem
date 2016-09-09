package com.hanay.foundsystem.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * @author
 * @version 创建时间：2014-12-30
 * @description 自定义ListView(ScrollView嵌套ExpandableListView)
 */

public class CustomExpandableListView extends ExpandableListView {

	public CustomExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
