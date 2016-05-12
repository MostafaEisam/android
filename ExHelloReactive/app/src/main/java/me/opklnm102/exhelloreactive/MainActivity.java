package me.opklnm102.exhelloreactive;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.fragment.AndThenWhenExampleFragment;
import me.opklnm102.exhelloreactive.fragment.CombineLatestExampleFragment;
import me.opklnm102.exhelloreactive.fragment.DistinctExampleFragment;
import me.opklnm102.exhelloreactive.fragment.FilterExampleFragment;
import me.opklnm102.exhelloreactive.fragment.FirstExampleFragment;
import me.opklnm102.exhelloreactive.fragment.GroupByExampleFragment;
import me.opklnm102.exhelloreactive.fragment.JoinExampleFragment;
import me.opklnm102.exhelloreactive.fragment.LongTaskFragment;
import me.opklnm102.exhelloreactive.fragment.MapExampleFragment;
import me.opklnm102.exhelloreactive.fragment.MergeExampleFragment;
import me.opklnm102.exhelloreactive.fragment.NetworkTaskFragment;
import me.opklnm102.exhelloreactive.fragment.ScanExampleFragment;
import me.opklnm102.exhelloreactive.fragment.SecondExampleFragment;
import me.opklnm102.exhelloreactive.fragment.SharedPreferencesListExampleFragment;
import me.opklnm102.exhelloreactive.fragment.TakeExampleFragment;
import me.opklnm102.exhelloreactive.fragment.ThirdExampleFragment;
import me.opklnm102.exhelloreactive.fragment.ZipExampleFragment;
import me.opklnm102.exhelloreactive.navigation_drawer.NavigationDrawerCallbacks;
import me.opklnm102.exhelloreactive.navigation_drawer.NavigationDrawerFragment;


public class MainActivity extends AppCompatActivity implements NavigationDrawerCallbacks {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentManager fm = getSupportFragmentManager();

        mNavigationDrawerFragment = (NavigationDrawerFragment) fm.findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, mDrawerLayout, mToolbar);

        /*
         Debug Build에서만 StrictMode 활성화
         Main Thread 사용에 대한 모든 위반사항과 Activities, BroadcastReceivers, Sqlite객체 등
         Memory Leak에 영향을 미칠 가능성이 있는 모든 위반 사항을 알려준다.
        */
        if(BuildConfig.DEBUG){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()  //위반사항이 생길 때마다 LogCat에 출력
            .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
            .detectAll()
            .penaltyLog()
            .build());
        }

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fm.beginTransaction()
                    .add(R.id.fragment_container, FirstExampleFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mNavigationDrawerFragment.isDrawerOpen()){
            mNavigationDrawerFragment.closeDrawer();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getSupportFragmentManager();
        switch (position){
            case 0:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, FirstExampleFragment.newInstance())
                        .commit();
                break;
            case 1:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, SecondExampleFragment.newInstance())
                        .commit();
                break;
            case 2:
            fm.beginTransaction()
                    .replace(R.id.fragment_container, ThirdExampleFragment.newInstance())
                    .commit();
            break;
            case 3:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, FilterExampleFragment.newInstance())
                        .commit();
                break;
            case 4:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, TakeExampleFragment.newInstance())
                        .commit();
                break;
            case 5:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, DistinctExampleFragment.newInstance())
                        .commit();
                break;
            case 6:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, MapExampleFragment.newInstance())
                        .commit();
                break;
            case 7:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, ScanExampleFragment.newInstance())
                        .commit();
                break;
            case 8:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, GroupByExampleFragment.newInstance())
                        .commit();
                break;
            case 9:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, MergeExampleFragment.newInstance())
                        .commit();
                break;
            case 10:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, ZipExampleFragment.newInstance())
                        .commit();
                break;
            case 11:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, JoinExampleFragment.newInstance())
                        .commit();
                break;
            case 12:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, CombineLatestExampleFragment.newInstance())
                        .commit();
                break;
            case 13:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, AndThenWhenExampleFragment.newInstance())
                        .commit();
                break;
            case 14:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, SharedPreferencesListExampleFragment.newInstance())
                        .commit();
                break;
            case 15:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, LongTaskFragment.newInstance())
                        .commit();
                break;
            case 16:
                fm.beginTransaction()
                        .replace(R.id.fragment_container, NetworkTaskFragment.newInstance())
                        .commit();
                break;
            case 17:
                startActivity(new Intent(this, SoActivity.class));
                break;
        }
    }
}
