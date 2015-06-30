package com.example.dong.ewhakatalk_dongheekim;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.BaseFragment;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.ChatFragment;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.ContactFragment;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.FindFriendFragment;
import com.example.dong.ewhakatalk_dongheekim.tabfragments.MoreFragment;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private PagerSlidingTabStrip mIndicator;
    private ViewPager mViewPager;
    private ArrayList<BaseFragment> mFragments;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initialFragment();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                mFragments);

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mIndicator = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        mIndicator.setShouldExpand(true);
        mIndicator.setSmoothScrollingEnabled(true);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for(int i=0; i<mSectionsPagerAdapter.getCount(); i++){
                    mIndicator.getIconTab(i).setImageResource(mSectionsPagerAdapter.getPageIconResId(i));
                }

                mIndicator.getIconTab(position).setImageResource(mSectionsPagerAdapter.getPageActivatedResId(position));

                BaseFragment selectedFragment = (BaseFragment)mSectionsPagerAdapter.getItem(position);

                if(selectedFragment.getActionBarTitle() != null)
                    tvTitle.setText(selectedFragment.getActionBarTitle());
                else
                    tvTitle.setText(selectedFragment.getTitle());
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mIndicator.setViewPager(mViewPager);

        tvTitle.setText(mSectionsPagerAdapter.getPageTitle(0));
        mIndicator.getIconTab(0).setImageResource(mSectionsPagerAdapter.getPageActivatedResId(0));
    }

    private void initialFragment(){
        mFragments = new ArrayList<BaseFragment>();
        mFragments.add(ContactFragment.newInstance("친구",
                R.drawable.tab_friend_icon_p,
                R.drawable.tab_friend_icon_n));
        mFragments.add(ChatFragment.newInstance("채팅",
                R.drawable.tab_chatting_icon_p,
                R.drawable.tab_chatting_icon_n));
        mFragments.add(FindFriendFragment.newInstance("친구찾기",
                R.drawable.tab_recommend_icon_p,
                R.drawable.tab_recommend_icon_n));
        mFragments.add(MoreFragment.newInstance("더보기",
                R.drawable.tab_more_icon_p,
                R.drawable.tab_more_icon_n));
    }

    public BaseFragment getFragment(int index){
        BaseFragment newFragment = null;
        newFragment = (BaseFragment)mSectionsPagerAdapter.getItem(index);

        return newFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
