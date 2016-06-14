package me.opklnm102.layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * 트윈 애니메이션은 적용 대상에 따라 뷰 애니메이션과 레이아웃 애니메이션으로 구분
 * 뷰 애니메이션 - 대상이 뷰
 * 레이아웃 애니메이션 - 레이아웃내 개별 항목이 대상. 주로 리스트뷰, 그리드뷰에 사용
 *                      이 둘은 차일드 항목의 형태가 일정하므로 애니메이션을 적용하기에 적합
 *                      개별 항목이 순차적으로 애니메이션되므로 적용 순서나 시작 시간 등의 추가 속성이 필요
 *                      이를 지정하는 객체가 LayoutAnimationController.
 *                      LayoutAnimationController(Animation animation, float delay) - 적용할 애니메이션과 지연시간
 *                      각 항목의 애니메이션 시작 시점은 지속시간 x 인덱스 x 지연시간으로 정해진다.
 *                      ex) 인덱스 0 - 1000 x 0 x 0.5 = 0
 *                          인덱스 1 - 1000 x 1 x 0.5 = 500
 *                          인덱스 2 - 1000 x 2 x 0.5 = 1000
 *                      지연시간이 1이면 하나씩 순차적으로 실행. 0.5면 앞 항목을 반쯤 진행했을 때 다음 애니메이션이 시작
 *                      즉, 지연시간은 항목간의 애니메이션이 중첩되는 비율을 지정
 *                      void setOrder(int order) - 애니메이션 적용 순서를 변경. (default, ORDER_NORMAL)
 *
 *                      북레터 메인화면의 애니메이션은 ORDER_RANDOM으로 LayoutAnimationController를 이용하여 구현되었을 것으로 추정
 */
public class ListAnimActivity extends AppCompatActivity {

    ListView lv;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_list);

        adapter = ArrayAdapter.createFromResource(this, R.array.list_item, android.R.layout.simple_list_item_1);
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);

        AnimationSet animationSet = new AnimationSet(true);
        Animation rtl = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f
                );
        rtl.setDuration(1000);
        animationSet.addAnimation(rtl);

        Animation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setDuration(1000);
        animationSet.addAnimation(alpha);

        LayoutAnimationController animationController = new LayoutAnimationController(animationSet, 0.5f);
        lv.setLayoutAnimation(animationController);
    }
}
