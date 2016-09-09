package com.hanay.foundsystem.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanay.foundsystem.R;
/**
 * @author 李海红
 * @version 创建时间：2015-5-7
 * @description 导航图
 */
public class Activity_HY extends Activity implements OnClickListener,OnPageChangeListener{

	private ViewPager mViewPager;

	private final int[] pics = {R.drawable.first,R.drawable.second,R.drawable.third};

	private ImageView[] mPoints;

	private int currentIndex = 0;

	private ImageView btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hy);

		initView();
		initPoint();
	}

	private void initView(){
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mViewPager.setAdapter(new ViewPagerAdapter());
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(currentIndex);
		btn = (ImageView)findViewById(R.id.hy_btn);
		btn.setOnClickListener(this);

	}
	private void initPoint(){
		LinearLayout linearLayout = (LinearLayout)findViewById(R.id.hy_layout);
		mPoints = new ImageView[pics.length];
		for(int i=0;i<pics.length;i++){
			mPoints[i] = (ImageView)linearLayout.getChildAt(i);
			mPoints[i].setEnabled(false);
			mPoints[i].setTag(i);
		}
		mPoints[currentIndex].setEnabled(true);
	}

	class ViewPagerAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return pics.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0==arg1);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			ImageView iv = new ImageView(Activity_HY.this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[position]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			container.addView(iv);
			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View)object;
			container.removeView(view);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		setpoints(arg0);
	}


	private void setpoints(int pos){
		if(pos<0 || pos>pics.length || currentIndex == pos)return;
		mPoints[pos].setEnabled(true);
		mPoints[currentIndex].setEnabled(false);
		currentIndex = pos;
	}

	@Override
	public void onClick(View arg0) {
		if(currentIndex == pics.length-1){
			Intent intent = new Intent(Activity_HY.this,MainActivity.class);
			startActivity(intent);
			finish();
		}


	}
}
