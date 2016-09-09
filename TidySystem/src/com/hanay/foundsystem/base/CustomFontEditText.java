package com.hanay.foundsystem.base;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 李海红
 * @version 创建时间：2014-12-13
 * @description 自定义字体
 */
public class CustomFontEditText extends EditText{

	public CustomFontEditText(Context context) {
		super(context);
		init(context);
	}

	public CustomFontEditText(Context context,AttributeSet attrs) {
		super(context,attrs);
		init(context);
	}
	public CustomFontEditText(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
		init(context);
	}
	private void init(Context context){
		AssetManager assertMgr=context.getAssets();
		Typeface font=Typeface.createFromAsset(assertMgr, "fonts/FZY1JW.TTF");
		setTypeface(font);
	}
}
