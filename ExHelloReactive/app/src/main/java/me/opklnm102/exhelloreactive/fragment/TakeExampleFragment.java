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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.opklnm102.exhelloreactive.AppInfoList;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.adapter.AppInfoListAdapter;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Observer;

/**
 * Created by Administrator on 2016-05-11.
 */
public class TakeExampleFragment extends Fragment {

    @BindView(R.id.recyclerView_app_info)
    RecyclerView rvAppInfo;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    AppInfoListAdapter mAppInfoListAdapter;

    ArrayList<AppInfo> mAppInfos = new ArrayList<>();

    Unbinder mUnbinder;

    public TakeExampleFragment(){
    }

    public static TakeExampleFragment newInstance() {
        TakeExampleFragment fragment = new TakeExampleFragment();
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

    private void loadList(List<AppInfo> appInfos){
        rvAppInfo.setVisibility(View.VISIBLE);

        Observable.from(appInfos)
                .takeLast(3)
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Something went south!", Toast.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(AppInfo appInfo) {
                        mAppInfos.add(appInfo);
                        mAppInfoListAdapter.addAppInfo(mAppInfos.size() - 1, appInfo);
                    }
                });

//        Observable.from(appInfos)
//                .take(3)
//                .subscribe(new Observer<AppInfo>() {
//                    @Override
//                    public void onCompleted() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(getActivity(), "Something went south!", Toast.LENGTH_SHORT).show();
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(AppInfo appInfo) {
//                        mAppInfos.add(appInfo);
//                        mAppInfoListAdapter.addAppInfo(mAppInfos.size() - 1, appInfo);
//                    }
//                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
