package com.example.dong.firstapp;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String CHEATER_INDEX = "cheaterIdx";

    TextView questionTv;

    Button trueBtn;
    Button falseBtn;
    ImageButton nextBtn;
    ImageButton prevBtn;
    Button cheatBtn;

    TrueFalse[] questionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    boolean isCheater[] = new boolean[questionBank.length];

    int currentindex = 0;

    private void updateQuestion(){
        int question = questionBank[currentindex].getQuestion();
        questionTv.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean anserIsTrue = questionBank[currentindex].isTrueQuestion();

        int messageResId = 0;

        if(isCheater[currentindex]){
            if(userPressedTrue == anserIsTrue){
                messageResId = R.string.judgment_toast;
            }else{
                messageResId = R.string.incorrect_judgement_toast;
            }
        }else{
            if(userPressedTrue == anserIsTrue){  //그냥
                messageResId = R.string.correct_toast;
            }else{
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
        setContentView(R.layout.activity_main);

        for(int i=0; i<isCheater.length; i++){
            isCheater[i] = false;
        }

        questionTv = (TextView)findViewById(R.id.question_text_view);
        questionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentindex = (currentindex + 1)%questionBank.length;
                updateQuestion();
            }
        });

        trueBtn = (Button)findViewById(R.id.true_button);
        trueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        falseBtn = (Button)findViewById(R.id.false_button);
        falseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        prevBtn = (ImageButton)findViewById(R.id.prev_button);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentindex != 0) {
                    currentindex = (currentindex - 1) % questionBank.length;
                }

                updateQuestion();
            }
        });

        nextBtn = (ImageButton)findViewById(R.id.next_button);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentindex = (currentindex + 1)%questionBank.length;

                updateQuestion();
            }
        });

        cheatBtn = (Button)findViewById(R.id.cheat_button);
        cheatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"cheat button clicked");
                Intent i = new Intent(MainActivity.this,CheatActivity.class);
                Log.d(TAG,"intent created");
                boolean answerIsTrue = questionBank[currentindex].isTrueQuestion();
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE,answerIsTrue);
                startActivityForResult(i,0);
            }
        });

        if(savedInstanceState != null){
            currentindex = savedInstanceState.getInt(KEY_INDEX,0);
            isCheater[currentindex] = savedInstanceState.getBoolean(CHEATER_INDEX,false);
        }
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        isCheater[currentindex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX,currentindex);
        savedInstanceState.putBoolean(CHEATER_INDEX,isCheater[currentindex]);
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
