package opklnm102.me.batterywidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * BatteryWidget --------> 서비스 -------------> 배터리BR
 *  서비스 기동         배터리BR 등록       잔량조사, 앱위젯에 출력
 */
public class BatteryWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(context, BatteryWidgetService.class);
        context.startService(intent);

        //배터리의 남은 양 계산해 갱신
        //앱위젯 자체가 BR이라 안에서 BR을 등록할 수 없다. -> BR에서 BR을 등록하면 무한루프의 위험. 허용X
        //중간에 서비스를 두고 대신 배터리 BR을 등록
//        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//        String str = batteryIntent.getIntExtra("level", -1) + "%";
//
//        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.batterywidget);
//        remoteViews.setTextViewText(R.id.textView_battery, str);
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Intent intent = new Intent(context, BatteryWidgetService.class);
        context.startService(intent);
    }
}
