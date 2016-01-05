package com.example.dong.sixthapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import javax.sql.PooledConnection;

/**
 * Created by Dong on 2015-07-11.
 */
public class PhotoGalleryFragment extends VisibleFragment {
    private static final String TAG = "PhotoGalleryFragment";

    GridView mGridView;
    ArrayList<GalleryItem> mItems;
    ThumbnailDownloader<ImageView> mThumbnailThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true); //유보 프래그먼트
        setHasOptionsMenu(true);  //옵션메뉴의 콜백을 받겠다.

        //AsyncTask 시작
        //자신의 백그라운드 스레드를 시작시키고 doInBackground()호출
        // new FetchItemsTask().execute();

        updateItems();

        //서비스 실행
//        Intent intent = new Intent(getActivity(), PollService.class);
//        getActivity().startService(intent);

        //알람실행
//        PollService.setServiceAlarm(getActivity(), true);

//        mThumbnailThread = new ThumbnailDownloader<ImageView>();

        //Handler는 현재 쓰레드의 Looper에 자신을 연결
        //Handler가 onCreate()에서 생성되었으므로 main쓰레드의 Looper와 연결될 것
        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        Log.i(TAG, "Background thread started");
    }

    public void updateItems(){
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridView);

        setupAdapter();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GalleryItem item = mItems.get(i);

                Uri photoPageUri = Uri.parse(item.getPhotoPageUrl());
                //1. 암시적 인텐트
                //Intent intent = new Intent(Intent.ACTION_VIEW, photoPageUri);

                //2.WebView
                Intent intent = new Intent(getActivity(), PhotoPageActivity.class);
                intent.setData(photoPageUri);

                startActivity(intent);
            }
        });

        return view;
    }

    //스레드 종료를 위해
    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    //뷰가 소멸할 때 클린업 메소드 호출
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    void setupAdapter() {
        //getActivity() == null인지 검사하는 이유
        //프래그먼트가 어느 액티비티에도 첨부되지 않은 상태로 존재할 수 있기 떄문
        //안드로이드 프레임워크의 콜백들에 프래그먼트가 호출 받으면 당연히 액티비티에 속해 있다.
        //호스팅하는 액티비티가 없다면 콜백 호출 또한 받을 수 없기 때문
        if (getActivity() == null || mGridView == null) return;

        if (mItems != null) {
//            mGridView.setAdapter(new ArrayAdapter<GalleryItem>(getActivity(), android.R.layout.simple_gallery_item, mItems));
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }

    //2
    //private class FetchItemsTask extends AsyncTask<Void, Void, Void>

    //3번째 인자
    //AsyncTask에서 생성된 결과의 타입, doInBackground() 리턴값, onPostExecute()의 입력 인자.
    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {

        @Override
        //2
        // protected Void doInBackground(Void... voids)
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            Activity activity = getActivity();
            if(activity == null)
                return new ArrayList<GalleryItem>();

            String query = PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(FlickrFetchr.PREF_SEARCH_QUERY, null);
            if (query != null) {
                return new FlickrFetchr().search(query);
            } else {
                //1
//            try{
//                String result = new FlickrFetchr().getUrl("http://www.google.com");
//                Log.i(TAG, "Fetched contents of URL: " + result);
//            }catch (IOException ioe){
//                Log.e(TAG, "Failed to fetch URL: " + ioe);
//            }

                //2
                // new FlickrFetchr().fetchItems();
                //return null;
                return new FlickrFetchr().fetchItems();
            }
        }

        //doInBackground()이 완전히 끝난 후 실행
        //main스레드에서 실행되기 때문에 UI변경이 안전
        //백그라운드 스레드에서 UI변경은 허용X, 안전하지도 않고 바람직하지도 않다.
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {

        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView iV = (ImageView) convertView.findViewById(R.id.gallery_item_imageView);
            iV.setImageResource(R.drawable.brian_up_close);
            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(iV, item.getUrl());

            return convertView;
        }
    }

    @Override
    @TargetApi(11)
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            //SearchView를 찾는다.
            MenuItem searchItem = menu.findItem(R.id.menu_item_search);
            SearchView searchView = (SearchView)searchItem.getActionView();

            //searchable.xml로부터 데이터를 얻는다.
            SearchManager searchManager = (SearchManager)getActivity()
                    .getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getActivity().getComponentName();
            //getSearchableInfo() -> 매니패스트에서 유용한 정보를 결합한 후 SearchableInfo객체로 반환
            SearchableInfo searchInfo = searchManager.getSearchableInfo(name);

            //SearchView와 연결
            searchView.setSearchableInfo(searchInfo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_search:
                getActivity().onSearchRequested();
                return true;
            case R.id.menu_item_clear:
                PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .edit()
                        .putString(FlickrFetchr.PREF_SEARCH_QUERY, null)
                        .commit();
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlarm = !PollService.isServiceAlarmOn(getActivity());
                PollService.setServiceAlarm(getActivity(), shouldStartAlarm);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    getActivity().invalidateOptionsMenu();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem toggleItem = menu.findItem(R.id.menu_item_toggle_polling);
        if(PollService.isServiceAlarmOn(getActivity())){
            toggleItem.setTitle(R.string.stop_polling);
        }else{
            toggleItem.setTitle(R.string.start_polling);
        }
    }
}