package com.example.dong.eighthapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Dong on 2015-07-17.
 */
/*
일단 onCreate()에서 커서를 설정, onDestroy()에서 커서를 닫는다.
좋은 방법은 아니다 -> DB쿼리가 백그라운드 스레드가 아닌 main스레드에서 실행되므로 경우에 따라서는
ANR(Application Not Responding, 애플리케이션이 응답하지 않음) 에러가 발생할 수 있기 때문
Loader를 사용해서 그 작업을 백그라운드로 이동시킨다.
 */
public class RunListFragmnet extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int REQUSET_NEW_RUN = 0;

//    private RunDatabaseHelper.RunCursor mRunCursor;

    /*    *****LoaderCallbacks의 콜백 메소드*****  */
    /*
    로더를 생성할 필요가 있을 때 LoaderManager에 의해 호출된다.
    같은 타입의 로더를 하나이상 갖고 있어서 그럿들을 구분할 필요가 있을 때 id인자가 유용하다
    Bundle인자는 전달되는 어떤 인자들도 저장할 수 있다.
     */
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        //이동 리스트 데이터들만 로드한다.
        return new RunListCursorLoader(getActivity());
    }

    /*
    백그라운드 쓰레드에서 데이터 로드가 끝나면 main쓰레드에서 호출된다.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //이 커서와 연관된 어댑터를 생성한다.
        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), (RunDatabaseHelper.RunCursor)cursor);
        setListAdapter(adapter);
    }

    /*
    사용 가능한 데이터가 더이상 없을 때 호출
    안전 측면을 위해 리스트 어댑터를 null로 설정
     */
    @Override
    public void onLoaderReset(Loader loader) {
        //어댑터를 통해서 커서의 사용을 중단시킨다.
        setListAdapter(null);
    }
    /*    ******************************************************************************  */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //이동 데이터들을 쿼리한다.
//        mRunCursor = RunManager.getInstance(getActivity()).queryRuns();
        //현재 커서를 가리키는 어댑터를 생성한다.
//        RunCursorAdapter adapter = new RunCursorAdapter(getActivity(), mRunCursor);
//        setListAdapter(adapter);
        //이동 리스트의 데이터들을 로드하기 위해 로더를 초기화한다.
        getLoaderManager().initLoader(0, null, this);
    }

//    @Override
//    public void onDestroy() {
//        mRunCursor.close();
//        super.onDestroy();
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_run:
                Intent intent = new Intent(getActivity(), RunActivity.class);
                startActivityForResult(intent, REQUSET_NEW_RUN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUSET_NEW_RUN == requestCode) {
//            mRunCursor.requery();  //main쓰레드에서 커서를 다시 쿼리하는 것은 바람직하지 않다.
//            ((RunCursorAdapter) getListAdapter()).notifyDataSetChanged();
            //새로운 이동 데이터를 얻기 위해 로더를 다시 시작시킨다.
            getLoaderManager().initLoader(0, null, this);
        }
    }

    /*
    리스트의 항목 선택시 RunActivity 론칭
    run테이블의 ID열이름을 _id로 주었기 때문에 CursorAdapter가 그것을 감지하고 id매개변수로 준다.
     _id는 데이터를 추가할 때 값이 자동 증가되는 기본키이기 때문에
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //id인자는 이동(Run) ID가 될 것이다. 이것은 CursorAdapter가 제공한다.
        Intent intent = new Intent(getActivity(), RunActivity.class);
        intent.putExtra(RunActivity.EXTRA_RUN_ID, id);
        startActivity(intent);
    }

    /*
        DB에서 쿼리된 데이터를 UI뷰에 제고아는 역할로는 RunCursor가 적합하지 않다.
         CursorAdapter가 있다. 뷰를 생성하고 재사용하는 로직을 처리해준다.
        */
    private static class RunCursorAdapter extends CursorAdapter {

        private RunDatabaseHelper.RunCursor mRunCursor;

        public RunCursorAdapter(Context context, RunDatabaseHelper.RunCursor cursor) {
            //CursorAdapter의 생성자에서는 3개의 인자를 받는다.
            //Context객체, Cursor객체, 정수플래그(대부분 deprecated되어 여기서는 0을 전달)
            super(context, cursor, 0);
            mRunCursor = cursor;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //레이아웃 인플레이터를 사용해서 한 행의 뷰를 얻는다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        /*
        CursorAdapter에 의해 호출
        CursorAdapter가 커서의 한 행 데이터로 뷰를 구성하고자 할 때 호출
        newView()에서 이전에 반환된 적이 있는 View에 대해서는 이 메소드가 항상 호출
         */
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            //현재 행의 이동(Run) 객체를 얻는다.
            Run run = mRunCursor.getRun();

            //시작 일자를 보여주는 텍스트 뷰를 설정한다.
            TextView startDateTextView = (TextView) view;
            String cellText = context.getString(R.string.cell_text, run.getStartDate());
            startDateTextView.setText(cellText);
        }
    }

    private static class RunListCursorLoader extends SQLiteCursorLoader{

        public RunListCursorLoader(Context context) {
            super(context);
        }

        @Override
        protected Cursor loadCursor() {
            //이동 리시트 데이터들을 쿼리한다.
            return RunManager.getInstance(getContext()).queryRuns();
        }
    }
}
