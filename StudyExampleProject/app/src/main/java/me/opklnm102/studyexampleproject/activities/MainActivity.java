package me.opklnm102.studyexampleproject.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import me.opklnm102.studyexampleproject.fragments.ContactsListFragment;
import me.opklnm102.studyexampleproject.views.adapters.MainPagerAdapter;
import me.opklnm102.studyexampleproject.R;

public class MainActivity extends AppCompatActivity {

    public static final String POSITION = "POSITION";

    Toolbar mToolbar;
    FloatingActionButton mfab;
    ViewPager mViewPager;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        setSupportActionBar(mToolbar);

        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);  //tabLayout에 viewPager를 연동
        setupTabIcons();
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mfab = (FloatingActionButton) findViewById(R.id.fab);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

    //tabMode="scrollable"을 시험하기위해 Fragment를 많이 생성
    public void setupViewPager() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab1", 0), "tab1", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab2", 1), "tab2", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab3", 2), "tab3", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab1", 0), "tab1", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab2", 1), "tab2", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab3", 2), "tab3", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab1", 0), "tab1", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab2", 1), "tab2", R.mipmap.ic_launcher);
        mainPagerAdapter.addFragment(ContactsListFragment.newInstance("tab3", 2), "tab3", R.mipmap.ic_launcher);
        mViewPager.setAdapter(mainPagerAdapter);
    }

    private int[] imageResId = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    //TabLayout에 Icon 달기
    public void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("One");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);

        mTabLayout.getTabAt(0).setCustomView(tabOne);  //CustomView 달기
        mTabLayout.getTabAt(1).setIcon(imageResId[1]);  //Icon만 달기
    }

    //이전에 선택되어 있던 탭 위치 복구
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
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
