package com.example.dong.secondapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dong on 2015-05-27.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.dong.second.date";

    private Date date;

    //DatePickerFragment에게 범죄 발생일자를 전달하기 위해 newInstance(Date) 메소드를 작성하여
    //Date를DatePickerFragment의 프래그먼트 인자로 전달
    //CrimeFragment가 모델계층(Crime 객체)과 자신의 뷰를 변경할 수 있도록
    //CrimeFragment에게 변경된 범죄 발생일자를  Intent에 담아
    //CrimeFragment.onActivityResult()를 호출할 것이다.
    //프래그먼트 인자의 생성과 설정은 프래그먼트 생성자를 대체하는 newInstance()에서 처리
    public static DatePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    //목표 프래그먼트로 데이터 전달
    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        //getTargetRequestCode() -> 어떤 프래그먼트가 결과를 반환하는지 목표 프래그먼트에 알려주기 위한 코드,
        //resultCode -> 조치할 액션을 결정하는 결과 코드
        //intent -> 엑스트라 데이터를 가질 수 있는 Intent객체
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    //DialogFragment를 화면에 보여주기 위해 호스팅 액티비티의 FragmentManager가 이 메소드를 호출
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Date객체의 정보를 사용해서 DatePicker를 초기화하려면 월, 일, 년의 정수 값 필요
        //Date는 타임스탬프 형태이므로 정수값이 아닌 다른 형태로 갖고 있다.
        date = (Date) getArguments().getSerializable(EXTRA_DATE);

        //년, 월, 일을 얻기 위해 Calendar객체를 생성
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        int amPM = calendar.get(Calendar.AM_PM);
        final int second = calendar.get(Calendar.SECOND);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_date, null);

        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //년, 월, 일을 Date객체로 변환
                date = new GregorianCalendar(year, monthOfYear, dayOfMonth, hour, minute, second).getTime();

                //선택된 날짜 값을 프래그먼트 인자에 변경하고 보존
                //장치 회전에 대비하여 Crime객체의 date값을 보존하기 위해
                //장치가 회전하면 FragmentManager가 현재의 DatePickerFragment인스턴스를 소멸시키고
                //새로운 인스턴스를 생성 -> FragmentManager는 그 인스턴스의 onCreateDialog()를 호출
                //이떄 저장된 date를 프래그먼트 인자로 부터 얻을수 있다.
                //onSaveInstanceState()에서 프래그먼트의 상태정보를 저장하는 것보다 이 방법이 더 간단
                getArguments().putSerializable(EXTRA_DATE, date);
            }
        });

        //AlertDialog.Builder -> AlertDialog인스턴스를 생성하는 데 필요한 인터페이스 제공
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }
}
