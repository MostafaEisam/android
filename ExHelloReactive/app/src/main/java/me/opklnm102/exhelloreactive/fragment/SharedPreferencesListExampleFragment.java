package me.opklnm102.exhelloreactive.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.opklnm102.exhelloreactive.AppInfoList;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.adapter.AppInfoListAdapter;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-12.
 */
public class SharedPreferencesListExampleFragment extends Fragment {

    @BindView(R.id.recyclerView_app_info)
    RecyclerView rvAppInfo;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    AppInfoListAdapter mAppInfoListAdapter;

    ArrayList<AppInfo> mAppInfos = new ArrayList<>();

    Unbinder mUnbinder;

    public SharedPreferencesListExampleFragment() {
    }

    public static SharedPreferencesListExampleFragment newInstance() {
        SharedPreferencesListExampleFragment fragment = new SharedPreferencesListExampleFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAppInfo.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAppInfoListAdapter = new AppInfoListAdapter(view.getContext());
        rvAppInfo.setAdapter(mAppInfoListAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setEnabled(true);
//        mSwipeRefreshLayout.setOnRefreshListener(this::refreshTheList);
        rvAppInfo.setVisibility(View.GONE);

        List<AppInfo> appInfoList = AppInfoList.getInstance().getAppInfoList();

        loadList();
    }

    private void loadList() {
        rvAppInfo.setVisibility(View.VISIBLE);

        getApps()
                //Observable이 Observer가 소비하는 것보다 더 빠르게 아이템을 발행할 경우.
                //Observable에게 아이템을 버퍼에 저장하고 적절한 타이밍에 제공하도록 지시
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())    //getApps()가 실행될 Scheduler 명시
                .observeOn(AndroidSchedulers.mainThread())  //결과가 실행될 Scheduler 명시
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        mAppInfos.add(appInfo);
                        mAppInfoListAdapter.addAppInfo(mAppInfos.size() - 1, appInfo);
                    }
                });
    }

    //SharedPreferences로 부터 설치된 Application 리스트를 읽어 이를 활용한 AppInfo 아이템을 하나씩 발행
    private Observable<AppInfo> getApps() {
        return Observable
                .create(subscriber -> {
                    List<AppInfo> appInfos = new ArrayList<AppInfo>();

                    SharedPreferences pref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    Type appInfoType = new TypeToken<List<AppInfo>>() {
                    }.getType();

                    String serializedAppInfos = pref.getString("APPS", "");
                    if (!"".equals(serializedAppInfos)) {
                        appInfos = new Gson().fromJson(serializedAppInfos, appInfoType);
                    }

                    for (AppInfo info : appInfos) {
                        subscriber.onNext(info);
                    }
                    subscriber.onCompleted();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
