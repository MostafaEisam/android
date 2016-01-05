package com.example.dong.thirdapp;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by Dong on 2015-06-03.
 */
public class VideoFragment extends Fragment {

    //public final static String VIDEO_URL = "raw/apollo_17_stroll.mpg";
    public final static String VIDEO_URL = "http://ia802302.us.archive.org/27/items/Pbtestfilemp4videotestmp4/video_test.mp4";

    private VideoView videoView;

    private Button playBtn;
    private Button stopBtn;
    private Button pauseBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = (VideoView) v.findViewById(R.id.videoView);

        playBtn = (Button) v.findViewById(R.id.playButton);
        stopBtn = (Button) v.findViewById(R.id.stopButton);
        pauseBtn = (Button) v.findViewById(R.id.pauseButton);

        //동영상 경로지정
        //videoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + VIDEO_URL));
        videoView.setVideoURI(Uri.parse( VIDEO_URL));
        Log.i("fd", "1");
        //미디어컨트롤러 추가
        MediaController controller = new MediaController(getActivity().getApplicationContext());
        videoView.setMediaController(controller);
        Log.i("fd", "2");
        //준비하는 과정을 미리함
        videoView.requestFocus();
        Log.i("fd", "3");
        //동영상 재생준비가 완료되었을때를 알수 있는 리스너(실제 웹에서 영상을 다운받아 출력할 때 사용)
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("fd", "4");
                Toast.makeText(getActivity(), "동영상 준비완료", Toast.LENGTH_SHORT).show();
            }
        });

        //동영상 재생이 완료된걸 알수 있는 리스너
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getActivity(), "동영상 재생생완료", Toast.LENGTH_SHORT).show();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();Log.i("fd", "4");
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVideo();Log.i("fd", "4");
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseVideo();Log.i("fd", "4");
            }
        });

        return v;
    }

    @Override
    public void onDestroy() {  //프래그먼트 소멸시 플레이 중지
        super.onDestroy();
        stopVideo();
    }

    private void playVideo() {
        //비디오를 처음부터 재생할땐 0
        videoView.seekTo(0);
        //비디오 재생시작
        videoView.start();
    }

    private void stopVideo() {
        //정지
        videoView.stopPlayback();
        videoView = null;
    }

    private void pauseVideo() {
        //일시 정지
        videoView.pause();
    }
}