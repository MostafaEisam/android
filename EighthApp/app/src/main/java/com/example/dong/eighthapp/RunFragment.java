package com.example.dong.eighthapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dong on 2015-07-15.
 */
public class RunFragment extends Fragment {

    private static final String TAG = "RunFragment";
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_RUN = 0;
    private static final int LOAD_LOCATION = 1;

    private Button mStartBtn, mStopBtn, mMapBtn;
    private TextView mStartedTv, mLatitudeTv, mLongitudeTv, mAltitudeTv, mDurationTv;

    private RunManager mRunManager;
    private Run mRun;
    private Location mLastLocation;

    private BroadcastReceiver mLocationReceiver = new LocationReceiver() {

        @Override
        protected void onLocationReceived(Context context, Location loc) {
            if (!mRunManager.isTrackingRun(mRun))
                return;
            mLastLocation = loc;
            if (isVisible())
                updataUI();
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT).show();
        }
    };

    /*
    사용자가 이동 리스트의 특정이동을 선택하여 그 이동의 상세 내역을 볼 수 있게하기 위해
    이동id를 프래그먼트 인자로 전달
    RunFragment는 RunActivity가 호스팅하므로 이동id를 갖는 인텐트 엑스트라도 필요

     */
    public static RunFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunFragment rf = new RunFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  //유보 프래그먼트
        mRunManager = RunManager.getInstance(getActivity());

        //이동(Run) ID가 인자로 전달되었는지 확인하고 그 이동 객체를 찾는다.
        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
//                mRun = mRunManager.getRun(runId);
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_RUN, args, new RunLoaderCallbacks());
//                mLastLocation = mRunManager.getLastLocationForRun(runId);
                lm.initLoader(LOAD_LOCATION, args, new LocationLoaderCallbacks());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);

        mStartedTv = (TextView) view.findViewById(R.id.run_startedTextView);
        mLatitudeTv = (TextView) view.findViewById(R.id.run_latitudeTextView);
        mLongitudeTv = (TextView) view.findViewById(R.id.run_longitudeTextView);
        mAltitudeTv = (TextView) view.findViewById(R.id.run_altitudeTextView);
        mDurationTv = (TextView) view.findViewById(R.id.run_durationTextView);

        mStartBtn = (Button) view.findViewById(R.id.run_startButton);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mRunManager.startLocationUpdates();
//                mRun = new Run();
                if (mRun == null) {
                    mRun = mRunManager.startNewRun();
                } else {
                    mRunManager.startTrackingRun(mRun);
                }

                updataUI();
            }
        });

        mStopBtn = (Button) view.findViewById(R.id.run_stopButton);
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mRunManager.stopLocationUpdates();
                mRunManager.stopRun();
                updataUI();
            }
        });

        mMapBtn = (Button) view.findViewById(R.id.run_mapButton);
        mMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RunMapActivity.class);
                intent.putExtra(RunMapActivity.EXTRA_RUN_ID, mRun.getId());
                startActivity(intent);
            }
        });

        updataUI();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mRunManager.isTrackingRun() && mRun == null) {
            mRun = new Run();
        }
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }

    //현재 위치 갱신 정보 요청 여부에 따라 버튼을 활성,비활성
    private void updataUI() {
        boolean started = mRunManager.isTrackingRun();
        boolean trackingThisRun = mRunManager.isTrackingRun(mRun);

        if (mRun != null)
            mStartedTv.setText(mRun.getStartDate().toString());

        int durationSeconds = 0;
        if (mRun != null && mLastLocation != null) {
            durationSeconds = mRun.getDurationsSeconds(mLastLocation.getTime());
            mLatitudeTv.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTv.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTv.setText(Double.toString(mLastLocation.getAltitude()));
            mMapBtn.setEnabled(true);
        }else{
            mMapBtn.setEnabled(false);
        }
        mDurationTv.setText(Run.formatDuration(durationSeconds));

        mStartBtn.setEnabled(!started);
        mStopBtn.setEnabled(started && trackingThisRun);
    }

    /*
    RunFragment에는 서로 다른 타입의 데이터가 2개 있다.(Run, Location). 그러나 자바 제네릭의
    제약 때문에 두 데이터의 LoaderCallbacks<D>의 인터페이스를 RunFragment내부의 메소드들로 직접
    구현할 수 없다. 대신 내부 클래스로 만들어서 각각 인터페이스르 구현한 후 LoaderManager의 initLoader()
    를 호출할 때 인자로 그 내부 클래스의 인스턴스를 전달하게 하면 자바 제네릭의 제약을 피할 수 있다.
     */
    private class RunLoaderCallbacks implements LoaderManager.LoaderCallbacks<Run> {

        @Override
        public Loader<Run> onCreateLoader(int id, Bundle args) {
            return new RunLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Run> loader, Run data) {
            mRun = data;
            updataUI();
        }

        @Override
        public void onLoaderReset(Loader<Run> loader) {
            //아무런 처리도 하지 않는다.
        }
    }

    private class LocationLoaderCallbacks implements LoaderManager.LoaderCallbacks<Location>{

        @Override
        public Loader<Location> onCreateLoader(int id, Bundle args) {
            return new LastLocationLoader(getActivity(), args.getLong(ARG_RUN_ID));
        }

        @Override
        public void onLoadFinished(Loader<Location> loader, Location location) {
            mLastLocation = location;
            updataUI();
        }

        @Override
        public void onLoaderReset(Loader<Location> loader) {
            //아무런 처리도 하지 않는다.
        }
    }
}
