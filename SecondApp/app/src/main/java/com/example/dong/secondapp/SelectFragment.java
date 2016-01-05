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
import android.widget.Button;

import java.util.Date;

/**
 * Created by Dong on 2015-05-31.
 */
public class SelectFragment extends DialogFragment {

    private Date date;

    public static SelectFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(DatePickerFragment.EXTRA_DATE, date);

        SelectFragment fragment = new SelectFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(DatePickerFragment.EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_select, null);

        date = (Date) getArguments().getSerializable(DatePickerFragment.EXTRA_DATE);

        Button dateSelBtn = (Button) v.findViewById(R.id.dialog_select_date);
        dateSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();

                DatePickerFragment dialog = DatePickerFragment.newInstance(date);
                dialog.setTargetFragment(SelectFragment.this, CrimeFragment.REQUEST_DATE);
                dialog.show(fm, CrimeFragment.DIALOG_DATE);
            }
        });

        Button timeSelBtn = (Button) v.findViewById(R.id.dialog_select_time);
        timeSelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();

                TimePickerFragment dialog = TimePickerFragment.newInstance(date);
                dialog.setTargetFragment(SelectFragment.this, CrimeFragment.REQUEST_TIME);
                dialog.show(fm, CrimeFragment.DIALOG_TIME);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("변경 선택")
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == CrimeFragment.REQUEST_DATE || requestCode == CrimeFragment.REQUEST_TIME) {
            date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        }
    }
}
