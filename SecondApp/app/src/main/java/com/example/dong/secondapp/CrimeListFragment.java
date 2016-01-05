package com.example.dong.secondapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Dong on 2015-05-26.
 */
//리스트뷰에 보여줄게 없을 경우 내포된 스피너가 보인다.
public class CrimeListFragment extends ListFragment {
    private static final String TAG = "CrimeListFragment";

    private ArrayList<Crime> crimes;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /*
    호스팅 액티비티에서 구현할 필요가 있는 인터페이스
    Callbacks에 호스팅 액티비티가 처리할 필요가 있는 일을 정의
    콜백 인터페이스를 사용하면 어떤 액티비티가 호스팅하는지 프래그먼트가 알 필요 없이 자신을
    호스팅하는 액티비티의 메소드들을 호출할 수 있다.
     */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    //프래그먼트가 액티비티에 첨부될 때(프래그먼트의 유보 여부와 관계없이)호출
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    //분리될 때 호출
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);  //옵션메뉴 콜백호출을 받을 필요가 있다는 것을 FragmentManager에 알려주는 코드

        setRetainInstance(true);  //프래그먼트 유보시키기
        mSubtitleVisible = false;

        //getActivity() -> Fragment의 편의 메소드, 호스팅하는 액티비티를 반환하여 프래그먼트가
        //더 많은 액티비티의 일을 처리할 수 있게 해준다.
        getActivity().setTitle(R.string.crimes_title);  //액션바에 제목 설정

        //CrimeListFragment에서는 CrimeLab에 있는 ArrayList를 액세스해야 한다.
        //CrimeLab의 싱글톤 인스턴스를 참조 받은 다음 ArrayList의 객체를 참조
        crimes = CrimeLab.get(getActivity()).getCrimes();

        //첫번째 인자는 두번째 인자인 리소스ID의 사용에 필요한 Context객체
        //리소스ID는 뷰 객체를 생성하기 위해 ArrayAdapter가 사용할 레이아웃
        //세번째 인자는 우리 객체들이 저장된 데이터 셋
        //android.R.layout.simple_list_item_1 -> SDK에서 제공하는 사전 정의된 레이아웃(루트로 TextView)
        //ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),android.R.layout.simple_list_item_1,crimes);

        //커스텀 어댑터 사용
        CrimeAdapter adapter = new CrimeAdapter(crimes);

        setListAdapter(adapter);  //ListFragment의 편의 메소드. CrimeListFragment의 ListView에서 사용하는 어댑터를 설정하기 위해 사용
        // ArrayAdapter<T>.getView()에 디폴트로 구현되있는 코드에서 toString()코드에 의존하여 제목만 나온다.
    }

    @TargetApi(11)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = super.onCreateView(inflater, container, savedInstanceState); 대신 밑의 코드 추가
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(android.R.id.empty);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            //프로요와 진저브레드 버전에서는 플로팅 컨텍스트 메뉴를 사용
            registerForContextMenu(listView);  //리스트뷰에 컨텍스트 메뉴 등록
        }else{
            //허니콤 이상 버전에서는 컨텍스트 액션바를 사용
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

            //AbsListView.MultiChoiceModeListener인터페이스를 구현하는 ListView의 리스너를 설정
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                //뷰가 선택 또는 선택취소되었을 때 재호출되는 콜백메소드
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    //필요하지만 여기서는 특별히 구현할게 없다.
                }

                //ActionMode.Callback 인터페이스의 메소드들
                //화면이 컨텍스트 액션 모드로 되면 ActionMode클래스의 인스턴스가 생성된다.
                //그리고 ActionMode.Callback의 메소드들이 ActionMode 생명주기의 서로 다른 시점에서 재호출된다.

                //ActionMode인스턴스가 생성될 떄 호출.
                //컨텍스트 액션 바에 나타나는 컨텍스트 메뉴 리소스를 이 메소드에서 인플레이트.
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    //액티비티가 아닌 액션모드로부터 MenuInflater객체를 얻는다.
                    //컨텍스트 액션바를 구성하기 위한 상세 정보를 액션 모드가 갖기 때문
                    //ex)컨텍스트 액션바의 제목을 지정
                    //ActionMode.setTitle()를 호출 -> 이경우 액티비티의 메뉴 인플레이터는 그 제목에 대해 모른다.
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.crime_list_item_context,menu);
                    return true;  //false면 ActionMode의 생성이 중단될 것이다.
                }

                //onCreateActionMode()다음에 호출.
                //그리고 기존의 컨텍스트 액션바가 새로운 데이터로 다시 채워질 필요가 있을 때 언제든지 호출
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    //필요하지만 여기서는 특별히 구현할게 없다.
                    return false;
                }

                //사용자가 액션을 선택하면 호출.
                //메뉴리소스에 정의된 컨텍스트 액션에 대한 응답을 이 메소드에서 한다.
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
                            CrimeLab crimeLab = CrimeLab.get(getActivity());
                            for(int i=adapter.getCount() - 1; i>=0; i--){
                                if(getListView().isItemChecked(i)){
                                    crimeLab.deleteCrime(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                //액션 모드 취소 or 선택된 액션의 응답이 끝나서 ActionMode가 소멸될 때 호출
                //디폴트로 해당 뷰(들)가 선택되지 않은 상태가 된다.
                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    //필요하지만 여기서는 특별히 구현할게 없다.
                }
            });
        }

        listView.setEmptyView(relativeLayout);  //텅빈뷰 셋

        Button btn = (Button) v.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
                startActivityForResult(i, 0);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                appCompatActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }
        return v;
    }

    //리스트 다시 로딩하기위해 오버라이딩
    //onStart()가 아닌 onResume()에서 하는 것은 전면에 없다고 중단될거라고 가정할 수 없고
    //여기가 제일 안전하기 때문
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //getAdapter()는 ListFragment의 편의 메소드, 그 ListFragment의 리스트뷰에 설정된 어댑터를 반환
        //Crime c = (Crime)(getListAdapter()).getItem(position);

        //커스텀 어댑터 사용
        //CrimeAdapter로 캐스팅하므로 타입확인의 장점. Crime만 저장하므로 캐스팅x
        Crime c = ((CrimeAdapter) getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked");

//        //CrimeActivity를 시작한다. 명시적 인텐트 생성
//        //getActivity()로 자신을 호스팅하는 액티비티를 Context로 전달
//        //Intent intent = new Intent(getActivity(),CrimeActivity.class);
//
//        //이 Crime객체를 전달하여 CrimePagerActivity를 시작시킨다.
//        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
//
//        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
//        startActivity(intent);
        //모든 콜백을 호출하기
        mCallbacks.onCrimeSelected(c);
    }

    //Crime의 국한된 리스트 항목들을 보여주기 위해 만들어졌다.
    //리스트 항목들의 데이터는 Crime의 접근자 메소드를 사용해서 받아야한다.
    //Crime객체와 상호 동작하는 방법을 아는 새로운 어댑터 필요
    private class CrimeAdapter extends ArrayAdapter<Crime> {

        public CrimeAdapter(ArrayList<Crime> crimes) {
            //Crime객체들이 저장된 데이터셋을 올바르게 연결하기위해 수퍼클래스의 생성자를 호출
            //사전 정의된 레이아웃을 사용하지 않을 것이므로 0을 전달
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //convertView는 리스트항목 하나를 보여주는 뷰객체 -> 이를 이용하여 뷰를 재사용

            //지정된 뷰가 없다면 하나를 인플레이트하여 생성
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
            }

            //이 Crime객체의 뷰를 구성한다.
            Crime c = getItem(position);  //어댑터의 getItem()으로 리스트의 현재 위치에 있는 Crime객체 가저옴

            //내용을 채우고
            TextView titleTv = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTv.setText(c.getTitle());
            TextView dateTv = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTv.setText(c.getDate().toString());
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((CrimeAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void updateUI(){
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    //옵션메뉴 인플레이트
    //Fragment를 호스팅하는 액티비티가 OS로부터 자신의 onCreateOptionsMenu()를 호출 받았을 때
    //FragmentManager는 Fragment.onCreateOptionMenu(Menu, MenuInflater)를 호출하는 책임을 갖는다.
    //단, 프래그먼트가 onCreateOptionMenu() 호출을 받아야 한다는 것을 명시적으로 FragmentManager에
    //알려주어야 한다. -> setHasOptionMenu(boolean hasMenu)를 호출
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //슈퍼클래스에서 정의된 옵션메뉴의 기능도 동작가능하게 함, Fragment는 아무내용없다.
        super.onCreateOptionsMenu(menu, inflater);
        //메뉴파일의 리소스ID를 인자로 전달, 정의된 메뉴 항목들로 Menu인스턴스가 채워진다.
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null)
            showSubtitle.setTitle(R.string.hide_subtitle);
    }

    //선택한 메뉴항목 처리후 더 이상의 처리가 필요 없다는 것을 나타내는 boolean 리턴
    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
//                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
//                i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
//                startActivityForResult(i, 0);
                //모든 콜백 호출하기
                ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_item_show_subtitle:
                AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                if (appCompatActivity.getSupportActionBar().getSubtitle() == null) {
                    appCompatActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
                    item.setTitle(R.string.hide_subtitle);
                    mSubtitleVisible = true;
                } else {
                    appCompatActivity.getSupportActionBar().setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                    mSubtitleVisible = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //onCreateOptionMenu()와 달리 이 메뉴의 콜백메소드에는 MenuInflater의 인스턴스가 인자로 전달되지 않는다.
        //getActivity().getMenuInflater()를 호출해 MenuInflater객체를 얻는다.
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
        Crime crime = adapter.getItem(position);

        switch(item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
