package me.opklnm102.exhelloreactive.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.App;
import me.opklnm102.exhelloreactive.AppInfoList;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.adapter.AppInfoListAdapter;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

public class ThirdExampleFragment extends Fragment {

    @BindView(R.id.recyclerView_app_info)
    RecyclerView rvAppinfo;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    AppInfoListAdapter mAppInfoListAdapter;

    ArrayList<AppInfo> mAppInfos = new ArrayList<>();

    Subscription mTimeSubscription;

    public ThirdExampleFragment() {
    }

    public static ThirdExampleFragment newInstance() {
        ThirdExampleFragment fragment = new ThirdExampleFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAppinfo.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mAppInfoListAdapter = new AppInfoListAdapter(view.getContext());
        rvAppinfo.setAdapter(mAppInfoListAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.myPrimaryColor));
        mSwipeRefreshLayout.setProgressViewOffset(false, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setEnabled(true);
//        mSwipeRefreshLayout.setOnRefreshListener(this::refreshTheList);
        rvAppinfo.setVisibility(View.GONE);

        List<AppInfo> appInfoList = AppInfoList.getInstance().getAppInfoList();

        AppInfo appOne = appInfoList.get(0);

        AppInfo appTwo = appInfoList.get(1);

        AppInfo appThree = appInfoList.get(2);

        loadApps(appOne, appTwo, appThree);
    }


    private void loadApps(AppInfo appOne, AppInfo appTwo, AppInfo appThree) {
        rvAppinfo.setVisibility(View.VISIBLE);

        //3개의 AppInfo 객체만 갖고 있고 이객체를 RecyclerView에 추가
        Observable<AppInfo> threeOfThem = Observable.just(appOne, appTwo, appThree);
        threeOfThem
                .repeat(3)  //개별적으로 1개씩 발행하는 9개의 아이템 시퀀스를 생성
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        mAppInfos.add(appInfo);
                        mAppInfoListAdapter.addAppInfo(mAppInfos.size() - 1, appInfo);
                    }
                });

        mTimeSubscription = Observable.timer(3, 3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d("RxJava", " I say " + aLong);
                    }
                });
    }

    private Observable<Integer> getInt() {

        //3초마다 발행
        Subscription stopMePlease = Observable
                .interval(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), "Yeaaah!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Toast.makeText(getActivity(), "I say " + aLong, Toast.LENGTH_SHORT).show();
                    }
                });

        //처음 3초후에 시작해서 3초마다 새로운 숫자 발행
        Subscription intervalMePlease = Observable
                .interval(3, 3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), "Yeaaah!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Toast.makeText(getActivity(), "I say " + aLong, Toast.LENGTH_SHORT).show();
                    }
                });

        //3초후에 0을 발행하고 완료
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d("RxJava", " I say " + aLong);
                    }
                });

        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            App.L.debug("GETINT");
            subscriber.onNext(42);
            subscriber.onCompleted();
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mTimeSubscription.isUnsubscribed()) {
            mTimeSubscription.unsubscribe();
        }
    }
}

