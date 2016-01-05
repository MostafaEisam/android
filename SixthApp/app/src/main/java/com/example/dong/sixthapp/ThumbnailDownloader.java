package com.example.dong.sixthapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dong on 2015-07-13.
 */
public class ThumbnailDownloader<Token> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    Handler mHandler;
    //동기화되는(synchronizedMap)HashMap
    Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());
    Handler mResponseHandler;
    Listener<Token> mListener;

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

//    public ThumbnailDownloader() {
//        super(TAG);
//    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    /*
    Handler는 그것의 Looper에 의해 유지
    Handler가 익명의 내부클래스(Handler의 서브클래스)라면 암시적 객체참조를 통해 메모리누수(leak)가 생기기 쉽다.
    그러나 여기서는 모든 것이 HandlerThread에 결속되어 있으므로 leak위험은 없다.
    Lint주석인 @SuppressLint("HandlerLeak")를 사용해 leak경고를 하지말라고 해준다.
     */
    //Looper가 최초로 큐를 확인하기 전에 호출 -> Handler를 구현하기에 적합한 곳
    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    /*
                    Token은 제네릭 클래스 인자이지만 msg.obj는 Object이므로 Token으로 캐스팅할 수 없기 떄문에
                    컴파일러가 경고.    @SuppressWarnings("unchecked")로 경고를 없애준다.
                    **타입 소거자(type erasure)때문에 생김.자바 generic에 관해 알아보자
                     */
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for url: " + requestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    //Adapter의 getView()에서 호출
    public void queueThumbnail(Token token, String url) {
        Log.i(TAG, "Got an URL: " + url);
        requestMap.put(token, url);

        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    //FlickrFetchr().getUrlBytes(url);로 부터 다운받은 바이트데이터를 비트맵으로 변환
    private void handleRequest(final Token token) {
        try {
            final String url = requestMap.get(token);
            if (url == null) return;

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            //커스텀 메시지를 main쓰레드에 역으로 전달
            /*
            또다른 편리한 Handler메소드인 post(Runnable)를 사용
            mResponseHandler가 main쓰레드의 Looper와 연관되기 때문에 이UI변경 코드는 main쓰레드에서 실행될 것
             */
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    /*
                    requestMap확인 -> GridView가 자신의 뷰들을 재생하기 때문에 필요
                    ThumbnailDownloader가 Bitmap의 다운로드를 마칠 동안 GridView는 ImageView를 재생하고
                    다른 URL을 요청할 수 있을 것이다. 따라서 requestMap을 다시 확인함으로써 각 Token이 올바른
                    이미지를 얻게 해준다. 그사이 다른 요청이 만들어졌어도 마찬가지다.
                    */
                    if (requestMap.get(token) != url)
                        return;

                    //requestMap에서 Token을 삭제하고 비트맵을 Token에 설정
                    requestMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    //큐에서 모든 요청(메시지) 지우기
    public void clearQueue(){
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        requestMap.clear();
    }
}
