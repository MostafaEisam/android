package me.opklnm102.exhelloreactive;

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
import me.opklnm102.exhelloreactive.fragment.DistinctExampleFragment;
import me.opklnm102.exhelloreactive.fragment.FilterExampleFragment;
import me.opklnm102.exhelloreactive.fragment.FirstExampleFragment;
import me.opklnm102.exhelloreactive.fragment.MapExampleFragment;
import me.opklnm102.exhelloreactive.fragment.ScanExampleFragment;
import me.opklnm102.exhelloreactive.fragment.SecondExampleFragment;
import me.opklnm102.exhelloreactive.fragment.TakeExampleFragment;
import me.opklnm102.exhelloreactive.fragment.ThirdExampleFragment;
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

        if(BuildConfig.DEBUG){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectAll()
            .penaltyLog()
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

        }
    }
}
