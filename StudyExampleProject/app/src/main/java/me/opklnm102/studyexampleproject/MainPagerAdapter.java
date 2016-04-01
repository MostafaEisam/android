package me.opklnm102.studyexampleproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

/**
 * Created by Administrator on 2016-04-01.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};
    private int[] imageResId = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    Context mContext;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];

        Drawable titleIcon = ContextCompat.getDrawable(mContext, imageResId[position]);
        titleIcon.setBounds(0, 0, titleIcon.getIntrinsicWidth(), titleIcon.getIntrinsicHeight());
        SpannableString spannableString = new SpannableString(" " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(titleIcon, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public Fragment getItem(int position) {
        return ContactsListFragment.newInstance(tabTitles[position], position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
