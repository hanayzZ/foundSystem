package com.hanay.foundsystem.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.adapter.AlbumGridViewAdapter;
import com.hanay.foundsystem.imageutils.AlbumHelper;
import com.hanay.foundsystem.imageutils.Bimp;
import com.hanay.foundsystem.imageutils.FileUtils;
import com.hanay.foundsystem.imageutils.ImageBucket;
import com.hanay.foundsystem.imageutils.ImageItem;
import com.hanay.foundsystem.imageutils.PublicWay;
import com.hanay.foundsystem.imageutils.Res;


/**
 * @author 李海红
 * @version 创建时间：2014-12-19
 * @description 这个是进入相册显示所有图片的界面
 */

public class AlbumActivity extends Activity {
	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private Button okButton;
	// 返回按钮
	private Button back;
	// 取消按钮
	private Button cancel;
	private Intent intent;
	// 预览按钮
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_album"));
		PublicWay.activityList.add(this);
		mContext = this;
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		/*IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);*/
		bitmap = BitmapFactory.decodeResource(getResources(), Res.getDrawableID("plugin_camera_no_pictures"));
		init();
		initListener();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

/*	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mContext.unregisterReceiver(this);
			gridImageAdapter.notifyDataSetChanged();
		}
	};*/

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				intent.putExtra("position", "1");
				intent.setClass(AlbumActivity.this, GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
			intent.setClass(mContext, UploadActivity.class);
			startActivity(intent);
			finish();
		}

	}

	// 返回按钮监听，跳到文件夹界面
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			intent.setClass(AlbumActivity.this, ImageFileActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// 取消按钮的监听，跳到上传数据界面
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			Bimp.tempSelectBitmap.clear();
			intent.setClass(mContext, UploadActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		back = (Button) findViewById(Res.getWidgetID("back"));
		cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		cancel.setOnClickListener(new CancelListener());
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(Res.getWidgetID("preview"));
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList, Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
		okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
	}

	private void initListener() {

		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
				if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
					toggleButton.setChecked(false);
					chooseBt.setVisibility(View.GONE);
					if (!removeOneData(dataList.get(position))) {
						Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"), 200).show();
					}
					return;
				}
				if (isChecked) {
					chooseBt.setVisibility(View.VISIBLE);
					try {
						Bitmap bmp = Bimp.revitionImageSize(dataList.get(position).getImagePath());// 压缩
						FileUtils.saveBitmap(bmp, dataList.get(position).getName());
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					Bimp.tempSelectBitmap.add(dataList.get(position));
					okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				} else {
					chooseBt.setVisibility(View.GONE);
					Bimp.tempSelectBitmap.remove(dataList.get(position));
					chooseBt.setVisibility(View.GONE);
					okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				}
				isShowOkBt();
			}
		});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap.contains(imageItem)) {
			Bimp.tempSelectBitmap.remove(imageItem);
			okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
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
			intent.setClass(AlbumActivity.this, UploadActivity.class);
			startActivity(intent);
			finish();
		}
		return true;
	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}
}
