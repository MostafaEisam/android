package com.example.dong.doitmission_04;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MenuActivity extends ActionBarActivity {
    Button btn1, btn2, btn3, btn4;
    public static final int REQUEST_CODE_SUBMENU = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn1 = (Button) findViewById(R.id.button2);  //고객관리
        btn2 = (Button) findViewById(R.id.button3);  //매출관리
        btn3 = (Button) findViewById(R.id.button4);  //상품관리
        btn4 = (Button) findViewById(R.id.button5);  //닫기

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getApplicationContext(), CustomerActivity.class);
                startActivityForResult(resultIntent, REQUEST_CODE_SUBMENU);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getApplicationContext(), SalesActivity.class);
                startActivityForResult(resultIntent, REQUEST_CODE_SUBMENU);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivityForResult(resultIntent, REQUEST_CODE_SUBMENU);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE_SUBMENU) {
            if (resultCode == RESULT_OK) {
                int menu = intent.getExtras().getInt("subMenu");

                switch (menu) {
                    case 1:
                        Toast.makeText(this, "고객관리", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(this, "매출관리", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(this, "상품관리", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
