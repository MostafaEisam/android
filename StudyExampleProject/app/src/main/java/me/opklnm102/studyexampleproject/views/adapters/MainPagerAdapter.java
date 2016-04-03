package me.opklnm102.studyexampleproject.views.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-04-01.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = MainPagerAdapter.class.getSimpleName();

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final ArrayList<String> mTitleList = new ArrayList<>();
    private final ArrayList<Integer> mIconList = new ArrayList<>();

//    private String tabTitles[] = new String[]{"Tab1", "Tab2", "Tab3"};

//    private int[] imageResId = {
//            R.mipmap.ic_launcher,
//            R.mipmap.ic_launcher,
//            R.mipmap.ic_launcher
//    };

    Context mContext;

    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        //글자만 보여줄 때
//        return tabTitles[position];

        //아이콘만 보여주고 싶을 때
//        return null;

        Log.d(TAG, " getPageTitle()");

        //아이콘과 글자 동시에 -> 아이콘이 안뜨네.. 그냥 tabLayout에 설정해서 해결결
       Drawable titleIcon = ContextCompat.getDrawable(mContext, mIconList.get(position));
        titleIcon.setBounds(0, 0, titleIcon.getMinimumWidth(), titleIcon.getMinimumHeight());
        SpannableString spannableString = new SpannableString(" " + mTitleList.get(position));
        ImageSpan imageSpan = new ImageSpan(titleIcon, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title, int resId) {
        mFragmentList.add(fragment);
        mTitleList.add(title);
        mIconList.add(resId);
    }
}
