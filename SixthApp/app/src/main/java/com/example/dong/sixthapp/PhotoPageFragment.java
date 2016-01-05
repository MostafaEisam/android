package com.example.dong.sixthapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

/**
 * Created by Dong on 2015-07-15.
 */
public class PhotoPageFragment extends VisibleFragment {
    private String mUrl;
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mUrl = getActivity().getIntent().getData().toString();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_page, container, false);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setMax(100);  //WebChromeClient에서 0~100의 범위로 진척도를 보여준다.
        final TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);

        mWebView = (WebView) view.findViewById(R.id.webView);

        //getSetting()로 WebSettings의 인스턴스를 얻은 후 자바스크립트 사용가능하게 설정
        //WebSettings는 WebView를 변경할 수 있는 3가지 방법중 1번째의 것
        mWebView.getSettings().setJavaScriptEnabled(true);

        //WebViewClient는 이벤트 인터페이스 -> 렌더링 이벤트에 응답할 수 있다.
        //ex)렌더러(renderer)가 특정 URL로부터 이미지를 로드하기 시작할 때 그것을 감지할 수 있다.
        //또는 HTTP POST요청을 서버에 다시 제출할지 여부를 결정할 수 있다.
        mWebView.setWebViewClient(new WebViewClient() {
            //새로운 URL이 WebView에 로드될 때 무엇을 할지 알려준다.
            //true -> '이 URL을 처리하지 마, 내가 처리할 테니까'
            //false -> 'WebView야 이 URL을 로드해, 난 안할거니까'
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        //브라우저 주변의 요소들을 변경시키는 이벤트에 반응
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                if (progress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(progress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                titleTextView.setText(title);
            }
        });

        //URL로딩은 WebView를 구성한 후에 처리되어야 하므로 제일 나중에 이루어져야 한다.
        mWebView.loadUrl(mUrl);

        return view;
    }
}
