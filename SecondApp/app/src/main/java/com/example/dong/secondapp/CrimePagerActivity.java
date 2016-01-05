package com.example.dong.secondapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Dong on 2015-05-27.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private ViewPager viewPager;
    private ArrayList<Crime> crimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //프래그먼트 컨테이너로 사용되는 뷰라면 어떤 것이든
        //반드시 리소스ID를 가져야만 FragmentManager가 관리할 수 있다.
        //1. ViewPager의 리소스ID를 생성
        //2. ViewPager의 인스턴스를 생성하고 그것을 viewPager 변수에 지정
        //3. 리소스 ID를 ViewPager인스턴스에 지정하여 그 인스턴스를 구성
        //4. ViewPager를 우리 액티비티의 콘텐트뷰로 설정
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.viewPager);
        setContentView(viewPager);

        crimes = CrimeLab.get(this).getCrimes();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        //FragmentStatePagerAdapter는 각 페이지를 관리하기 위해 액티비티가 아닌 프래그먼트를 사용하는
        //PagerAdapter를 구현한 것이며, 프래그먼트의 상태도 저장하고 복원할 수 있다.
        //FragmentStatePagerAdapter의 역할
        //우리가 반환하는 프래그먼트들을 액티비티에 추가 한다. 프래그먼트들의 뷰들이 올바른 위치에
        //올 수 있도록 ViewPager가 그것들을 식별하는 것을 도와준다.
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

            @Override
            public int getCount() {
                return crimes.size();
            }

            @Override
            public Fragment getItem(int pos) {
                Crime crime = crimes.get(pos);
                return CrimeFragment.newInstance(crime.getId());
            }
        });

        //선택한 요소를 ViewPager의 첫화면으로 보여준다.
        final UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for(int i=0; i<crimes.size(); i++){
            if(crimes.get(i).getId().equals(crimeId)){
                viewPager.setCurrentItem(i);
                break;
            }
        }

        //액션바에 나타나는 액티비티의 제목을 현재 Crime의 제목으로 바꾼다.
        //ViewPager가 보여주는 현재 페이지의 변경사항들을 리스닝하는 방법이 OnPageChangeListener다.
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //페이지가 이동되는 위치를 알려줌
            @Override
            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            //현재 선택된 페이지
            @Override
            public void onPageSelected(int pos) {
                Crime crime = crimes.get(pos);
                if(crime.getTitle() != null){
                    setTitle(crime.getTitle());
                }
            }

            //페이지의 애니메이션 상태(드래그됨, 변동없음, 비동작)을 알려준다.
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
    }
}
