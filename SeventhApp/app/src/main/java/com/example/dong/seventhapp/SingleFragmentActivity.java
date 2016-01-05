package com.example.dong.seventhapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Dong on 2015-05-26.
 */
//SingleFragmentActivity의 서브 클래스는 createFragment()를 구현하여 액티비티가 호스팅하는 프래그먼트의 인스턴스를 반환해야 한다.
public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();  //프래그먼트 인스턴스 생성에 사용

    //인플레이트할 레이아웃의 ID를 반환
    //서브 클래스에서 다른 레이아웃을 반환하기 위해 오버라이딩 가능 -> 유연성
    protected int getLayoutResId(){
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());

        //코드에서 프래그먼트를 액티비티에 추가하려면 액티비티의 FragmentManager를 명시적으로 호출해야한다.
        FragmentManager fm = getSupportFragmentManager();  //FragmentManager인스턴스를 얻는 것.
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);   //FragmentManager로부터 CrimeFragment를 요청

        if(fragment == null){
            fragment = createFragment();
            //프래그먼트 트랜잭션을 생성, 커밋
            //프래그먼트 트랜잭션 -> 프래그먼트를 리스트에 추가(add), 삭제(remove), 첨부(attach), 분리(detach), 변경(replace)하는데 사용
            //프래그먼트를 사용해서 런타임시에 화면을 구성 또는 재구성하는 방법의 핵심
            //fm.beginTransaction() -> FragmentTransaction의 인스턴스를 생성하여 반환
            //FragmentTransaction는 플루언트 인터페이스(fluent interface)를 사용
            //FragmentTransaction을 구성하는 메소드들이 void대신 FragmentTransaction객체를 반환하기 때문에 연결해서 호출가능
            fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
            //컨테이너 뷰 ID -> 액티비티 뷰의 어디에 프래그먼트 뷰가 나타나야 하는지를 FragmentManager에게 알려준다.
            //                  FragmentManager의 리스트에서 프래그먼트를 고유하게 식별하는데 사용
            //새로 생성된 Fragment객체 -> 컨테이너에 붙일 객체
        }
    }
}
