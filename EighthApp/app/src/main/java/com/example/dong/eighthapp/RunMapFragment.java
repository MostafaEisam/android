package com.example.dong.eighthapp;

import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dong.eighthapp.RunDatabaseHelper.LocationCursor;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

/**
 * Created by Dong on 2015-07-20.
 */
public class RunMapFragment extends SupportMapFragment implements LoaderCallbacks<Cursor> {
    private static final String ARG_RUN_ID = "RUN_ID";
    private static final int LOAD_LOCATIONS = 0;

    private GoogleMap mGoogleMap;
    private RunDatabaseHelper.LocationCursor mLocationCursor;  //지동에 경로 나타낼 때 사용

    /*
    이동(Run) id를 인자로 받아서 그것을 새로 생성된 RunMapFragment인스턴스의 Bundle인자로 설정
    이 Bundle인자는 위치 리스트의 데이터들을 가져올 때 사용될 것
     */
    public static RunMapFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        //GoogleMap 객체의 참조를 인스턴스 변수에 보존한다.
        //GoogleMap은 MapView와 연결된 모델 객체, 지도에 여러가지를 추가하는데 사용
        mGoogleMap = getMap();
        //사용자의 위치를 보여준다.
        mGoogleMap.setMyLocationEnabled(true);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //이동(Run) id가 인자로 전달되었는지 확인하고 그 이동 객체를 찾는다.
        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long runId = args.getLong(ARG_RUN_ID, -1);
        return new LocationListCursorLoader(getActivity(), runId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationCursor = (LocationCursor) cursor;
        updateUI();
    }

    //프래그먼트를 벗어난 후 LoadManager가 로더를 셧다운할 때 호출
    //장치의 회전시에는 호출X
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //커서를 닫고 데이터의 사용을 중단한다.
        mLocationCursor.close();
        mLocationCursor = null;
    }

    /*
    라이브 위치 위치변경 추가해야함 -> LocationReceiver를 사용해 지도에 표식 다시그리기
    새로 그리기 전에 이전 이동 표식을 지운다.
     */
    private void updateUI() {
        if (mGoogleMap == null || mLocationCursor == null)
            return;

        //현재 이동의 위치들이 나타날 지도 위에 겹쳐서 보여줄 것을 설정한다.
        //모든 위치를 연결하는 폴리라인을 생성한다.
        PolylineOptions line = new PolylineOptions();

        //또한, 지도에 맞추기 위해 LatLngBounds를 생성한다.
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();

        //위치들을 반복 처리한다.
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()) {
            Location loc = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

            Resources r = getResources();

            //이것이 첫번째 위치면 그것의 표식을 추가한다.
            //제목 및 텍스트를 저장하기 위해 MarkerOptions인스턴스 생성
            //icon(BitmapDescriptor)과 BitmapDescriptorFactory를 사용해 다른 색의 표식 또는
            //커스텀 그래픽 표시까지도 생성가능
            if (mLocationCursor.isFirst()) {
                String startDate = new Date(loc.getTime()).toString();
                MarkerOptions startMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(r.getString(R.string.run_start))
                        .snippet(r.getString(R.string.run_started_at_format, startDate));
                mGoogleMap.addMarker(startMarkerOptions);
            } else if (mLocationCursor.isLast()) {
                //이것이 마지막 위치면서 첫번째가 아니면 표식을 추가한다.
                String endDate = new Date(loc.getTime()).toString();
                MarkerOptions finishMarkerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(r.getString(R.string.run_finish))
                        .snippet(r.getString(R.string.run_finished_at_format, endDate));
                mGoogleMap.addMarker(finishMarkerOptions);
            }
            Log.e("fdf", "fdfdffaaaaaa");
            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }

        //폴리라인을 지도에 추가한다.
        mGoogleMap.addPolyline(line);

        //경로를 보여주기 위해 지도의 크기를 조정한다.
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        //맵 카메라의 이동 명령을 구성한다.
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds,
                display.getWidth(), display.getHeight(), 15);
        mGoogleMap.moveCamera(movement);
    }
}
