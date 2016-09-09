package com.hanay.foundsystem.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.base.BaseApplication;
import com.hanay.foundsystem.base.CustomFontEditText;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.imageutils.Bimp;
import com.hanay.foundsystem.imageutils.FileUtils;
import com.hanay.foundsystem.imageutils.ImageItem;
import com.hanay.foundsystem.imageutils.PublicWay;
import com.hanay.foundsystem.imageutils.Res;
import com.hanay.foundsystem.util.DensityUtil;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.NetWorkUtil;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.Util;
import com.hanay.foundsystem.util.ValueUtil;


/**
 * @author 李海红
 * @version 创建时间：2014-3-2
 * @description 发布状态
 */

public class UploadActivity extends BaseActivity implements OnClickListener {

	/** 返回*/
	private TextView tv_publish_back;
	/** 登录 */
	private Button btn_publish;

	/** 话题 */
	private CustomFontEditText et_topic;

	/** 发表的内容 */
	private CustomFontEditText et_describe;

	/** 定位 */
	private LocationClient mLocationClient;
	//	private EditText tv_address;
	/** 话题Bean */
	//private TopicBean topicbean;
	private String topicId;

	/** 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;

	/** 拍照的照片存储位置 */
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/Photo_LJ/");

	/** 照相机拍照得到的图片 */
	private File mCurrentPhotoFile;
	private String FileName;

	/** 压缩图片 */
	private Bitmap bmp;

	public static Bitmap bimap;
	/** 网格显示图片 */
	private GridView noScrollgridview;

	/** GridView的间隔的宽度像素 */
	private int gridViewWidth;

	/** 图片网格适配器 */
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private String userId,title,memory;

	/** 等待框 */
	private Dialog waitDialog;

	@Override
	protected int getLayoutId() {
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		parentView = getLayoutInflater().inflate(R.layout.activity_publish, null);
		PublicWay.activityList.add(this);
		return R.layout.activity_publish;
	}

	@Override
	protected void setupViews() {
		tv_publish_back=(TextView) findViewById(R.id.tv_publish_back);
		tv_publish_back.setOnClickListener(this);
		//tv_publish=(TextView) findViewById(R.id.tv_publish);
		btn_publish = (Button) findViewById(R.id.btn_publish);
		btn_publish.setOnClickListener(this);
		et_topic=(CustomFontEditText) findViewById(R.id.et_topic);
		et_describe = (CustomFontEditText) findViewById(R.id.et_describe);
		//	tv_address = (EditText) findViewById(R.id.tv_address);
		gridViewWidth = DensityUtil.dip2px(UploadActivity.this, 29);
		Util.setupUI(findViewById(R.id.ll_superviseUpload), UploadActivity.this);
		Init();
	}

	@Override
	protected void initialized() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			topicId=bundle.getString("topicId");//在参与讨论某个话题
			et_topic.setVisibility(View.GONE);
		}else{
			et_topic.setVisibility(View.VISIBLE);
		}
		userId = SharedPreferencesUtil.getSharePreStr(getContext(), "userInfo", "userId");
		// 初始化定位
		mLocationClient = ((BaseApplication) getApplication()).mLocationClient;
		initLocation();
		mLocationClient.start();
		//((BaseApplication) getApplication()).mLocationResult = tv_address;
		// 向服务器上传数据参数
		waitDialog = DialogUtil.waitDialog(UploadActivity.this, "上传中...");
		// android 如何让dialog不消失，即使是用户按了返回键dialog也不消失
		waitDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dailog, int keycode, KeyEvent event) {
				if (keycode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 初始化定位的相关参数
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(2000);// 设置发起定位请求的间隔时间为2000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_publish_back:// 返回键
			DeleteFile(PHOTO_DIR);
			Bimp.tempSelectBitmap.clear();
			finish();
			break;
		case R.id.btn_publish:// 上传相关内容(图片和文字)
			memory = et_describe.getText().toString();
			Bundle bundle = getIntent().getExtras();
			if(bundle!=null){
				{
					if(ValueUtil.isStrNotEmpty(memory)){
						if(memory.length()>0&&memory.length()<150){
							if (NetWorkUtil.isNetworkConnected(getContext())) {
								waitDialog.show();
								AddMemory(userId,topicId,memory);
							} else {
								ToastUtil.ToastMessage(getContext(), "网络异常,请检查网络设置!");
							}
						}else{
							ToastUtil.ToastMessage(getContext(), "描述的内容不能超过150字！"); 	  
						}
					}else{
						ToastUtil.ToastMessage(getContext(), "请输入你想说的话！"); 
					}
				}
				
			}else{
				title = et_topic.getText().toString();
				if (ValueUtil.isStrNotEmpty(title)) {
					if(title.length()>0&&title.length()<30){
						if(ValueUtil.isStrNotEmpty(memory)){
							if(memory.length()>0&&memory.length()<50){
								if (NetWorkUtil.isNetworkConnected(getContext())) {
									waitDialog.show();
									AddTopic(userId, title);
								} else {
									ToastUtil.ToastMessage(getContext(), "网络异常,请检查网络设置!");
								}
							}else{
								ToastUtil.ToastMessage(getContext(), "描述的内容不能超过50字！"); 	  
							}
						}else{
							ToastUtil.ToastMessage(getContext(), "请输入你想说的话！"); 
						}
					}else{
						ToastUtil.ToastMessage(getContext(), "话题字数不能超过30字！"); 
					}

				} else {
					ToastUtil.ToastMessage(getContext(), "请输入话题！");
				}
			}

		
		
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化拍照和相册选择照片功能
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void Init() {
		pop = new PopupWindow(UploadActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		//pop.setAnimationStyle(R.style.PopupAnimation);
		pop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 拍照按钮的监听
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				doTakePhoto();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 照片选择的监听,跳到AlbumActivity选择照片
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(UploadActivity.this, AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);// 切换动画效果
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 取消按钮的监听
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					// 取消隐藏键盘
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 隐藏软键盘
					imm.hideSoftInputFromWindow(et_describe.getWindowToken(), 0);
					ll_popup.startAnimation(AnimationUtils.loadAnimation(UploadActivity.this, R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(UploadActivity.this, GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}
	/**
	 * 调用相机拍照功能
	 */
	protected void doTakePhoto() {
		try {
			if (!PHOTO_DIR.exists()) {
				@SuppressWarnings("unused")
				boolean iscreat = PHOTO_DIR.mkdirs();// 创建照片的存储目录
			}
			FileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, FileName);
			final Intent intent = getTakePickIntent(mCurrentPhotoFile);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(this, "photoPickerNotFoundText", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getTakePickIntent(File f) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
		case CAMERA_WITH_DATA:
			//只上传一张照片
			if (Bimp.tempSelectBitmap.size() <1 && resultCode == RESULT_OK) {
				File f = new File(PHOTO_DIR, FileName);
				if (f.exists()) {
					// bm = BitmapFactory.decodeFile(f.getAbsolutePath());
					try {
						bmp = Bimp.revitionImageSize(f.getAbsolutePath());// 压缩
						Bitmap bitmap1 = createImageBitmap(bmp); // 水印
						ImageItem takePhoto = new ImageItem();
						takePhoto.setName(FileName);// 文件图片路径
						takePhoto.setBitmap(bitmap1);
						Bimp.tempSelectBitmap.add(takePhoto);
						FileUtils.saveBitmap(bitmap1, FileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
					return;
				}

			}
			break;
		}
	}
	/**
	 * 给图片添加水印
	 */
	private Bitmap createImageBitmap(Bitmap src) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());// 设置日期格式
		String phototime = df.format(new Date());
		int w = src.getWidth();
		int h = src.getHeight();
		String mstrTitle = phototime;
		Bitmap bmpTemp = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmpTemp);
		Paint p = new Paint();
		String familyName = "楷体";
		Typeface font = Typeface.create(familyName, Typeface.NORMAL);
		p.setColor(Color.RED);
		p.setTypeface(font);
		p.setTextSize(20);
		canvas.drawBitmap(src, 0, 0, p);
		p.setAlpha(100);
		canvas.drawText(mstrTitle, 0, 20, p);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return bmpTemp;
	}
	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			// loading();
			adapter.notifyDataSetChanged();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 1) {
				return 1;
			}
			//return (Bimp.tempSelectBitmap.size() + 1);
			return (Bimp.tempSelectBitmap.size()+1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grider_image);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((BaseApplication.width - gridViewWidth) / 4, (BaseApplication.width - gridViewWidth) / 4);
				holder.image.setLayoutParams(params);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 2) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());// 获取图片数组里的某个人图片
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				case 2:
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}


	/**
	 * 新建记忆
	 * @param $token
	 * @param $userId 用户ID
	 * @param $topicId 话题ID
	 * @param $memory 记忆内容
	 * @param $uploadImg 话题图片
	 * @return 成功返回话题ID
	 */
	private void AddMemory(String userId,final String topicId,final String memory){

		Api.AddMemory(userId,topicId,memory,new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				// TODO Auto-generated method stub
				waitDialog.dismiss();
				if (result.isValidate()) {
					//StatusBean bean = StatusBean.parse(result.getMsg());
					//bean.setContent(memory);
					//bean.setTitle(title);
					//bundle.putSerializable("data", bean);
					Bimp.tempSelectBitmap.clear();// 清空lists
					// bimap.recycle();// 清空缓存，防止OOM
					DeleteFile(PHOTO_DIR);
					Bundle bundle = new Bundle();
					bundle.putString("topicId",topicId);
					bundle.putString("title",title);
					startActivity(TopicStatusesActivity.class, bundle);
					finish();
				} else {
					waitDialog.dismiss();
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				// TODO Auto-generated method stub
				waitDialog.show();
			}

			@Override
			public void fail(String error) {
				// TODO Auto-generated method stub
				waitDialog.dismiss();
			}
		});


	}
	/**
	 * 新建话题
	 * @param $token
	 * @param $userId 用户ID
	 * @param $topic 话题标题
	 * @return 成功返回话题ID
	 */
	private void AddTopic(final String userId,String topic){
		Api.AddTopic(userId, topic, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				// TODO Auto-generated method stub
				if (result.isValidate()) {
					topicId = result.getMsg();
					//topicId=topicbean.getTopicId();
					System.out.println("是否获取了topicId"+topicId);
					AddMemory(userId,topicId,memory);
				}else{

				}
			}

			@Override
			public void loading() {
				// TODO Auto-generated method stub

			}

			@Override
			public void fail(String error) {
				// TODO Auto-generated method stub

			}
		});
	}
	/*private void AddTopic(String userId,String title,String content) {

		final HashMap<String, String> parms = new HashMap<String, String>();

		parms.put("token",Api.token);
		parms.put("userId", userId);
		final HashMap<String, String> topic= new HashMap<String, String>();
		topic.put("title", title);
		topic.put("content", content);
		parms.put("topic",new JSONObject(topic).toString() );

		final HashMap<String, File> files = new HashMap<String, File>();
		File file = null;
		int i = 1;
		for (ImageItem c : Bimp.tempSelectBitmap) {
			file = new File(FileUtils.SDPATH + c.getName());
			files.put("uploadImg" + i++, file);
			file = null;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Result result = Result.standardParse(VolleyUtils.post(ApiClient.hostName + "/AddTopic", parms, files));
					Message msg = new Message();
					msg.obj = result;
					msg.what = 2;
					mhandler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					mhandler.sendEmptyMessage(0);
					return;
				}
			}
		}).start();
	}*/

	/*	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 发表图片说说失败
				waitDialog.dismiss();
				ToastUtil.ToastMessage(getContext(), "上传失败，请重试");
				break;

			case 1:
				adapter.notifyDataSetChanged();
				break;

			case 2:// 发表图片说说成功
				Result result = (Result) msg.obj;
				waitDialog.dismiss();
				if (result.isValidate()) {
					StatusBean bean = StatusBean.parse(result.getMsg());
					bean.setContent(content);
					bean.setTitle(title);
					Bundle bundle = new Bundle();
					bundle.putSerializable("data", bean);
					Bimp.tempSelectBitmap.clear();// 清空lists
					// bimap.recycle();// 清空缓存，防止OOM
					DeleteFile(PHOTO_DIR);
					Intent intent = new Intent(UploadActivity.this, TopicStatusesActivity.class);
					intent.putExtra("title", title);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				} else {
					waitDialog.dismiss();
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
				break;
			}
		}
	};*/

	/** 删除文件夹 */
	public void DeleteFile(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Bimp.tempSelectBitmap.clear();
			DeleteFile(PHOTO_DIR);
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			finish();
		}
		return true;
	}

}
