package opklnm102.me.datewidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateWidget extends AppWidgetProvider {

    String[] strDayOftheWeek = {"일", "월", "화", "수", "목", "금", "토"};
    final static String ACTION_DISPLAY_FULLTIME = "DateWidget.DisplayFullTime";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i=0; i<appWidgetIds.length; i++){
            RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.datewidget);
            GregorianCalendar cal = new GregorianCalendar();
            String date = String.format("%d월 %d일", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
            remote.setTextViewText(R.id.textView_date, date);
            remote.setTextViewText(R.id.textView_dayOftheWeek, strDayOftheWeek[cal.get(Calendar.DAY_OF_WEEK) - 1]);

            Intent intent = new Intent(context, DateWidget.class);
            intent.setAction(ACTION_DISPLAY_FULLTIME);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remote.setOnClickPendingIntent(R.id.linearLayout_main, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remote);
        }

        //모든 인스턴스의 정보가 같을 때 갱신
//        appWidgetManager.updateAppWidget(appWidgetIds, remote);

        //각 인스턴스마다 보여주는 정보가 다를 때 갱신
//        for(int i=0; i<appWidgetIds.length; i++){
//            appWidgetManager.updateAppWidget(appWidgetIds[i], remote);
//        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //커스텀 액션 처리
        String action = intent.getAction();
        if(action != null && action.equals(ACTION_DISPLAY_FULLTIME)){
            GregorianCalendar cal = new GregorianCalendar();
            String strFullTime = String.format("%d년 %d월 %d일 %d:%d:%d",
                    cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
            Toast.makeText(context, strFullTime, Toast.LENGTH_SHORT).show();
            return;
        }

        super.onReceive(context, intent);
    }
}
