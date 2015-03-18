package com.example.dong.doitmission_08;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Dong on 2015-03-16.
 */

//일정을 선택하는 커스텀 다이얼로그
public class PlanDialog extends Dialog {
    private Button okBtn, closeBtn;
    private EditText planEt, hourEt, minuteEt;
    private String plan, hour, minute;

    public PlanDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);  //커스텀 다이얼로그 레이아웃

        okBtn = (Button) findViewById(R.id.okBtn);
        closeBtn = (Button) findViewById(R.id.closeBtn);
        planEt = (EditText) findViewById(R.id.planeditText);
        hourEt = (EditText) findViewById(R.id.houreditText);
        minuteEt = (EditText) findViewById(R.id.minuteeditText);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plan = planEt.getText().toString();  //입력된 일정을 얻어옴
                hour = hourEt.getText().toString();  //입력된 시을 얻어옴
                minute = minuteEt.getText().toString();  //입력된 분을 얻어옴

                if (0 >= Integer.parseInt(hour) || Integer.parseInt(hour) >= 24)
                    Toast.makeText(getContext(), "hour error", Toast.LENGTH_SHORT).show();
                else if (0 > Integer.parseInt(minute) || Integer.parseInt(minute) >= 60)
                    Toast.makeText(getContext(), "minute error", Toast.LENGTH_SHORT).show();
                else
                    dismiss();  //이후 MainActivity에서 구현해준 Dissmiss 리스너가 작동함
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();  //이후 MainActivity에서 구현해준 Dissmiss와 cancel 리스너가 작동함
            }
        });
    }

    public void init() {
        minuteEt.setText(null);
        hourEt.setText(null);
        planEt.setText(null);
    }

    public String getPlan() {  //일정을 리턴
        return plan;
    }

    public String getHour() {  //시를 리턴
        return hour;
    }

    public String getMinute() {  //분을 리턴
        return minute;
    }
}