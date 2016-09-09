package com.hanay.foundsystem.service;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.api.Api;
import com.hanay.foundsystem.api.ApiCallBack;
import com.hanay.foundsystem.bean.NotificationContentBean;
import com.hanay.foundsystem.bean.Result;
import com.hanay.foundsystem.util.SharedPreferencesUtil;

/**
 * @author 李海红
 * @version 创建时间：2015-1-12
 * @description 后台轮询并发出通知
 */

public class NotificationService extends Service {
	private String userId;
	private Handler mHandler;
	private boolean mRunning = false;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		userId = SharedPreferencesUtil.getSharePreStr(this, "userInfo", "userId");

		mRunning = true;
		HandlerThread thread = new HandlerThread("MyHandlerThread");
		thread.start();// 创建一个HandlerThread并启动它
		mHandler = new Handler(thread.getLooper());// 使用HandlerThread的looper对象创建Handler,如果使用默认的构造方法,很有可能阻塞UI线程
		mHandler.post(mBackgroundRunnable);// 将线程post到Handler中
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mRunning = false;
		mHandler.removeCallbacks(mBackgroundRunnable);// 销毁线程
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancelAll();
		super.onDestroy();
	}

	// 实现耗时操作的线程
	Runnable mBackgroundRunnable = new Runnable() {
		@Override
		public void run() {
			while (mRunning) {
				poll();
			}
		}
	};

	/**
	 * 轮询获取推送消息
	 */
	private void poll() {
		getNotificationContent(userId);
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取通知消息
	 * @param userId
	 */
	private void getNotificationContent(String userId) {
		Api.PushMsg(userId, new ApiCallBack() {

			@Override
			public void succeed(Result result) {
				if (result.isValidate()) {
					List<NotificationContentBean> list = NotificationContentBean.parse(result.getMsg());
					for (int i = 0; i < list.size(); i++) {
						notification(i, list.get(i).getMemory_id(),list.get(i).getMemory_id());
					}
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

	/**
	 * 发送通知
	 * 
	 * @param id
	 * @param content
	 */
	private void notification(int id,String memory_id,String count) {
		NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this);
		notifyBuilder.setContentTitle("消息");
		notifyBuilder.setContentText(count);
		notifyBuilder.setSmallIcon(R.drawable.ic_launcher);
		notifyBuilder.setWhen(System.currentTimeMillis());
		// 闪现一段提示语，用来提醒用户
		notifyBuilder.setTicker("你有新的评论了！");
		// 将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
		notifyBuilder.setAutoCancel(true);
		// 设置notification的优先级，优先级越高的，通知排的越靠前
		notifyBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
		
		
		//推送给用户
		Intent intent =new Intent();
		intent.putExtra("mineFragment", "msgFragment");
		//intent.putExtra("memory_id", memory_id);
		intent.setClass(this, com.hanay.foundsystem.activity.MainActivity.class);
		
		
		// 当设置下面PendingIntent.FLAG_UPDATE_CURRENT这个参数的时候，常常使得点击通知栏没效果，你需要给notification设置一个独一无二的requestCode
		int requestCode = (int) SystemClock.uptimeMillis();
		PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notifyBuilder.setContentIntent(pendingIntent);

		Notification notification = notifyBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id, notification);
	}

}
