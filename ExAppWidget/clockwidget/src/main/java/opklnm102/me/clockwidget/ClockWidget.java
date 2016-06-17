package opklnm102.me.clockwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 30분 보다 자주 갱신해야하는 위젯
 * 타이머나 알람을 사용하여 직접 갱신주기 컨트롤
 *
 * 타이머의 문제 -> 슬립모드에서도 작동 -> CPU동작, 배터리 과소모
 * 대안 -> 알람 사용
 *
 * 알람
 * 주기적으로 호출
 * 슬립모드에 들어가면 장비가 깨어날 떄까지 실행이 연기
 * 정확한 시간을 갱신하면서 배터리를 절약하는 합리적인 방법
 * 알람도 1초단위로 갱신하면 슬립모드로 안들어간다. -> 1분정도는 되야..
 */
public class ClockWidget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockWidget.class);
        intent.setAction("ClockUpdate");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000, pendingIntent);  //현재시간부터 1초단위로 알람
    }

    @Override
    public void onDisabled(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockWidget.class);
        intent.setAction("ClockUpdate");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.cancel(pendingIntent);
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals("ClockUpdate")){
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thiswidget = new ComponentName(context, ClockWidget.class);
            int[] ids = appWidgetManager.getAppWidgetIds(thiswidget);
            onUpdate(context, appWidgetManager, ids);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.clockwidget);
        GregorianCalendar cal = new GregorianCalendar();
        String strNow = String.format("%d:%d:%d",
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND));

        remote.setTextViewText(R.id.textView_nowtime, strNow);
        appWidgetManager.updateAppWidget(appWidgetIds, remote);

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
    }

//    AlarmManager mAlarmManager;
//    PendingIntent mPendingIntent;

//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//
//        Timer timer = new Timer();
//        ClockTask task = new ClockTask(context, appWidgetManager, appWidgetIds);
//
//        //1초 간격의 타이머 설치
//        timer.scheduleAtFixedRate(task, 0, 1000);
//    }
//
//    class ClockTask extends TimerTask {
//
//        Context mContext;
//        AppWidgetManager mAppWidgetManager;
//        int[] mIds;
//
//        public ClockTask(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//            mContext = context;
//            mAppWidgetManager = appWidgetManager;
//            mIds = appWidgetIds;
//        }
//
//        //타이머가 초단위로 실행되면서 현재 시간을 조사하여 출력
//        @Override
//        public void run() {
//            RemoteViews remote = new RemoteViews(mContext.getPackageName(), R.layout.clockwidget);
//            GregorianCalendar cal = new GregorianCalendar();
//            String strNow = String.format("%d:%d:%d",
//                    cal.get(Calendar.HOUR),
//                    cal.get(Calendar.MINUTE),
//                    cal.get(Calendar.SECOND));
//
//            remote.setTextViewText(R.id.textView_nowtime, strNow);
//            mAppWidgetManager.updateAppWidget(mIds, remote);
//
//            AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
//            am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
//        }
//    }
}
