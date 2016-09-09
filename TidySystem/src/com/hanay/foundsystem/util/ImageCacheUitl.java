package com.hanay.foundsystem.util;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.hanay.foundsystem.imageutils.FileUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


/**
 * @author
 * @version 创建时间：2014-12-13
 * @description 第三方universalimageloader.jar的相关使用
 */

public class ImageCacheUitl {
	private static DisplayImageOptions normaloptions;
	private static ImageLoader imgloader = null;

	public static ImageLoader getImgLoader() {
		return imgloader;
	}

	/**
	 * 初始化
	 */
	public static void iniImageCache(Context context) {
		/*
		 * roundoptions = new DisplayImageOptions.Builder().cacheInMemory(true)
		 * .cacheInMemory(true) .cacheOnDisc(true) .build();
		 */

		// .showImageForEmptyUri(R.drawable.icon) //设置图片Uri为空或是错误的时候显示的图片
		// .showImageOnFail(R.drawable.icon) //设置图片加载/解码过程中错误时候显示的图片
		// .cacheInMemory() //设置下载的图片是否缓存在内存中
		// .cacheOnDisc() //设置下载的图片是否缓存在SD卡中
		// .imageScaleType(ImageScaleType.EXACTLY) //设置图片缩放方式，缩放至目标大小
		// .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
		// .resetViewBeforeLoading() //设置图片在下载前是否重置
		// .displayer(new RoundedBitmapDisplayer(20)) //设置图片显示方式，是否设置为圆角，弧度为多少
		// //设置图片渐显时间 FadeInBitmapDisplayer(int durationMillis)
		// //正常显示�?��图片SimpleBitmapDisplayer()

		// File cacheDir = StorageUtils.getCacheDirectory(context);
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(context)
		// .memoryCacheExtraOptions(480, 800) // default = device screen
		// dimensions
		// .diskCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
		// .taskExecutor(...)
		// .taskExecutorForCachedImages(...)
		// .threadPoolSize(3) // default
		// .threadPriority(Thread.NORM_PRIORITY - 1) // default
		// .tasksProcessingOrder(QueueProcessingType.FIFO) // default
		// .denyCacheImageMultipleSizesInMemory()
		// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		// .memoryCacheSize(2 * 1024 * 1024)
		// .memoryCacheSizePercentage(13) // default
		// .diskCache(new UnlimitedDiscCache(cacheDir)) // default
		// .diskCacheSize(50 * 1024 * 1024)
		// .diskCacheFileCount(100)
		// .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) //
		// default
		// .imageDownloader(new BaseImageDownloader(context)) // default
		// .imageDecoder(new BaseImageDecoder()) // default
		// .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) //
		// default
		// .writeDebugLogs()
		// .build();

		// 显示图片的配置
		File cacheDir = new File(FileUtils.SDPATH + "picturecache/");
		normaloptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).discCache(new UnlimitedDiscCache(cacheDir)).writeDebugLogs().build();
		imgloader = ImageLoader.getInstance();
		imgloader.init(config);
	}

	/**
	 * 显示图片
	 * 
	 * @param igv
	 *            显示图片的ImageView组件
	 * @param url
	 *            图片的地址
	 * @param drawresid
	 *            默认显示的图片
	 */
	public static void displayNormalImageView(ImageView igv, String url, int drawresid) {
		if (url == null || url.trim().equals("null") || url.trim().equals("")) {
			ImageLoader.getInstance().displayImage("drawable://" + drawresid, igv, normaloptions);
		} else {
			ImageLoader.getInstance().displayImage(url, igv, normaloptions);
		}
	}

	/**
	 * Bitmap圆角处理
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

}
