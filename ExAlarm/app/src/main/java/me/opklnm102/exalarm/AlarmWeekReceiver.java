package me.opklnm102.exalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.Calendar;

public class AlarmWeekReceiver extends BroadcastReceiver {

    public static final String TAG = AlarmWeekReceiver.class.getSimpleName();

    public AlarmWeekReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, " onReceivee");

        boolean[] week = intent.getBooleanArrayExtra("weekday");

        Calendar calendar = Calendar.getInstance();

        if (!week[calendar.get((Calendar.DAY_OF_WEEK))])
            return;

        //효과음 재생
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("chicks.mp3");
            mMediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
