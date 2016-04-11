
package me.opklnm102.exalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //원칙적으로는 notification을 표시하는게 맞다.
        Toast.makeText(context, "It's time to start", Toast.LENGTH_LONG).show();
    }
}
