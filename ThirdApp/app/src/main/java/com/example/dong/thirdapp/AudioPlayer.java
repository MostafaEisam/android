package com.example.dong.thirdapp;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Dong on 2015-06-01.
 */
public class AudioPlayer {

    private MediaPlayer mediaPlayer;

    public void stop(){
        if(mediaPlayer != null){
            mediaPlayer.release();  //인스턴스 소멸, 호출 전까지 오디오 디코더HW와 다른 시스템 리소스(모든 앱에 걸쳐 공유)들을 잡고 있다.
            mediaPlayer = null;
        }
    }

    public void play(Context context){
        //제일 앞에서 호출하면 인스턴스를 여러개 생성하는 것을 막는다.
        //사용자가 재생버튼을 두번 클릭할 때를 대비
        //stop();

        if(mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(context, R.raw.one_small_step);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();  //재생이 끝나면 인스턴스 소멸
            }
        });

        mediaPlayer.start();
    }

    public void pause(){
        if(mediaPlayer != null && mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }
}
