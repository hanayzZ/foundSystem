package com.hanay.foundsystem.chat.chatter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hanay.foundsystem.R;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class ChatEmoticons {

	private Context mContext;
	private EditText mEditText;
	private Dialog mDialog;
	
	private int[] mImageIds;
	private List<Map<String,Object>> mListItems;
	
	public ChatEmoticons(Context context, EditText editText) {
		mContext = context;
		mEditText = editText;
		
		mImageIds = new int[107];
		mListItems = new ArrayList<Map<String,Object>>();
		init();
	}
	
	public void init() {
		for(int i = 0; i < 107; i++){
			try {
				if(i<10){
					Field field = R.drawable.class.getDeclaredField("f00" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					mImageIds[i] = resourceId;
				}else if(i<100){
					Field field = R.drawable.class.getDeclaredField("f0" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					mImageIds[i] = resourceId;
				}else{
					Field field = R.drawable.class.getDeclaredField("f" + i);
					int resourceId = Integer.parseInt(field.get(null).toString());
					mImageIds[i] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	        Map<String,Object> listItem = new HashMap<String,Object>();
			listItem.put("image", mImageIds[i]);
			mListItems.add(listItem);
		}
	}
	
	public void createExpressionDialog() {
		mDialog = new Dialog(mContext);
		GridView gridView = createGridView();
		mDialog.setContentView(gridView);
		mDialog.setTitle("emoticons");
		mDialog.show();
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bitmap bitmap = null;
				bitmap = BitmapFactory.decodeResource(mContext.getResources(), mImageIds[arg2 % mImageIds.length]);
				ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
				String str = null;
				if(arg2<10){
					str = "f00"+arg2;
				}else if(arg2<100){
					str = "f0"+arg2;
				}else{
					str = "f"+arg2;
				}
				SpannableString spannableString = new SpannableString(str);
		//		SpannableString spannableString = new SpannableString("an emo");
				spannableString.setSpan(imageSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mEditText.append(spannableString);
				mDialog.dismiss();
			}
		});
	}
	
	/**
	 * 生成一个表情对话框中的gridview
	 * @return
	 */
	private GridView createGridView() {
		final GridView view = new GridView(mContext);
		
		SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, mListItems, R.layout.cb0_chat_emoticon, new String[]{"image"}, new int[]{R.id.cb0_chat_emoticon_image});
		view.setAdapter(simpleAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		return view;
	}
	
}