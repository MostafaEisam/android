package com.example.dong.ewhakatalk_dongheekim;

import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.BaseFragment;

import java.util.ArrayList;

public class SectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter implements IconTabProvider {

	private ArrayList<BaseFragment> mFragments;

	public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<BaseFragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public BaseFragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mFragments.get(position).getTitle();
	}

	@Override
	public int getPageIconResId(int position) {
		return mFragments.get(position).getDefaultIcon();
	}

	@Override
	public int getPageActivatedResId(int position) {
		return mFragments.get(position).getActivatedIcon();
	}
}
