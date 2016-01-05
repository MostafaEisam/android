package com.example.dong.sixthapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

/**
 * Created by Dong on 2015-07-14.
 */
/*
포그라운드 통지를 감추는 일반화된 프래그먼트
 */
public abstract class VisibleFragment extends Fragment {
    public static final String TAG = "VisibleFragment";

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(getActivity(), "Got a broadcast:" + intent.getAction(), Toast.LENGTH_SHORT).show();
            //결과 되돌려주기
            //여기를 실행하는 것은 수신이 되는 것이므로 통지를 취소시킨다.
            //더 복잡한 데이터를 받을 필요가 있다면 setResultData(String), setResultExtras(Bundle)을 사용
            //일단, 반환값들이 여기서 설정되면 이후의 모든 수신자들은 그값들을 받거나 변경할 수 있다.
            Log.i(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        //인텐트필터를 전달하기 위해 코드에서 하나 생성
        //XML로 지정할 수 있는 어떤 IntentFilter라도 코드로 나타낼 수 있다.
        //즉, 필터를 구성하는 addCategory(String), addAction(String), addDataPath(String)등을 호출
        IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        //getActivity().registerReceiver(mOnShowNotification, filter);
        //전송한 인텐트를 받으려면 같은 퍼미션을 사용해야한다.
        getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }
}
