package com.example.dong.firstapp;

import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends AppCompatActivity {
    private static final String TAG = "CheatActivity";
    public static final String EXTRA_ANSWER_IS_TRUE = "tfquiz.ANSWER_IS_TRUE";
    public static final String EXTRA_ANSWER_SHOWN = "tfquiz.ANSWER_SHOWN";
    private static final String CHEATER_INDEX = "cheaterIdx";

    boolean answerIsTure;
    boolean isAnswerShown;

    TextView anserTv;
    TextView apiTv;
    Button showAnswer;

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState == null) {
            isAnswerShown = false;
            setAnswerShownResult();
        }
        else{
            isAnswerShown = savedInstanceState.getBoolean(CHEATER_INDEX);
            setAnswerShownResult();
        }

        answerIsTure = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        anserTv = (TextView) findViewById(R.id.answerTextView);
        apiTv = (TextView)findViewById(R.id.apiTextView);

        apiTv.setText("API 레벨 " + Build.VERSION.SDK_INT);

        showAnswer = (Button) findViewById(R.id.showAnswerButton);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answerIsTure) {
                    anserTv.setText(R.string.true_button);
                } else {
                    anserTv.setText(R.string.false_button);
                }
                isAnswerShown = true;
                setAnswerShownResult();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(CHEATER_INDEX,isAnswerShown);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
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
