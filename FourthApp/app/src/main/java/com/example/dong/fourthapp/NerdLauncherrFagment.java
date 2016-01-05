package com.example.dong.fourthapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dong on 2015-07-10.
 */
public class NerdLauncherrFagment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);  //유보 프래그먼트

        /*
         startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);를 하는 이유!!
        MAIN/LAUNCHER암시적 인텐트와 일치할 수 있고 안될 수도 있기 떄문
        startActivity(Intent)는 "이 암시적 인텐트와 일치하는 액티비티를 시작시켜라"는 의미가 아니다.
        그것은 "이 암시적 인텐트와 일치하는 디폴트 액티비티를 시작시켜라"는 의미다.
        암시적 인텐트를 startActivity() 또는 startActivityForResult()를 통해 전달할 때 os는 내부적으로
        Intent.CATEGORY_DEFAULT카테고리를 인텐트에 추가한다.
        따라서 startActivity()로 전달되는 암시적 인텐트와 일치하는 인텐트 필터를 원한다면 그 인텐트
        필터에 DEFAULT카테고리를 포함시켜야 한다.
        단, MAIN/LAUNCHER 인텐터 필터를 갖는 액티비티는 자신이 속한 앱의 주 진입점이 된다.
        따라서 CATEGORY_DEFAULT 카테고리를 포함할 필요는 없다.

        MAIN/LAUNCHER 인텐트 필터는 CATEGORY_DEFAULT를 포함하지 않을 수 있으므로 startActivity()로 전달되는
        암시적 인텐트와 일치된다는 보장이 없다. 따라서 MAIN/LAUNCHER 인텐트 필터를 갖는 액티비티들의 경우에는
        startActivity()대신 PackageManager를 직접 쿼리하는 인텐트를 사용한다.
        */
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        //암시적 인텐트에 응답하는 액티비티 수 구하기
        final PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);

        Log.i("NerdLauncher", "I've found" + activities.size() + " activities.");

        //다른 메타데이터와 더불어 액티비티들의 라벨은 PackageManager가 반환하는 ResolveInfo객체에서 알 수있다.
        //ResolveInfo객체들을 라벨의 알파벳순으로 정렬한다.
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo resolveInfo, ResolveInfo t1) {
                return String.CASE_INSENSITIVE_ORDER.compare(resolveInfo.loadLabel(pm).toString(),
                        t1.loadLabel(pm).toString());
            }
        });

        setListAdapter(new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1, activities){
                    public View getView(int pos, View convertView, ViewGroup parent){
                        View view = super.getView(pos, convertView, parent);
                        TextView tV = (TextView)view;
                        ResolveInfo ri = getItem(pos);
                        tV.setText(ri.loadLabel(pm));
                        return view;
                    }
                });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo)l.getAdapter().getItem(position);
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if(activityInfo == null) return;

        //명시적 인텐트의 일부로 액션을 전달
        //이 경우 대부분의 App들은 액션이 있건 없건 동일하게 동작
        //그러나 일부App에서는 자신들의 동작을 변경할 수 있다.
        //즉, 시작되는 방법에 따라 같은 액티비티가 다른 UI를 보여줄 수 있다.
        Intent intent = new Intent(Intent.ACTION_MAIN);

        //메타데이터로부터 패키지 이름과 클래스 이름을 얻는다. 이것을 이용해 명시적 인텐트 생성
        intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);

        //새로운 액티비티를 새로운 태스크로 시작
        //액티비티당 하나의 태스크만 생성
        //새로운 태스크를 시작시키는 대신 기존 태스크로 전환시킨다.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}
