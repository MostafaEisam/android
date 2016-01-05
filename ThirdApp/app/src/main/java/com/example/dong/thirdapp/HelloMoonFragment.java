package com.example.dong.thirdapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Dong on 2015-05-31.
 */
public class HelloMoonFragment extends Fragment {
    private AudioPlayer mPlayer = new AudioPlayer();

    private Button playBtn;
    private Button stopBtn;
    private Button pauseBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_hello_moon,container,false);

        playBtn = (Button)v.findViewById(R.id.hellomoon_playButton);
        stopBtn = (Button)v.findViewById(R.id.hellomoon_stopButton);
        pauseBtn = (Button)v.findViewById(R.id.hellomoon_pauseButton);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.play(getActivity());
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {  //프래그먼트 소멸시 플레이 중지
        super.onDestroy();
        mPlayer.stop();
    }
}
