package com.example.dong.doitmission_08;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Hashtable;


public class MainActivity extends ActionBarActivity {
    CalendarMonthView monthView;  //월별 캘린더 뷰 객체
    CalendarMonthAdapter monthViewAdapter;  //월별 캘린더 어댑터
    TextView monthText;  //월을 표시하는 텍스트뷰
    int curYear;  //현재 연도
    int curMonth;  //현재 월
    MonthItem curItem;  //선택된 날짜 아이템
    int day;  //현재 일

    PlanDialog plandialog;  //다이얼로그

    ListView listView;  //리스트뷰
    listItemAdapter listAdapter;  //리스트 어댑터

    Hashtable<String, ArrayList> ht = new Hashtable<String, ArrayList>();  //일정 저장용 테이블

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //캘린더 생성
        monthView = (CalendarMonthView) findViewById(R.id.monthView);
        monthViewAdapter = new CalendarMonthAdapter(this);
        monthView.setAdapter(monthViewAdapter);

        //커스텀 다이얼로그 객체 생성
        plandialog = new PlanDialog(MainActivity.this);
        plandialog.setTitle("일정 추가");

        //커스텀 다이얼로그 객객체에 Dismiss 리스너 설정
        //커스텀 다이얼로그가 사라졌을 때(Dismiss) 취할 행동들
        plandialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {  //HashTable에 추가..
                String time, plan;  //일정(시간,내용)
                time = plandialog.getHour() + " : " + plandialog.getMinute();
                plan = plandialog.getPlan();
                listItem item = new listItem(time, plan);
                String key = curYear + "-" + curMonth + "-" + day;  //해쉬 Key

                if (ht.containsKey(key)) {  //키가 있으면 있는 ArrayList에 추가
                    ArrayList<listItem> al = ht.get(key);
                    ht.remove(key);
                    al.add(item);
                    ht.put(key, al);
                } else {  //없으면 새로운 ArrayList를 만들어서 추가
                    ArrayList<listItem> al = new ArrayList<>();
                    al.add(item);
                    ht.put(key, al);
                }

                curItem.setisPlan(true);
                Toast.makeText(getApplicationContext(), "일정 저장", Toast.LENGTH_SHORT).show();
                plandialog.init();  //다이얼로그 editText 초기화
            }
        });

        plandialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(getApplicationContext(), "일정 저장 취소", Toast.LENGTH_SHORT).show();
                plandialog.init();  //다이얼로그 editText 초기화
            }
        });

        monthView.setOnDataSelectionListener(new OnDataSelectionListener() {
            @Override
            public void onDataSelected(AdapterView parent, View v, int position, long id) {
                //현재 선택한 날짜의 일정 정보 표시
                curItem = (MonthItem) monthViewAdapter.getItem(position);
                day = curItem.getDay();
                String key = curYear + "-" + curMonth + "-" + day;  //해쉬 Key

                //어댑터랑 연결할 리스트뷰 참조
                listView = (ListView) findViewById(R.id.listview);

                if (curItem.getisPlan()) {
                    //setAdapter하면 된다... 해쉬테이블의 리스트랑
                    listAdapter = new listItemAdapter();

                    ArrayList<listItem> al = ht.get(key);

                    for (int i = 0; i < al.size(); i++) {  //아이템 추가
                        listAdapter.add(al.get(i));
                    }

                    listView.setAdapter(listAdapter);  //연결
                }
                else{
                    listAdapter = null;
                    listView.setAdapter(listAdapter);
                }

                Log.d("CalendarMonthViewActivity", "Selected : " + day);
            }
        });

        monthText = (TextView) findViewById(R.id.monthText);
        setMonthText();

        //이전 월로 넘어가는 이벤트 처리
        Button monthPrevious = (Button) findViewById(R.id.monthPrevious);
        monthPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthViewAdapter.setPreviousMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });

        //다음 월로 넘어가는 이벤트 처리
        Button monthNext = (Button) findViewById(R.id.monthNext);
        monthNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthViewAdapter.setNextMonth();
                monthViewAdapter.notifyDataSetChanged();

                setMonthText();
            }
        });
    }

    private void setMonthText() {
        curYear = monthViewAdapter.getCurYear();
        curMonth = monthViewAdapter.getCurMonth();

        monthText.setText(curYear + "년 " + (curMonth + 1) + "월");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.addplan:  //대화상자 띄우기
                plandialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}
