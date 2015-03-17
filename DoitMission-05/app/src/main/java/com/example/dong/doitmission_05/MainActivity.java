package com.example.dong.doitmission_05;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    EditText eT1, eT2;
    Button btn1, btn2;

    DecimalFormat decimalFormat = new DecimalFormat("00");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eT1 = (EditText) findViewById(R.id.editText);  //이름
        eT2 = (EditText) findViewById(R.id.editText2);  //나이
        btn1 = (Button) findViewById(R.id.button);  //생년월일
        btn2 = (Button) findViewById(R.id.button2);  //저장

        btn1.setText(RightNow());  //현재 날짜 셋팅

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, listener, 2015, 3+1, 5);
                dialog.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"이름:" + eT1.getText().toString() + " 나이:" + eT2.getText().toString() + " 생년월일:" + btn1.getText().toString(),Toast.LENGTH_SHORT ).show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
           btn1.setText(year + "년 " + decimalFormat.format(monthOfYear+1) + "월 " + decimalFormat.format(dayOfMonth) + "일");
        }
    };

    public String RightNow() {
        Calendar rightNow = Calendar.getInstance();
        Date d = new Date(rightNow.getTimeInMillis());

        String result = simpleDateFormat.format(d);
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
