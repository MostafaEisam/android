package me.opklnm102.exalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DisplayScoreReceiver extends BroadcastReceiver {
    public DisplayScoreReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "DisplayScoreReceiver", Toast.LENGTH_LONG).show();
    }
}
