package com.hanay.foundsystem.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author 李海红
 * @version 创建时间：2015-5-11
 * @description  广告滑动页的viewpage适配
 */

public class PageViewAdapter extends FragmentPagerAdapter {
	private List<Fragment> fragments = new ArrayList<Fragment>(); 
	public PageViewAdapter(FragmentManager fm) {
		super(fm);
	}

	public PageViewAdapter(FragmentManager fragmentManager, 
			ArrayList<Fragment> fragments){ 
		super(fragmentManager);
		this.fragments = fragments; 
	}

	@Override
	public Fragment getItem(int index) {
		return fragments.get(index); 
	}

	@Override
	public int getCount() {
		return fragments.size(); 
	}

}
