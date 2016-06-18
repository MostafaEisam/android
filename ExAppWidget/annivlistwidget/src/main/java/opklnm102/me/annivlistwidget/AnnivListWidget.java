package opklnm102.me.annivlistwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

/**
 * 목록 -> 실시간 업데이트 -> 지속적으로 새로운 정보 공급
 * 백그라운드에서 계속 필요한 데이터를 제공
 * BR만으로는 안됨. 서비스 동원
 *
 * 원격지의 데이터를 필요한 만큼 받아서 리모트 뷰를 제공 -> 특별한 어댑터 필요
 * RemoteViewsService
 *      Service 상속
 *      백그라운드에서 계속 동작
 *      데이터가 필요할 때 마다 어댑터를 통해 최신 데이터 제공(배열, CP, 네트워크)
 *      onGetViewFactory()에서 리모트 어랩터인 RemoteViewsFactory를 생성하여 리턴. 이 어댑터에 의해 데이터가 컬렉션뷰로 공금
 *
 * RemoteViewsFactory
 *      리모트 컬렉션 뷰와 데이터를 연결하는 어댑터 인터페이스
 *      어댑터를 위젯에 맞게 랩핑 -> 사용법 비슷
 *
 * 홈화면처럼 앱위젯을 담는 호스트를 만들어야 한다면 활용할 클래스 2가지
 * 1. AppWidgetHost - 시스템의 앱위젯 서비스와 홈화면 같은 앱위젯 호스트를 연결,
 *                 새로 설치되는 위젯에 id할당, 요청한 만큼의 영역 할당, 위젯 삭제등의 처리
 *
 * 2. AppWidgetHostView - 홈화면의 일부 영역 관리, RemoteViews객체를 받아 위젯 영역 갱신
 *
 * 커스텀 런처, 특정 장비를 위한 쉘 개발시 필요한 기술
 */
public class AnnivListWidget extends AppWidgetProvider {

    public AnnivListWidget(){
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    /*
    데이터를 읽어서 공급하는 처리를 서비스와 어댑터가 대신하므로

     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i=0; i<appWidgetIds.length; i++){
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.annivlistwidget);
            Intent intent = new Intent(context, AnnivListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            remoteViews.setRemoteAdapter(R.id.listView_anniv, intent);  //데이터를 어디서 공급받는지
            remoteViews.setEmptyView(R.id.listView_anniv, R.id.emptyView);  //데이터가 없을 때 어떤 위젯을 보여줄 것인지.
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    //백그라운드에서 동작하는 데이터 공급 서비스
    public static class AnnivListWidgetService extends RemoteViewsService{

        //어댑터 객체 생성하여 리턴
        //intent - 위젯Id, 데이터를 얻는데 필요한 정보 저장
        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new AnnivListFactory(this.getApplicationContext(), intent);
        }
    }
}
