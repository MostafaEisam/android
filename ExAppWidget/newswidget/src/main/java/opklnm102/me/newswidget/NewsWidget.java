package opklnm102.me.newswidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * 정보를 보여준다는 면에서 가장 전형적인 사용 예
 *
 *        --------------------- UPDATE -------------->
 *  설치                                                 초기 설정
 *        <-------- 버튼 클릭시 Change 방송 요청 ------
 *
 *        --------------------- CHANGE -------------->
 *  클릭                                                 뉴스 갱신
 *        <------------------ 그리기 요청 ------------
 */
public class NewsWidget extends AppWidgetProvider {

    final static String ACTION_NEWS_CHANGE = "NewsChange";
    final static String PREF = "NewsWidget";
    final static String TAG = NewsWidget.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d(TAG, "onUpdate, length = " + appWidgetIds.length + ", id = " + appWidgetIds[0]);

        for (int i = 0; i < appWidgetIds.length; i++) {
            updateNews(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    /*
    UPDATE 방송 처리 루틴의 초기화 코드와 설정 액티비티의 초기화 코드가 유사해 메소드로 구현
    외부에서도 호출해야하므로 static
     */
    static void updateNews(Context context, AppWidgetManager appWidgetManager, int widgetId) {
        String[] arrNews = {
                "1. ㅇㅇㅇㅇㅇ",
                "2. ㅇㅇㅇㅇㅇ",
                "3. ㅇㅇㅇㅇㅇ",
                "4. ㅇㅇㅇㅇㅇ",
                "5. ㅇㅇㅇㅇㅇ"
        };

        int newsid = new Random().nextInt(arrNews.length);
        RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.newwidget);
        remote.setTextViewText(R.id.textView_news, arrNews[newsid]);
        SharedPreferences prefs = context.getSharedPreferences(PREF, 0);
        boolean isRed = prefs.getBoolean("red_" + widgetId, false);
        remote.setTextColor(R.id.textView_news, isRed ? Color.RED : Color.BLACK);
        Log.d(TAG, "updateNews, id = " + widgetId);

        //클릭리스너 설정
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsid", newsid);
        //PendingIntent는 굉장히 비싼 장치. 매번생성하지 않고 캐시 유지. 동일한 인텐트 재사용
        //FLAG_UPDATE_CURRENT - 비용이 발생해도 할수없으니 엑스트라 정보 갱신
        PendingIntent pendingIntent = PendingIntent.getActivity(context, widgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remote.setOnClickPendingIntent(R.id.relativeLayout_main, pendingIntent);

        Intent intent2 = new Intent(context, ConfigActivity.class);
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, widgetId, intent2, 0);
        remote.setOnClickPendingIntent(R.id.button_config, pendingIntent2);

        Intent intent3 = new Intent(context, NewsWidget.class);
        intent3.setAction(ACTION_NEWS_CHANGE);
        intent3.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, widgetId, intent3, 0);
        remote.setOnClickPendingIntent(R.id.button_change, pendingIntent3);

        appWidgetManager.updateAppWidget(widgetId, remote);
    }

    //커스텀 방송을 수신해야해 오버라이딩
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //CHANGE 방송은 클릭한 위젯 하나만을 대상
        if(action != null && action.equals(ACTION_NEWS_CHANGE)){
            int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            updateNews(context, AppWidgetManager.getInstance(context), id);
            Log.d(TAG, " onReceive(CHANGE), id = " + id);
            return;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for(int i=0; i<appWidgetIds.length; i++){
            Log.d(TAG, " onDeleted, id = " + appWidgetIds[i]);
            SharedPreferences prefs = context.getSharedPreferences(PREF, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("red_" + appWidgetIds[i]);
            editor.commit();
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, " onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, " onDisabled");
    }
}
