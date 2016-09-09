package com.hanay.foundsystem.zoom;

import android.annotation.TargetApi;
import android.view.View;
/**
 * @author 李海红
 * @version 创建时间：2014-12-19
 * @description
 */
@TargetApi(16)
public class SDK16 {

	public static void postOnAnimation(View view, Runnable r) {
		view.postOnAnimation(r);
	}
	
}
