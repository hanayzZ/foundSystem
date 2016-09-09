package com.hanay.foundsystem.imageutils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author 李海红
 * @version 创建时间：2014-12-19
 * @description
 */
public class NoScrollGridView extends GridView {
	public NoScrollGridView(Context context) {
		super(context);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
