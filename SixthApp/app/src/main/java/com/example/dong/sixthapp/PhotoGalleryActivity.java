package com.example.dong.sixthapp;

import android.app.SearchManager;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class PhotoGalleryActivity extends SingleFragmentActivity {
    private static final String TAG = "PhotoGalleryActivity";

    @Override
    protected Fragment createFragment() {
        return new PhotoGalleryFragment();
    }

    //새로운 Intent를 받는다.
    //받을 때 마다 fragment의 항목을 채운다.
    //onNewIntent(Intent)의 처리에서 주목해야 할것
    //만일 새로운 인텐트 값이 필요하면 그것을 어딘가에 저장해야 한다는 것
    //getIntent()로부터 받는 값은 새로운 것이 아닌 이전의 인텐트가 될 것
    //getIntent()는 현재의 액티비티를 시작시켰던 인텐트를 반환하게 되어 있기 때문
    @Override
    protected void onNewIntent(Intent intent) {
        PhotoGalleryFragment fragment = (PhotoGalleryFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.i(TAG, "Received a new search query: " + query);

            //SharedPreferences얻기
            //Editor객체 -> SharedPreferences에 값들을 변경 및 보존하기 위해 사용
            //FragmentTransaction으로 했던것과 유사하게 변경된 데이터를 모았다가 하나의 트랜잭션으로 파일에 저장
            //edit() -> Editor객체 얻기
            //모든 변경이 끝나면 Editor의 commit()를 호출하여 SharedPreferences파일에 저장
            PreferenceManager.getDefaultSharedPreferences(this)
                    .edit()
                    .putString(FlickrFetchr.PREF_SEARCH_QUERY, query)
                    .commit();
        }
        fragment.updateItems();
    }
}
