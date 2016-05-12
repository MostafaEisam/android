package me.opklnm102.exhelloreactive.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.opklnm102.exhelloreactive.App;
import me.opklnm102.exhelloreactive.AppInfoList;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.adapter.AppInfoListAdapter;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Administrator on 2016-05-12.
 */
public class NetworkTaskFragment extends Fragment {

    @BindView(R.id.arc_progress)
    ArcProgress mArcProgress;

    @BindView(R.id.button_download)
    Button btnDownload;

    Unbinder mUnbinder;

    //프로그레스바 갱신을 관리하는데 사용
    private PublishSubject<Integer> mPublishSubject = PublishSubject.create();

    public NetworkTaskFragment() {
    }

    public static NetworkTaskFragment newInstance() {
        NetworkTaskFragment fragment = new NetworkTaskFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    //Button을 이용해 다운로드 시작
    @OnClick(R.id.button_download)
    void download() {
        btnDownload.setText("Downloading");
        btnDownload.setClickable(false);

        mPublishSubject
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        App.L.debug("Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        App.L.error(e.toString());
                    }

                    @Override
                    public void onNext(Integer integer) {
                        //다운로드 동안 Progress Bar 갱신
                        mArcProgress.setProgress(integer);
                    }
                });

        String destination = "/sdcard/oftboy.avi";

        //다운로드의 진행 상태를 관찰하고 ProgressBar를 갱신하기 위해 새로운 구독 생성
        observableDownload("http://archive.blender.org/fileadmin/movies/softboy.avi", destination)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    //onNext()
                    //다운로드가 완료된 후 비디오 플레이어를 시작
                    resetDownloadButton();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(destination);
                    intent.setDataAndType(Uri.fromFile(file), "video/avi");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }, error ->{  //onError()
                    Toast.makeText(getActivity(), "Something went south", Toast.LENGTH_SHORT).show();
                    resetDownloadButton();
                });
    }

    private void resetDownloadButton() {
        btnDownload.setText("Download");
        btnDownload.setClickable(true);
        mArcProgress.setProgress(0);
    }

    private Observable<Boolean> observableDownload(String source, String destination) {
        return Observable.create(subscriber -> {
            try {
                boolean result = downloadFile(source, destination);
                if(result){
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                }else {
                    subscriber.onError(new Throwable("Download failed"));
                }
            }catch (Exception e){
                subscriber.onError(e);
            }
        });
    }

    private boolean downloadFile(String source, String destination) {
        boolean result = false;
        InputStream is = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(source);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return false;
            }

            int fileLength = connection.getContentLength();

            is = connection.getInputStream();
            os = new FileOutputStream(destination);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = is.read(data)) != -1) {
                total += count;

                if (fileLength > 0) {
                    int percentage = (int) (total * 100 / fileLength);
                    mPublishSubject.onNext(percentage);
                }
                os.write(data, 0, count);
            }
            mPublishSubject.onCompleted();
            result = true;
        } catch (Exception e) {
            mPublishSubject.onError(e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                mPublishSubject.onError(e);
            }

            if(connection != null){
                connection.disconnect();
                mPublishSubject.onCompleted();
            }
        }
        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
