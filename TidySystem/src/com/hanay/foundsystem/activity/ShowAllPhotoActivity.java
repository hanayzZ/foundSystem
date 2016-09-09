package com.hanay.foundsystem.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.hanay.foundsystem.adapter.AlbumGridViewAdapter;
import com.hanay.foundsystem.imageutils.Bimp;
import com.hanay.foundsystem.imageutils.FileUtils;
import com.hanay.foundsystem.imageutils.ImageItem;
import com.hanay.foundsystem.imageutils.PublicWay;
import com.hanay.foundsystem.imageutils.Res;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * @author 李海红
 * @version 创建时间：2014-12-19
 * @description 这个是显示一个文件夹里面的所有图片时的界面
 */
public class ShowAllPhotoActivity extends Activity {
	private GridView gridView;
	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	
	/**完成按钮*/ 
	private Button okButton;
	
	/**预览按钮*/
	private Button preview;
	
	/** 返回按钮*/
	private Button back;
	
	/**取消按钮*/
	private Button cancel;
	
	/**标题*/
	private TextView headTitle;
	
	private Context mContext;
	private Intent intent;
	
	public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_show_all_photo"));
		PublicWay.activityList.add(this);
		mContext = this;
		back = (Button) findViewById(Res.getWidgetID("showallphoto_back"));
		cancel = (Button) findViewById(Res.getWidgetID("showallphoto_cancel"));
		preview = (Button) findViewById(Res.getWidgetID("showallphoto_preview"));
		okButton = (Button) findViewById(Res.getWidgetID("showallphoto_ok_button"));
		headTitle = (TextView) findViewById(Res.getWidgetID("showallphoto_headtitle"));
		this.intent = getIntent();
		String folderName = intent.getStringExtra("folderName");
		if (folderName.length() > 8) {
			folderName = folderName.substring(0, 9) + "...";
		}
		headTitle.setText(folderName);
		cancel.setOnClickListener(new CancelListener());
		back.setOnClickListener(new BackListener(intent));
		preview.setOnClickListener(new PreviewListener());
		init();
		initListener();
		isShowOkBt();
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {  
		  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	gridImageAdapter.notifyDataSetChanged();
        }  
    };  

	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				intent.putExtra("position", "2");
				intent.setClass(ShowAllPhotoActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	private class BackListener implements OnClickListener {// 返回按钮监听
		Intent intent;

		public BackListener(Intent intent) {
			this.intent = intent;
		}

		public void onClick(View v) {
			intent.setClass(ShowAllPhotoActivity.this, ImageFileActivity.class);
			startActivity(intent);
		}

	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			Bimp.tempSelectBitmap.clear();
			intent.setClass(mContext, UploadActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void init() {
		IntentFilter filter = new IntentFilter("data.broadcast.action");  
		registerReceiver(broadcastReceiver, filter);  
		progressBar = (ProgressBar) findViewById(Res.getWidgetID("showallphoto_progressbar"));
		progressBar.setVisibility(View.GONE);
		gridView = (GridView) findViewById(Res.getWidgetID("showallphoto_myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this,dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		okButton = (Button) findViewById(Res.getWidgetID("showallphoto_ok_button"));
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked,
							Button button) {
						if (Bimp.tempSelectBitmap.size() >= PublicWay.num&&isChecked) {
							button.setVisibility(View.GONE);
							toggleButton.setChecked(false);
							Toast.makeText(ShowAllPhotoActivity.this, Res.getString("only_choose_num"), 200)
									.show();
							return;
						}

						if (isChecked) {
							Log.i("-----------------", dataList.get(position).getName());
							button.setVisibility(View.VISIBLE);
							try {
								Bitmap bmp = Bimp.revitionImageSize(dataList.get(position).getImagePath());// 压缩
								FileUtils.saveBitmap(bmp, dataList.get(position).getName());
							} catch (IOException e) {
								e.printStackTrace();
							}
							Bimp.tempSelectBitmap.add(dataList.get(position));
							okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size()
									+ "/"+PublicWay.num+")");
						} else {
							button.setVisibility(View.GONE);
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				okButton.setClickable(false);
				intent.setClass(mContext, UploadActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(Res.getString("finish")+"(" + Bimp.tempSelectBitmap.size() + "/"+PublicWay.num+")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*intent.setClass(ShowAllPhotoActivity.this, SuperviseUploadActivity.class);
			startActivity(intent);*/
			finish();
		}

		return false;

	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

}
