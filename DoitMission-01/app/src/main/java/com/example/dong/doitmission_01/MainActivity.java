package com.example.dong.doitmission_01;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    Button btn1,btn2;
    ImageView iV1,iV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button)findViewById(R.id.upBtn);
        btn2 = (Button)findViewById(R.id.downBtn);
        iV1 = (ImageView)findViewById(R.id.imageView);
        iV2 = (ImageView)findViewById(R.id.imageView2);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.upBtn:
                iV1.setVisibility(View.VISIBLE);
                iV2.setVisibility(View.INVISIBLE);
                btn1.setEnabled(false);
                btn2.setEnabled(true);
                break;
            case R.id.downBtn:
                iV1.setVisibility(View.INVISIBLE);
                iV2.setVisibility(View.VISIBLE);
                btn1.setEnabled(true);
                btn2.setEnabled(false);
                break;
        }
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
