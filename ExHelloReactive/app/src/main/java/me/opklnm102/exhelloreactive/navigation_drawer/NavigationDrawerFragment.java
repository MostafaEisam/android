package me.opklnm102.exhelloreactive.navigation_drawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.R;


public class NavigationDrawerFragment extends Fragment implements NavigationDrawerCallbacks {

    public static final String PREF_USER_LEARNED_DRAWER = "";

    public static final String STATE_SELECTED_POSITION = "";

    public static final String PREFERENCES_FILE = "";

    NavigationDrawerCallbacks mCallbacks;

    @BindView(R.id.recyclerView_drawer)
    RecyclerView mDrawerList;

    View mFragmentContainerView;

    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mActionBarDrawerToggle;

    boolean mUserLearnedDrawer;

    boolean mFromSavedInstanceState;

    int mCurrentSelectedPosition;

    public static void saveSharedSetting(Context context, String settingName, String settingValue) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static String readSharedSetting(Context context, String settingName, String settingValue) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return pref.getString(settingName, settingValue);
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    public static NavigationDrawerFragment newInstance() {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallbacks = (NavigationDrawerCallbacks) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawer Callbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mDrawerList.setHasFixedSize(true);

        final List<NavigationItem> navigationItemList = getMenu();
        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(navigationItemList);
        adapter.setNavigationDrawerCallbacks(this);
        mDrawerList.setAdapter(adapter);
        selectItem(mCurrentSelectedPosition);
    }

    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mActionBarDrawerToggle;
    }

    public void setActionBarDrawerToggle(ActionBarDrawerToggle actionBarDrawerToggle) {
        mActionBarDrawerToggle = actionBarDrawerToggle;
    }

    public void setup(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setStatusBarBackgroundColor(
                getResources().getColor(R.color.myPrimaryColor));

        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveSharedSetting(getActivity(), PREF_USER_LEARNED_DRAWER, "true");
                }

                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });

        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(mFragmentContainerView);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(mFragmentContainerView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public List<NavigationItem> getMenu() {
        List<NavigationItem> items = new ArrayList<>();
        items.add(new NavigationItem("Example 1", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Example 2", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Example 3", getResources().getDrawable(R.mipmap.ic_launcher)));

        items.add(new NavigationItem("Filter", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Take and TakeLast", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Distinct abd DistinctUntilChanged", getResources().getDrawable(R.mipmap.ic_launcher)));

        items.add(new NavigationItem("Map", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Scan", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("GroupBy", getResources().getDrawable(R.mipmap.ic_launcher)));

        items.add(new NavigationItem("Merge", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Zip", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Join", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("CombineLatest", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("And Then When", getResources().getDrawable(R.mipmap.ic_launcher)));

        items.add(new NavigationItem("SharedPreferences", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Long task", getResources().getDrawable(R.mipmap.ic_launcher)));
        items.add(new NavigationItem("Network task", getResources().getDrawable(R.mipmap.ic_launcher)));

        items.add(new NavigationItem("Stack Overflow", getResources().getDrawable(R.mipmap.ic_launcher)));

        return items;
    }

    void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
        ((NavigationDrawerAdapter) mDrawerList.getAdapter()).selectPosition(position);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectItem(position);
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        mDrawerLayout = drawerLayout;
    }
}
