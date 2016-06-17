package opklnm102.me.exappwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

/**
 * AppWidget
 * 특수한 실행 형태
 * 다른 앱의 일부 영역(보통 홈화면)을 차지한 채 주기적으로 갱신되는 조그만 앱
 * 4.2부터 잠금화면에도 배치 가능
 * 항상 실행중, 면적을 적게 차지한다는 점에서 실용적
 * 구조가 독특 -> 제대로 만드려면 고급기법 총동원해야할 정도로 선수지식이 많다.
 * 호스트의 영역일부를 임대해서 빌붙는 처지다 보니 자유도가 떨어지고 단순출력에도 제약이 많고
 * 호스트와의 통신도 복잡
 *
 * 같은 AppWidget을 여러개 실행 가능 -> 각각을 인스턴스라고 한다. 고유ID를 발급하여 관리
 * 여러개의 인스턴스를 설치해도 잘 실행될 수 있는 구조로 만들어야 한다. -> 정교한 코드 요구
 *
 * AppWidgetManager
 * AppWidget 관리
 * 설치된 AppWidget의 목록을 가지며 AppWidget과 관련된 정보를 조사하고 미리 지정된 시간이 될 때마다
 * AppWidget에게 신호를 보내 상태를 갱신
 * 콜백메소드의 인수로 전달되지만 필요할 경우
 * static AppWidgetManager getInstance(Context context) 호출
 *
 * AppWidget관련 정보 조사 메소드
 * Lisst<AppWidgetProviderInfo> getInstalledProviders() - 설치된 모든 앱위젯 목록
 * AppWidgetProviderInfo getAppWidgetInfo(int appWidgetId) - 특정ID의 위젯 정보
 * int[] getAppWidgetIds(ComponentName provider) - 특정 컴포넌트가 제공하는 위젯 목록
 *
 * AppWidgetProviderInfo - 앱위젯의 여러가지 속성정의, 크기, 갱신주기, 설정액티비티 등
 *
 * 앱 위젯은 일반앱에는 필요없는 속성이 있다.
 * AppWidgetProviderInfo클래스로 기술
 * xml문서의 <appwidget-provider>앨리먼트로 작성하여 매니페스트에 메타데이터로 포함 시킴
 *
 * minWidth, minHeight
 * 앱위젯이 차지할 화면상의 디폴트 크기 지정
 * 4.0 이전
 *      (셀수 * 74) - 2dp
 * 4.0 이후
 *      70 * 셀수 - 30dp
 *
 * resizeMode
 * 앱위젯을 배치한 후 크기 조정 가능한 방향 지정
 * none - 고정
 * horizontal - 수평
 * vertical - 수직
 * horizontal|vertical - 양방향
 *
 * minResizeWidth, minResizeHeight
 * 크기조정이 가능한 최소, 최대(제한X) 크기 지정
 * resizeMode가 none면 무소용
 * 자유롭게 크기조정이 가능하더라도 최소 크기를 지정하고 싶을 때 사용
 * minWidth = 180, minResizeWidth = 110이면 처음에는 3셀폭, 크기를 조정하여 2셀로 줄일 수 있다는 뜻
 *
 * previewImage
 * 앱위젯의 미리보기 이미지 설정
 *
 * autoAdvanceViewId
 * 앱위젯 호스트에 의해 자동으로 진행되어야 할 서브뷰의 ID지정
 * 컬랙션 뷰에서 지정한 시간마다 자동으로 다음 뷰로 전환할 때 사용
 *
 * updatePeriodMillis
 * 호스트는 일정간격으로 앱위젯에게 신호를 보내 정보를 갱신할 기회 제공
 * 1/1000초 단위 -> 정확도가 높진 않다. 자주갱신할 필요X
 * ex) 날씨, 뉴스는 2시간에 1번 갱신해도 충분
 * 배터리 성능에 직접적인 영향을 미치므로 가급적이면 길게 설정
 * 너무 자주 갱신하면 그때마다 CPU가 슬립모드에서 깨어나 동작하므로 쉴틈이 없다
 * 1.6부터 최소갱신 주기가 30분으로 강제
 * 더 짧은 주기로 갱신되어야 하는 경우는 타이머, 알람, 백그라운드 서비스 사용
 * 알람 - 주기를 자유롭게 선택. 장비가 슬립일 때는 동작X. -> 배터리 걱정X
 * 갱신주기를 0으로 설정하면 호스트가 갱신신호를 보내지 않아 명령만 입력받는 위젯이 된다.
 * 갱신주기는 1번째 인스턴스 기준
 *
 * initialLayout
 * 앱위젯이 호스트에 있는 위젯을 직접 조작할 수 없으므로 RemoteViews객체로 레이아웃 정보만 전달
 * Layout - Linear, Relative, Frame
 * View - Button, TextView, ImageView, ImageButton, ProgressBar
 * Collection View - ListView, GridView, StackView, AdapterViewFlipper
 * 기타 - AnalogClock, Chronometer, ViewFliper -> 실용성 떨어짐
 * 만 지원
 *
 * configure
 * 옵션을 편집할 액티비티 지정
 * 모양이나 동작방식을 지정하는 옵션 설정 -> 설정액티비티에서
 * 설정이 필요없는 앱위젯은 생략
 *
 * widgetCategory
 * 4.2부터 추가
 * 홈화면용(home_screen)인지 잠금화면용(keyguard)인지 지정. 둘다 가능
 *
 * initalKeyguardLayout
 * 4.2부터 추가
 * widgetCategory에 keyguard가 지정되있을 때만 유효
 * 잠금화면용 레이아웃 지정
 *
 * 앱위젯 라이프 사이클
 * 앱위젯 개발이 어려운 이유는 앱위젯프로세스와 앱위젯을 보여주는 호스트 프로세스가 분리되어 있기 때문
 *
 *     ----------------- 갱신 알림 -------------->
 * 홈                                                 앱위젯
 *     <-- 그려줌 --------------------- 그리기 요청
 *
 *     <-- 이벤트 발생시 처리 ------ 이벤트 처리요청
 *
 * 출력 - RemoteViews를 전달해 호스트에 요청
 * 이벤트 - 앱위젯 표면의 이벤트는 호스트가 받으며 이때마다 약속된 방법으로 앱위젯에게 신호를 보내 알려준다.
 *          이벤트를 처리하려면 어떤 이벤트에 대해 어떤 신호를 보내달라고 미리 부탁
 *
 * 앱위젯은 BroadcastReceiver
 * ACTION_APPWIDGET_ENABLED
 * 앱위젯의 1번째 인스턴스가 설치될 때, 장비 부팅할 때 전달
 * 모든 인스턴스가 공통적으로 필요로 하는 전역적인 초기화 수행
 * ex) DB생성, 파일목록 조사
 *
 * ACTION_APPWIDGET_DISABLED
 * 마지막 인스턴스가 제거될 때 전달
 * 전역적인 초기화를 해제
 * ex) DB, 임시파일 삭제
 *
 * ACTION_APPWIDGET_DELETE
 * 인스턴스 일부 삭제시 전달
 * 인텐트로 삭제되는 인스턴스의 ID배열이 전달
 * 개별 인스턴스가 사용하던 자원 반납, 불필요한 정보 제거
 *
 * ACTION_APPWIDGET_UPDATE -> 반드시 처리
 * 개별 앱위젯을 갱신할 때마다 전달(갱신주기, 장비부팅)
 * 출력할 정보를 재조사하고 자신을 다시 그린다.
 * 인텐트로 갱신이 필요한 모든 인스턴스ID 배열이 전달
 *
 * ACTION_APPWIDGET_OPTIONS_CHANGED
 * 4.1에서 추가
 * 앱위젯의 크기나 옵션이 바뀌었을 때 전달
 * 새로운 옵션 정보는 Bundle로 전달
 * 커스텀 옵션을 사용한다면 이 방송에서 새옵션을 읽어 적용
 *
 * 최초 설치 - ENABLED - 동작준비, 필요한 전역자원 할당
 *             UPDATE - 초기화, 초기상태를 조사하여 출력
 * 갱신 주기시마다 - UPDATE - 현재상태를 조사하여 갱신
 *                   UPDATE - 현재상태를 조사하여 갱신
 *                   ...
 * 장비 부팅시 - ENABLED - 동작준비, 필요한 전역자원 할당
 *               UPDATE - 초기화, 초기상태를 조사하여 출력
 * 앱위젯 제거 - DELETE - 인스턴스가 사용하던 자원 해제
 * 마지막 인스턴스 제거 - DISABLED - 전역 자원 해제
 *
 * AppWidgetProvider
 * 앱위젯 도우미 클래스
 * BroadcastReceiver 상속
 * onReceive() - 위젯을 위한 방송을 받아 각 메소드로 분배
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //설치된 앱위젯 정보 덤프
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        List<AppWidgetProviderInfo> appWidgetProviderInfoList = appWidgetManager.getInstalledProviders();

        String strResult = "count = " + appWidgetProviderInfoList.size() + "\n";
        for(AppWidgetProviderInfo info : appWidgetProviderInfoList){
            strResult += info.toString() + "\n\n";
        }
        TextView tvResult = (TextView) findViewById(R.id.textView_result);
        tvResult.setText(strResult);
    }
}
