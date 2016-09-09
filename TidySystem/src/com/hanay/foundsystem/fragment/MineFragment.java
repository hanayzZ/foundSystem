package com.hanay.foundsystem.fragment;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.activity.LoginActivity;
import com.hanay.foundsystem.activity.RegisterActivity;
import com.hanay.foundsystem.activity.SetActivity;
import com.hanay.foundsystem.activity.StatusesInfoActivity;
import com.hanay.foundsystem.activity.TopicStatusesActivity;
import com.hanay.foundsystem.adapter.StatusAdapter;
import com.hanay.foundsystem.adapter.TopicAdapter;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.base.BaseFragment;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.bean.StatusBean;
import com.hanay.foundsystem.bean.TopicBean;
import com.hanay.foundsystem.bean.UserBean;
import com.hanay.foundsystem.imageutils.Bimp;
import com.hanay.foundsystem.imageutils.FileUtils;
import com.hanay.foundsystem.imageutils.ImageItem;
import com.hanay.foundsystem.util.DialogUtil;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.NetWorkUtil;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ToastUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


/**
 * @author 李海红
 * @version 创建时间：2014-12-23
 * @description 导航标签(我的)
 */

public class MineFragment extends BaseFragment implements OnClickListener {
	/**头像*/
	private ImageView head;
	/**设置*/
	private TextView tv_set;
	/**用户名*/
	private TextView  tv_username;
	/**手机号*/
	private TextView  tv_phone;
	private TextView tv_call;
	/**状态、话题、收藏*/
	private RadioGroup rg_supervise;
	private RadioButton rb_publish_mine,rb_topic,rb_fav;

	/**未登陆*/
	LinearLayout ll_title;
	LinearLayout ll_out;
	Button btn_register;
	Button btn_login;

	/**状态*/
	private PullToRefreshListView lv_mine_info;
	private StatusBean statusbean;
	private List<StatusBean> mineinfolists;
	private StatusAdapter groupInfoAdapter;

	/**话题*/
	private PullToRefreshListView lv_topic;
	private TopicBean topicBean;
	private List<TopicBean> topiclists;
	private TopicAdapter topicAdapter;

	/**收藏*/
	private PullToRefreshListView lv_fav;
	private StatusBean favBean;
	private List<StatusBean> favlists;
	private StatusAdapter favAdapter;

	/**用户bean*/
	private UserBean userBean=new UserBean();
	/**昵称*/
	private String username;
	/**手机号*/
	private String phone;
	/** * page 当前页码*/
	private int page = 0;
	private String userId=null;
	private static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/Photo_LJ/");
	/** 照相机拍照得到的图片 */
	private File mCurrentPhotoFile;
	private String FileName;
	/** 等待框 */
	private Dialog waitDialog;
	/** 加载等待 */
	private ProgressBar progressBar;
	/** * 暂无发布的状态*/
	private TextView noSatus;
	
	/** * 暂无发布的话题*/
	private TextView noTopic;
	
	/** * 暂无收藏的状态*/
	private TextView noFav;
	
	/**提示*/
	private TextView tv_tip;
	private Intent dataIntent = null;
	private Handler handler = new Handler();

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void setupViews(View view) {
		ll_title= (LinearLayout) view.findViewById(R.id.ll_title);
		tv_tip=(TextView) view.findViewById(R.id.tv_tip);
		ll_out= (LinearLayout) view.findViewById(R.id.ll_out);
		btn_register=(Button) view.findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		btn_login=(Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		head= (ImageView) view.findViewById(R.id.igb_head);
		head.setOnClickListener(this);
		tv_set=(TextView) view.findViewById(R.id.tv_set);
		tv_set.setOnClickListener(this);
		tv_username=(TextView) view.findViewById(R.id.tv_username);
		tv_phone=(TextView) view.findViewById(R.id.tv_phone);
		tv_call=(TextView) view.findViewById(R.id.tv_call);
		tv_call.setOnClickListener(this);
		rg_supervise = (RadioGroup) view.findViewById(R.id.rg_supervise);
		//状态
		rb_publish_mine = (RadioButton) view.findViewById(R.id.rb_publish_mine);
		lv_mine_info=(PullToRefreshListView) view.findViewById(R.id.lv_mine_info);
		//话题
		rb_topic = (RadioButton) view.findViewById(R.id.rb_topic);
		lv_topic=(PullToRefreshListView) view.findViewById(R.id.lv_topic);
		//收藏
		rb_fav = (RadioButton) view.findViewById(R.id.rb_fav);
		lv_fav=(PullToRefreshListView) view.findViewById(R.id.lv_fav);
		/** 进度条 */
		progressBar = (ProgressBar) view.findViewById(R.id.pb_mine);
		noSatus = (TextView)view.findViewById(R.id.noSatus);
		noTopic= (TextView)view.findViewById(R.id.noTopic);
		noFav= (TextView)view.findViewById(R.id.noFav);
	}

	@Override
	protected void initialized() {
		waitDialog = DialogUtil.waitDialog(getActivity(), "上传中...");
		waitDialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return true;
				}
				return false;
			}
		});
		userId = SharedPreferencesUtil.getSharePreStr(getActivity(), "userInfo", "pusherId");
		if (ValueUtil.isStrEmpty(userId)) {
			userId = SharedPreferencesUtil.getSharePreStr(getActivity(), "userInfo", "userId");
			//进入个人中心
			if(ValueUtil.isStrNotEmpty(userId)){
				tv_set.setVisibility(View.VISIBLE);
				tv_tip.setVisibility(View.GONE);
				ll_out.setVisibility(View.GONE);
				ll_title.setVisibility(View.VISIBLE);
				tv_username.setVisibility(View.VISIBLE);
				tv_phone.setVisibility(View.VISIBLE);
				tv_call.setVisibility(View.VISIBLE);
				HomePage(userId);//获取个人资料
				rg_supervise.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_publish_mine:
							rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.lightgreen));
							rb_topic.setBackgroundColor(getResources().getColor(R.color.white));
							rb_fav.setBackgroundColor(getResources().getColor(R.color.white));
							lv_mine_info.setVisibility(View.VISIBLE);
							lv_topic.setVisibility(View.GONE);
							lv_fav.setVisibility(View.GONE);
							if (ValueUtil.isListEmpty(mineinfolists)) {
								progressBar.setVisibility(View.VISIBLE);
								GetMyMemory("list", userId,"0",page);
							} else {
								groupInfoAdapter = new StatusAdapter(getActivity(), mineinfolists);
								lv_mine_info.setAdapter(groupInfoAdapter);
							}
							break;
						case R.id.rb_topic:
							rb_topic.setBackgroundColor(getResources().getColor(R.color.lightgreen));
							rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.white));
							rb_fav.setBackgroundColor(getResources().getColor(R.color.white));
							lv_topic.setVisibility(View.VISIBLE);
							lv_mine_info.setVisibility(View.GONE);
							lv_fav.setVisibility(View.GONE);
							if (ValueUtil.isListEmpty(topiclists)) {
								progressBar.setVisibility(View.VISIBLE);
								getTopic("list", userId, page);
							} else {
								topicAdapter = new TopicAdapter(getActivity(), topiclists);
								lv_topic.setAdapter(topicAdapter);
							}
							break;
						case R.id.rb_fav:
							rb_fav.setBackgroundColor(getResources().getColor(R.color.lightgreen));
							rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.white));
							rb_topic.setBackgroundColor(getResources().getColor(R.color.white));
							lv_fav.setVisibility(View.VISIBLE);
							lv_mine_info.setVisibility(View.GONE);
							lv_topic.setVisibility(View.GONE);

							if (ValueUtil.isListEmpty(favlists)) {
								progressBar.setVisibility(View.VISIBLE);
								GetMyMemory("list", userId,"1",page);
							} else {
								favAdapter = new StatusAdapter(getActivity(), favlists);
								lv_fav.setAdapter(favAdapter);
							}
							break;
						}
					}
				});
				lv_mine_info.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						final StatusBean bean = (StatusBean) parent.getItemAtPosition(position);
						Bundle bundle = new Bundle();
						bundle.putString("memoryId",bean.getMemoryId());
						//bundle.putSerializable("statuslist", bean);
						startActivity(StatusesInfoActivity.class, bundle);
					}
				});
				//我的状态刷新
				lv_mine_info.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						if (lv_mine_info.isHeaderShown()) {// 下拉刷新
							page = 0;
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									GetMyMemory("list", userId,"0",page);
								}
							}, 1000);
						} else if (lv_mine_info.isFooterShown()) {// 上拉加载
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									//page++;
									GetMyMemory("list", userId,"0",page);
								}
							}, 1000);
						}
					}
				});
				//话题的点击
				lv_topic.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						final TopicBean event = (TopicBean) parent.getItemAtPosition(position);
						Bundle bundle = new Bundle();
						bundle.putString("topicId",event.getTopicId());
						//bundle.putString("title", event.getTitle());
						startActivity(TopicStatusesActivity.class, bundle);
					}
				});
				//话题的刷新
				lv_topic.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						if (lv_topic.isHeaderShown()) {// 下拉刷新
							page = 0;
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									getTopic("list", userId, page);
								}
							}, 1000);
						} else if (lv_topic.isFooterShown()) {// 上拉加载
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									//page++;
									getTopic("list", userId, page);
								}
							}, 1000);
						}
					}
				});
				//收藏的点击
				lv_fav.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						final StatusBean bean = (StatusBean) parent.getItemAtPosition(position);
						Bundle bundle = new Bundle();
						bundle.putString("memoryId",bean.getMemoryId());
						//bundle.putSerializable("statuslist", bean);
						startActivity(StatusesInfoActivity.class, bundle);
					}
				});
				//收藏的刷新
				lv_fav.setOnRefreshListener(new OnRefreshListener<ListView>() {

					@Override
					public void onRefresh(PullToRefreshBase<ListView> refreshView) {
						if (lv_fav.isHeaderShown()) {// 下拉刷新
							page= 0;
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									GetMyMemory("list", userId,"1",page);
								}
							}, 1000);
						} else if (lv_fav.isFooterShown()) {// 上拉加载
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									//page++;
									GetMyMemory("list", userId,"1",page);
								}
							}, 1000);
						}
					}
				});

				rb_publish_mine.setChecked(true);
			}else{
				tv_set.setVisibility(View.GONE);
				ll_out.setVisibility(View.VISIBLE);
				ll_title.setVisibility(View.GONE);
			}

		}else{
			//进入其他人个人中心
			userId = SharedPreferencesUtil.getSharePreStr(getActivity(), "userInfo", "pusherId");
			SharedPreferencesUtil.putSharePre(getActivity(), "userInfo", "pusherId","");
			System.out.println("进入过其他的个人中心后pusherId"+SharedPreferencesUtil.getSharePreStr(getActivity(), "userInfo", "pusherId"));
			tv_tip.setVisibility(View.GONE);
			ll_out.setVisibility(View.GONE);
			ll_title.setVisibility(View.VISIBLE);
			tv_username.setVisibility(View.VISIBLE);
			tv_phone.setVisibility(View.VISIBLE);
			tv_call.setVisibility(View.VISIBLE);
			HomePage(userId);//获取个人资料
			rg_supervise.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					switch (checkedId) {
					case R.id.rb_publish_mine:
						rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.lightgreen));
						rb_topic.setBackgroundColor(getResources().getColor(R.color.white));
						rb_fav.setBackgroundColor(getResources().getColor(R.color.white));
						lv_mine_info.setVisibility(View.VISIBLE);
						lv_topic.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						if (ValueUtil.isListEmpty(mineinfolists)) {
							progressBar.setVisibility(View.VISIBLE);
							GetMyMemory("list", userId,"0",page);
						} else {
							groupInfoAdapter = new StatusAdapter(getActivity(), mineinfolists);
							lv_mine_info.setAdapter(groupInfoAdapter);
						}
						break;
					case R.id.rb_topic:
						rb_topic.setBackgroundColor(getResources().getColor(R.color.lightgreen));
						rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.white));
						rb_fav.setBackgroundColor(getResources().getColor(R.color.white));
						lv_topic.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						if (ValueUtil.isListEmpty(topiclists)) {
							progressBar.setVisibility(View.VISIBLE);
							getTopic("list", userId, page);
						} else {
							topicAdapter = new TopicAdapter(getActivity(), topiclists);
							lv_topic.setAdapter(topicAdapter);
						}
						break;
					case R.id.rb_fav:
						rb_fav.setBackgroundColor(getResources().getColor(R.color.lightgreen));
						rb_publish_mine.setBackgroundColor(getResources().getColor(R.color.white));
						rb_topic.setBackgroundColor(getResources().getColor(R.color.white));
						lv_fav.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_topic.setVisibility(View.GONE);

						if (ValueUtil.isListEmpty(favlists)) {
							progressBar.setVisibility(View.VISIBLE);
							GetMyMemory("list", userId,"1",page);
						} else {
							favAdapter = new StatusAdapter(getActivity(), favlists);
							lv_fav.setAdapter(favAdapter);
						}
						break;
					}
				}
			});
			lv_mine_info.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final StatusBean bean = (StatusBean) parent.getItemAtPosition(position);
					Bundle bundle = new Bundle();
					bundle.putString("memoryId",bean.getMemoryId());
					//bundle.putSerializable("statuslist", bean);
					startActivity(StatusesInfoActivity.class, bundle);
				}
			});
			//我的状态刷新
			lv_mine_info.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (lv_mine_info.isHeaderShown()) {// 下拉刷新
						page = 0;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								GetMyMemory("list", userId,"0",page);
							}
						}, 1000);
					} else if (lv_mine_info.isFooterShown()) {// 上拉加载
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								//page++;
								GetMyMemory("list", userId,"0",page);
							}
						}, 1000);
					}
				}
			});
			//话题的点击
			lv_topic.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final TopicBean event = (TopicBean) parent.getItemAtPosition(position);
					Bundle bundle = new Bundle();
					bundle.putString("topicId",event.getTopicId());
					//bundle.putString("title", event.getTitle());
					startActivity(TopicStatusesActivity.class, bundle);
				}
			});
			//话题的刷新
			lv_topic.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (lv_topic.isHeaderShown()) {// 下拉刷新
						page = 0;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getTopic("list", userId, page);
							}
						}, 1000);
					} else if (lv_topic.isFooterShown()) {// 上拉加载
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								//page++;
								getTopic("list", userId, page);
							}
						}, 1000);
					}
				}
			});
			//收藏的点击
			lv_fav.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					final StatusBean bean = (StatusBean) parent.getItemAtPosition(position);
					Bundle bundle = new Bundle();
					bundle.putString("memoryId",bean.getMemoryId());
					//bundle.putSerializable("statuslist", bean);
					startActivity(StatusesInfoActivity.class, bundle);
				}
			});
			//收藏的刷新
			lv_fav.setOnRefreshListener(new OnRefreshListener<ListView>() {

				@Override
				public void onRefresh(PullToRefreshBase<ListView> refreshView) {
					if (lv_fav.isHeaderShown()) {// 下拉刷新
						page= 0;
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								GetMyMemory("list", userId,"1",page);
							}
						}, 1000);
					} else if (lv_fav.isFooterShown()) {// 上拉加载
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								//page++;
								GetMyMemory("list", userId,"1",page);
							}
						}, 1000);
					}
				}
			});

			rb_publish_mine.setChecked(true);
			
			
		}


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register://注册
			startActivity(RegisterActivity.class);
			break;
		case R.id.btn_login://登陆
			startActivity(LoginActivity.class);
			break;
		case R.id.tv_set://设置
			startActivity(SetActivity.class);
			break;
		case R.id.igb_head://换头像
			ShowPickDialog();
			break;
		case R.id.tv_call://拨号联系
			Intent intent=new Intent();
			Uri uri = Uri.parse("tel:"+phone);
			intent = new Intent(Intent.ACTION_CALL,uri);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	/**设置头像*/
	private void ShowPickDialog() {

		new AlertDialog.Builder(getActivity())
		.setTitle("设置头像...")
		.setNegativeButton("相册", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				FileName=System.currentTimeMillis() + ".jpg";
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
				startActivityForResult(intent, 1);
			}
		})
		.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				FileName=System.currentTimeMillis() + ".jpg";
				mCurrentPhotoFile= new File(PHOTO_DIR,FileName);

				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
						.fromFile(mCurrentPhotoFile));
				startActivityForResult(intent, 2);
			}
		}).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
			// 如果是调用相机拍照时
		case 2:
			startPhotoZoom(Uri.fromFile(mCurrentPhotoFile));
			break;
			// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话，
			 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只
			 * 在这个地方加下，大家可以根据不同情况在合适的
			 * 地方做判断处理类似情况
			 * 
			 */
			if(data != null){
				//dataIntent = data;
				setPicToView(data);
				if (NetWorkUtil.isNetworkConnected(getContext())) {
					waitDialog.show();
					NewAvatar(userId,FileName);
				} else {
					ToastUtil.ToastMessage(getContext(), "网络异常,请检查网络设置!");
				}

			}
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 裁剪图片方法实现
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Bitmap output = Bitmap.createBitmap(photo.getHeight(), photo.getWidth(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, photo.getHeight(), photo.getWidth());
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			canvas.drawARGB(0, 0, 0, 0);
			canvas.drawCircle(photo.getWidth()/2, photo.getHeight()/2, photo.getWidth()/2, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(photo, rect, rect, paint);
			photo = output;
			Drawable drawable = new BitmapDrawable(photo);
			head.setImageDrawable(drawable);
			head.invalidate();
			ImageItem takePhoto = new ImageItem();
			takePhoto.setName(FileName);// 文件图片路径
			takePhoto.setBitmap(photo);
			Bimp.tempSelectBitmap.add(takePhoto);
			FileUtils.saveBitmap(photo, FileName);
		}else{
			ToastUtil.ToastMessage(getActivity(), "文件不存在！");
		}
	}

	/**
	 * 修改头像
	 * @param $token
	 * @param $userId 用户ID
	 * @param $uploadImg 上传用户头像
	 */
	private void NewAvatar(String userId,String FileName){
		//	if(dataIntent != null){
		/*	Bundle extras = dataIntent.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");*/
		//ImageItem takePhoto = new ImageItem();
		//takePhoto.setName(FileName);// 文件图片路径
		//takePhoto.setBitmap(photo);
		//Bimp.tempSelectBitmap.add(takePhoto);
		/*		FileUtils.saveBitmap(photo, FileName);*/
		/**
		 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
		 * 传到服务器，QQ头像上传采用的方法跟这个类似
		 */
		//	Drawable drawable = new BitmapDrawable(photo);
		//	ByteArrayOutputStream stream = new ByteArrayOutputStream();
		//	photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
		//	byte[] bytes = stream.toByteArray();
		//	final String picStr = new String(Base64Coder.encodeLines(bytes));
		/*final HashMap<String, String> parms = new HashMap<String, String>();
				parms.put("token",Api.token);
				parms.put("userId", userId);
				final HashMap<String, File> files = new HashMap<String, File>();
				File file =new File(FileUtils.SDPATH +FileName ) ;
				files.put("uploadImg" , file);*/
		/*	final HashMap<String, File> files = new HashMap<String, File>();
				File file = null;
				int i = 1;
				for (ImageItem c : Bimp.tempSelectBitmap) {
					file = new File(FileUtils.SDPATH + c.getName());
					files.put("uploadImg" + i++, file);
					file = null;
				}*/
		/*		new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Result result = Result.standardParse(VolleyUtils.post(ApiClient.hostName + "/NewAvatar", parms, files));
							Message msg = new Message();
							msg.obj = result;
							msg.what = 1;
							mhandler.sendMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
							mhandler.sendEmptyMessage(0);
							return;
						}
					}
				}).start();*/

		/*}else {
				Toast.makeText(getActivity(), "图片不存在", Toast.LENGTH_SHORT).show();	
			}*/
		//	}

		Api.NewAvatar(userId,FileName,new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				// TODO Auto-generated method stub
				if (result.isValidate()){
					waitDialog.dismiss();
					//ToastUtil.ToastMessage(getContext(), result.getMsg());
				}else{
					//ToastUtil.ToastMessage(getContext(), result.getMsg());
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
				ToastUtil.ToastMessage(getContext(),error);
			}
		});


	}

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
	/**
	 * 个人主页
	 * @param $token
	 * @param $userId
	 * @return 个人信息
	 */
	private void HomePage(String userId){
		Api.HomePage(userId, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					progressBar.setVisibility(View.GONE);
					userBean = UserBean.parse(result.getMsg());
					ImageCacheUitl.displayNormalImageView(head,userBean.getAvatar(), R.id.igb_head);
					tv_username.setText(	userBean.getUsername()+":  ");
					tv_phone.setText(userBean.getPhone());
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
			}

			@Override
			public void loading() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void fail(String error) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.GONE);
			}
		});
	}
	/**
	 *获取我我的状态和我收藏的状态
	 */
	private void GetMyMemory(String type, String userId,final String userType,final int pagetype) {

		Api.GetMyMemory( type,userId,userType, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if(userType.equals("0")){
					if(result.isValidate()){
						progressBar.setVisibility(View.GONE);
						//我发布的
						if (pagetype == 0) {// 第一页

							// 活动图片
							statusbean = StatusBean.parse(result.getMsg());
							mineinfolists = statusbean.getList();
							if (ValueUtil.isListNotEmpty(mineinfolists)) {
								groupInfoAdapter = new StatusAdapter(getActivity(),mineinfolists);
								lv_mine_info.setAdapter(groupInfoAdapter);
								//page++;
							} else {

							}
						} else {
							// 多页内容
							statusbean = StatusBean.parse(result.getMsg());
							mineinfolists.addAll(statusbean.getList());
							groupInfoAdapter.notifyDataSetChanged();
							//page++;
						}
						lv_mine_info.onRefreshComplete();
					}else{
						progressBar.setVisibility(View.GONE);
						noSatus.setVisibility(View.VISIBLE);
						noTopic.setVisibility(View.GONE);
						noFav.setVisibility(View.GONE);
						
					}
				
				}else if(userType.equals("1")){
					if(result.isValidate()){
						progressBar.setVisibility(View.GONE);
						//收藏的
						if (pagetype == 0) {// 第一页
							// 活动图片
							favBean = StatusBean.parse(result.getMsg());
							favlists = favBean.getList();
							if (ValueUtil.isListNotEmpty(favlists)) {
								favAdapter = new StatusAdapter(getActivity(),favlists);
								lv_fav.setAdapter(favAdapter);
								//page++;
							} else {

							}
						} else {
							// 多页内容
							favBean = StatusBean.parse(result.getMsg());
							favlists.addAll(favBean.getList());
							favAdapter.notifyDataSetChanged();
							//page++;
						}
						lv_fav.onRefreshComplete();
					}else{
						progressBar.setVisibility(View.GONE);
						noSatus.setVisibility(View.GONE);
						noTopic.setVisibility(View.GONE);
						noFav.setVisibility(View.VISIBLE);
					}
				}else{
					
				}
			
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				if(userType.equals("0")){
					lv_mine_info.onRefreshComplete();
				}else{
					lv_fav.onRefreshComplete();
				}
			
				if(rb_publish_mine.isChecked()){
					if(ValueUtil.isListNotEmpty(mineinfolists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noSatus.setVisibility(View.VISIBLE);
						lv_topic.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}/*else if(rb_topic.isChecked()){
					if(ValueUtil.isListNotEmpty(topiclists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noTopic.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}*/else if(rb_fav.isChecked()){
					if(ValueUtil.isListNotEmpty(favlists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noFav.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_topic.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}
			}

			@Override
			public void loading() {
				//progressBar.setVisibility(View.VISIBLE);
			}
		});

	}
	/*	Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 上传失败
				waitDialog.dismiss();
				ToastUtil.ToastMessage(getContext(), "上传失败，请重试");
				break;
			case 1:// 上传成功
				waitDialog.dismiss();
				Result result = (Result) msg.obj;
				if (result.isValidate()) {
					//Bimp.tempSelectBitmap.clear();// 清空lists
					DeleteFile(PHOTO_DIR);
				} else {
					ToastUtil.ToastMessage(getContext(), result.getMsg());
				}
				break;
			}
		}
	};
	 */
	/**
	 * 向服务器请求我的话题
	 * 传'list'获取Topic列表
	 * listType传'数字'则获取用户ID为这一数字的某个具体用户所发布的topic
	 */
	private void getTopic(final String type, String listType, final int pagetype) {
		Api.getTopic(type, listType, pagetype, new ApiCallBack() {

			@Override
			public void succeed(Result result) {

				if (result.isValidate()) {
					if (pagetype == 0) {// 第一页
						progressBar.setVisibility(View.GONE);
						// 活动图片
						topicBean = TopicBean.parse(result.getMsg());
						topiclists = topicBean.getLists();
						if (ValueUtil.isListNotEmpty(topiclists)) {
							topicAdapter = new TopicAdapter(getActivity(), topiclists);
							lv_topic.setAdapter(topicAdapter);
							// page++;
						} else {
							//lv_topic.setEmptyView(tv_noData);
						}
					} else {
						// 多页内容
						topicBean = TopicBean.parse(result.getMsg());
						topiclists.addAll(topicBean.getLists());
						topicAdapter.notifyDataSetChanged();
						//page++;
					}
					lv_topic.onRefreshComplete();

				} else {
					progressBar.setVisibility(View.GONE);
					noTopic.setVisibility(View.VISIBLE);
					noSatus.setVisibility(View.GONE);
					//lv_topic.setVisibility(View.GONE);
					//lv_fav.setVisibility(View.GONE);
				}
			}

			@Override
			public void loading() {
				//progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void fail(String error) {
				progressBar.setVisibility(View.GONE);
				lv_topic.onRefreshComplete();
				if(rb_topic.isChecked()){
					if(ValueUtil.isListNotEmpty(topiclists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noTopic.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}
				/*if(rb_publish_mine.isChecked()){
					if(ValueUtil.isListNotEmpty(mineinfolists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noSatus.setVisibility(View.VISIBLE);
						lv_topic.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}else if(rb_topic.isChecked()){
					if(ValueUtil.isListNotEmpty(topiclists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noTopic.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_fav.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}else if(rb_fav.isChecked()){
					if(ValueUtil.isListNotEmpty(favlists)){
						ToastUtil.ToastMessage(getContext(), error);
					}else{
						//noFav.setVisibility(View.VISIBLE);
						lv_mine_info.setVisibility(View.GONE);
						lv_topic.setVisibility(View.GONE);
						ToastUtil.ToastMessage(getContext(), error);
					}
				}
			*/}
		});
	}

}
