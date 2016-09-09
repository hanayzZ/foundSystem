package com.hanay.foundsystem.base;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.hanay.foundsystem.exception.AppException;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.util.SharedPreferencesUtil;
import com.hanay.foundsystem.util.ValueUtil;
import com.hanay.foundsystem.volley.VolleyUtils;


/**
 * @author 李海红
 * @version 创建时间：2014-12-13
 * @description 程序的入口,用于存放全局变量和公用的资源等
 */

public class BaseApplication extends Application {
	/**
	 * Activity集合
	 */
	private static ArrayList<BaseActivity> activitys = new ArrayList<BaseActivity>();
	/**
	 * App异常崩溃处理器
	 */
	private UncaughtExceptionHandler uncaughtExceptionHandler;
	/**
	 * 屏幕的宽高像素
	 */
	public static int width, height;
	public String token;

	public LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	public TextView mLocationResult;

	/**
	 * 判断网络是否连接
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null) {
			return ni.isAvailable();
		}
		return false;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		doOncreate();
	}

	private void doOncreate() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int w_screen = dm.widthPixels;
		int h_screen = dm.heightPixels;
		BaseApplication.width = w_screen;
		BaseApplication.height = h_screen;
		SharedPreferencesUtil.putSharePre(getApplicationContext(), "PhoneInfo", "width", w_screen);
		SharedPreferencesUtil.putSharePre(getApplicationContext(), "PhoneInfo", "height", h_screen);
		//SharedPreferencesUtil.putSharePre(getApplicationContext(), "Token", "token", "yb2f5L6qgpRZwQNNsiZYde8U9K3bqy4P");
        //初始化服务器
		VolleyUtils.initVolley(this.getApplicationContext());
		ImageCacheUitl.iniImageCache(this.getApplicationContext());
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		// 注册App异常崩溃处理器
//		Thread.setDefaultUncaughtExceptionHandler(getUncaughtExceptionHandler());
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			if (ValueUtil.isStrNotEmpty(location.getAddrStr())) {
				logMsg(location.getAddrStr());
				mLocationClient.stop();
			}
			Log.i("BaiduLocationApiDem", sb.toString());
		}
	}

	/**
	 * 显示定位地址
	 */
	public void logMsg(String address) {
		try {
			if (mLocationResult != null)
				mLocationResult.setText(address);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置 App异常崩溃处理器
	 * 
	 * @param uncaughtExceptionHandler
	 */
	public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
		this.uncaughtExceptionHandler = uncaughtExceptionHandler;
	}

	private UncaughtExceptionHandler getUncaughtExceptionHandler() {
		if (uncaughtExceptionHandler == null) {
			uncaughtExceptionHandler = AppException.getInstance(this);
		}
		return uncaughtExceptionHandler;
	}

	/**
	 * 添加Activity到ArrayList<Activity>管理集合
	 * 
	 * @param activity
	 */
	public void addActivity(BaseActivity activity) {
		String className = activity.getClass().getName();
		for (Activity at : activitys) {
			if (className.equals(at.getClass().getName())) {
				activitys.remove(at);
				break;
			}
		}
		activitys.add(activity);
	}

	/**
	 * 退出应用程序的时候,手动调用
	 */
	@Override
	public void onTerminate() {
		for (BaseActivity activity : activitys) {
			activity.defaultFinish();
		}
	}

}
