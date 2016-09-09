package com.hanay.foundsystem.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.hanay.foundsystem.R;
import com.hanay.foundsystem.base.BaseActivity;
import com.hanay.foundsystem.util.ImageCacheUitl;
import com.hanay.foundsystem.zoom.PhotoView;
import com.hanay.foundsystem.zoom.PhotoViewAttacher.OnViewTapListener;


/**
 * @author 李海红
 * @version 创建时间：2014-1-4
 * @description 点击放大图片 布局：item_grida_viewpage
 */
public class SimpleSampleActivity extends BaseActivity {

	/** 自定义可以放大缩小的view */
	private PhotoView photoView;

	/**ViewPager实现图片的切换*/
	private ViewPager mViewPager;
	private TestAdapter mAdapter;

	/**详情界面传来的图片地址litsts*/
	private List<String> pathlists=new ArrayList<String>();
	private Intent intent;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.item_grida_viewpage;
	}

	@Override
	protected void setupViews() {
		mViewPager =(ViewPager) findViewById(R.id.pager); 
	}

	@Override
	protected void initialized() {
		intent = getIntent();
		mAdapter = new TestAdapter(SimpleSampleActivity.this); 
		pathlists = (List<String>) intent.getExtras().getSerializable("data");
		mAdapter.change(pathlists);  
		mViewPager.setAdapter(mAdapter);  
		int id = intent.getIntExtra("ID", 0);
		mViewPager.setCurrentItem(id);
	}
	public class TestAdapter extends PagerAdapter {  

		private List<String> mPaths;  

		private Context mContext;  

		public TestAdapter(Context mContext) {  
			this.mContext = mContext;  
		}  

		public void change(List<String> paths) {  
			mPaths = paths;  
		}  

		@Override  
		public int getCount() {  
			return mPaths.size();  
		}  

		@Override  
		public boolean isViewFromObject(View view, Object obj) {  
			return view == (View) obj;  
		}  

		@Override  
		public Object instantiateItem (ViewGroup view, int position) { 
			photoView = new PhotoView(mContext);
			photoView.setBackgroundColor(0xff000000);
			photoView.setOnViewTapListener(new ViewTapListener());
			ImageCacheUitl.displayNormalImageView(photoView, mPaths.get(position), R.id.pager);
			((ViewPager)view).addView(photoView, 0);  
			return photoView;  
		}  

		@Override  
		public void destroyItem (ViewGroup view, int position, Object object) {  
			view.removeView((View)object);  
		}  
	}

	private class ViewTapListener implements OnViewTapListener{

		@Override
		public void onViewTap(View view, float x, float y) {
			finish();
		}

	}
}
