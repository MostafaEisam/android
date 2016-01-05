package com.example.dong.secondapp;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Dong on 2015-05-26.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    //폰이나 태블릿이나 똑같이 두패널 레이아웃을 인플레이트한다.
    //앨리어스 리소스(alias resource)를 사용해 해결한다.
    @Override
    protected int getLayoutResId() {
        //return R.layout.activity_twopane;
        return R.layout.activity_masterdetail;
    }

    /*
    CrimeListFragment의 onListItemClick()에서는 이 메소드를 호출
    2가지를 처리해야한다.
    1. 만일 폰 인터페이스를 사용 중이라면 새로운 CrimePagerActivity를 시작시킨다.
    2. 만일 태블릿 인터페이스를 사용 중이라면 CrimeFragment를 detailFragmentContainer에 넣는다.
    */
    @Override
    public void onCrimeSelected(Crime crime) {
        //1. 만일 폰 인터페이스를 사용 중이라면 새로운 CrimePagerActivity를 시작시킨다.
        if (findViewById(R.id.detailFragmentContainer) == null) {
            //CrimePagerActivity의 인스턴스를 시작시킨다.
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
            startActivity(intent);
        } else {
            //2. 만일 태블릿 인터페이스를 사용 중이라면 CrimeFragment를 detailFragmentContainer에 넣는다.
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if (oldDetail != null) {
                ft.remove(oldDetail);
            }

            ft.add(R.id.detailFragmentContainer, newDetail);
            ft.commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
}
