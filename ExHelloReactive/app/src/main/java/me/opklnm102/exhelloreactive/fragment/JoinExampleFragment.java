package me.opklnm102.exhelloreactive.fragment;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.opklnm102.exhelloreactive.App;
import me.opklnm102.exhelloreactive.AppInfoList;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.adapter.AppInfoListAdapter;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2016-05-12.
 */
public class JoinExampleFragment extends Fragment {

    @BindView(R.id.recyclerView_app_info)
    RecyclerView rvAppInfo;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    AppInfoListAdapter mAppInfoListAdapter;

    ArrayList<AppInfo> mAppInfos = new ArrayList<>();

    Unbinder mUnbinder;

    public JoinExampleFragment() {
    }

    public static JoinExampleFragment newInstance() {
        JoinExampleFragment fragment = new JoinExampleFragment();
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

        loadList(appInfoList);
    }

    private void loadList(List<AppInfo> appInfos) {
        rvAppInfo.setVisibility(View.VISIBLE);

        //설치된 App리스트에서 App 아이템을 매초 발행하는 Observable 시퀀스
        Observable<AppInfo> appsSequence = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .map(position -> {
                    App.L.debug("Position: " + position);
                    return appInfos.get(position.intValue());
                });

        //매초 Long 아이템 발행
        Observable<Long> tictoc = Observable.interval(1000, TimeUnit.MILLISECONDS);

        //2번째 아이템이 발행될 때마다 소스 아이템을 결합, 동일한 소스 아이템을 2초동안 사용
        appsSequence
                .join(
                        //appsSequence와 tictoc을 조인하기 위해 2개의 Func1 변수 명시
                        //2개의 time window 서술
                        tictoc, appInfo -> Observable.timer(2, TimeUnit.SECONDS),
                        time -> Observable.timer(0, TimeUnit.SECONDS),
                        this::updateTitle  //어떻게 발행한 아이템을 결합할 것인지 서술
                )
                .observeOn(AndroidSchedulers.mainThread())
                .take(10)
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), "Here is the list!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        mAppInfos.add(appInfo);
                        int position = mAppInfos.size() - 1;
                        mAppInfoListAdapter.addAppInfo(position, appInfo);
                        rvAppInfo.smoothScrollToPosition(position);
                    }
                });
    }

    private AppInfo updateTitle(AppInfo appInfo, Long time){
        appInfo.setName(time + " " + appInfo.getName());
        return appInfo;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
