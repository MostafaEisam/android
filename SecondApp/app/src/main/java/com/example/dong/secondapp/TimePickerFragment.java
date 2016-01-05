package com.example.dong.secondapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Dong on 2015-05-29.
 */
public class TimePickerFragment extends DialogFragment {

    private Date date;

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(DatePickerFragment.EXTRA_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(DatePickerFragment.EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        date = (Date) getArguments().getSerializable(DatePickerFragment.EXTRA_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);
        int amPM = calendar.get(Calendar.AM_PM);

        Log.d("TIME", "hour : " + hour + " minute : " + minute + " amPM : " + amPM);

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_time, null);

        TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_time_timePicker);
        //   timePicker.setCurrentHour(hour);
        // timePicker.setCurrentMinute(minute);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                date = new GregorianCalendar(year, month, day, hourOfDay, minute, second).getTime();

                getArguments().putSerializable(DatePickerFragment.EXTRA_DATE, date);
            }
        });

//        return new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//               sendResult(Activity.RESULT_OK);

//            }
//        },hour,minute, DateFormat.is24HourFormat(getActivity())
//        );

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("범죄 발생시간:")
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
