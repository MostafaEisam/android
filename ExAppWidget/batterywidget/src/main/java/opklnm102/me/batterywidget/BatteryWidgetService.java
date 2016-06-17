package opklnm102.me.batterywidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

public class BatteryWidgetService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBRBattery,filter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBRBattery);
    }

    BroadcastReceiver mBRBattery = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                int scale, level, ratio;
                scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                ratio = level * 100 / scale;

                RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.batterywidget);
                remote.setTextViewText(R.id.textView_battery, ratio + "%");
                AppWidgetManager wm = AppWidgetManager.getInstance(BatteryWidgetService.this);
                ComponentName widget = new ComponentName(context, BatteryWidget.class);
                wm.updateAppWidget(widget, remote);
            }
        }
    };
}
