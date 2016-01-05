package com.example.dong.fifthapp;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Dong on 2015-07-10.
 */
public class RemoteControlFragment extends Fragment {
    private TextView mSelectedTextView;
    private TextView mWorkingTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remote_control, container, false);

        mSelectedTextView = (TextView) view.findViewById(R.id.fragment_remote_control_selectedTextView);
        mWorkingTextView = (TextView) view.findViewById(R.id.fragment_remote_control_workingTextView);

        View.OnClickListener numberButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tV = (TextView) view;
                String working = mWorkingTextView.getText().toString();
                String text = tV.getText().toString();

                if (working.equals("0"))
                    mWorkingTextView.setText(text);
                else
                    mWorkingTextView.setText(working + text);
            }
        };

//        Button zeroBtn = (Button)view.findViewById(R.id.fragment_remote_control_zeroButton);
//        zeroBtn.setOnClickListener(numberButtonListener);
//
//        Button oneBtn = (Button)view.findViewById(R.id.fragment_remote_control_oneButton);
//        oneBtn.setOnClickListener(numberButtonListener);

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.fragment_remote_control_tableLayout);
        int number = 1;

        //2개의 TextView를 건너뛰기 위해 2부터 시작
        for (int i = 2; i < tableLayout.getChildCount() - 1; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button btn = (Button) row.getChildAt(j);
                btn.setText("" + number);
                btn.setOnClickListener(numberButtonListener);
                number++;
            }
        }

        TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
        Button deleteBtn = (Button) bottomRow.getChildAt(0);
        deleteBtn.setText("지움");
        deleteBtn.setTextAppearance(getActivity(), R.style.ActionRemoteButton);  //동적으로 스타일 적용
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWorkingTextView.setText("0");
            }
        });

        Button zeroBtn = (Button) bottomRow.getChildAt(1);
        zeroBtn.setText("0");
        zeroBtn.setOnClickListener(numberButtonListener);

//        Button enterBtn = (Button)view.findViewById(R.id.fragment_remote_control_enterButton);
        Button enterBtn = (Button) bottomRow.getChildAt(2);
        enterBtn.setText("적용");
        enterBtn.setTextAppearance(getActivity(), R.style.ActionRemoteButton);
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence working = mWorkingTextView.getText();

                if (working.length() > 0)
                    mSelectedTextView.setText(working);
                mWorkingTextView.setText("0");
            }
        });

        return view;
    }


}
