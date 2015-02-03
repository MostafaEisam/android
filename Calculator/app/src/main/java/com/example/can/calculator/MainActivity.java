
package com.example.can.calculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.StringTokenizer;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    String CalcResult = "";
    String result = "";
    TextView eTv, rTv;

    boolean operator = false;
    boolean cont = false;

    //숫자 버튼 id
    static final int NumberBUTTONS[] = {
            R.id.Zero,
            R.id.One,
            R.id.Two,
            R.id.Three,
            R.id.Four,
            R.id.Five,
            R.id.Six,
            R.id.Seven,
            R.id.Eight,
            R.id.Nine
    };
    //부호 버튼 id
    static final int SignBUTTONS[] = {
            R.id.Plus,  // +
            R.id.Minus,  // -
            R.id.Mul,  // *
            R.id.Divide,  // /
            R.id.Asign,  // =
            R.id.C  // 초기화
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //숫자 버튼 등록
        for (int btnId : NumberBUTTONS) {
            Button btn = (Button) findViewById(btnId);
            btn.setOnClickListener(this);
        }
        //부호 버튼 등록
        for (int btnId : SignBUTTONS) {
            Button btn = (Button) findViewById(btnId);
            btn.setOnClickListener(this);
        }
        eTv = (TextView) findViewById(R.id.Expression);
        rTv = (TextView) findViewById(R.id.Result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Zero:
                CalcResult += 0;
                eTv.setText(CalcResult);
                break;
            case R.id.One:
                CalcResult += 1;
                eTv.setText(CalcResult);
                break;
            case R.id.Two:
                CalcResult += 2;
                eTv.setText(CalcResult);
                break;
            case R.id.Three:
                CalcResult += 3;
                eTv.setText(CalcResult);
                break;
            case R.id.Four:
                CalcResult += 4;
                eTv.setText(CalcResult);
                break;
            case R.id.Five:
                CalcResult += 5;
                eTv.setText(CalcResult);
                break;
            case R.id.Six:
                CalcResult += 6;
                eTv.setText(CalcResult);
                break;
            case R.id.Seven:
                CalcResult += 7;
                eTv.setText(CalcResult);
                break;
            case R.id.Eight:
                CalcResult += 8;
                eTv.setText(CalcResult);
                break;
            case R.id.Nine:
                CalcResult += 9;
                eTv.setText(CalcResult);
                break;
            case R.id.Plus:  // +
                if (cont) {  //연속된 계산일 경우
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"+");
                        eTv.setText(CalcResult);
                        operator = false;
                    }
                    else{
                        operator = true;
                        result = operation(CalcResult);
                        CalcResult = result;
                        CalcResult += "+";
                        eTv.setText(CalcResult);
                        rTv.setText(result);
                        //operator = false;
                    }
                } else {  //처음 부호일 경우
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"+");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        cont = true;
                        CalcResult += "+";
                        eTv.setText(CalcResult);
                    }
                }
                break;
            case R.id.Minus:  // -
                if (cont) {  //연속된 계산일 경우
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"-");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        result = operation(CalcResult);
                        CalcResult = result;
                        CalcResult += "-";
                        eTv.setText(CalcResult);
                        rTv.setText(result);
                        //operator = false;
                    }
                } else {  //처음 부호일 경우
                    // operator = true;
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"-");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        cont = true;
                        CalcResult += "-";
                        eTv.setText(CalcResult);
                    }
                }
                break;
            case R.id.Mul:  // *
                if (cont) {  //연속된 계산일 경우
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"*");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        result = operation(CalcResult);
                        CalcResult = result;
                        CalcResult += "*";
                        eTv.setText(CalcResult);
                        rTv.setText(result);
                        //operator = false;
                    }
                } else {  //처음 부호일 경우
                    // operator = true;
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"*");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        cont = true;
                        CalcResult += "*";
                        eTv.setText(CalcResult);
                    }
                }
                break;
            case R.id.Divide:  // /
                if (cont) {  //연속된 계산일 경우
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"/");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        result = operation(CalcResult);
                        CalcResult = result;
                        CalcResult += "/";
                        eTv.setText(CalcResult);
                        rTv.setText(result);
                        //operator = false;
                    }
                } else {  //처음 부호일 경우
                    // operator = true;
                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
                        CalcResult = signCheck(CalcResult,"/");
                        eTv.setText(CalcResult);
                    }
                    else{
                        operator = true;
                        cont = true;
                        CalcResult += "/";
                        eTv.setText(CalcResult);
                    }
                }
                break;
            case R.id.Asign:  // =
                result = operation(CalcResult);
                eTv.setText(CalcResult + "=");
                rTv.setText(result);
                operator = false;
                break;
            case R.id.C:  // 초기화
                CalcResult = "";
                result = "";
                cont = false;
                operator = false;
                eTv.setText("ExpressionText");
                rTv.setText("ResultText");
                break;
        }
    }

    //중복 부호 처리 메소드
    String signCheck(String expression,String signFlag){
        char sign=' ';
        String temp="";

        //식에 어떤 부호가 있는지 찾고
        if(expression.contains("+"))
            sign = '+';
        else if(expression.contains("-"))
            sign = '-';
        else if(expression.contains("*"))
            sign = '*';
        else if(expression.contains("/"))
            sign = '/';

        //호출한 버튼의 부호로 바꾼다.
        switch(signFlag){
            case "+":
                temp = expression.replace(sign,'+');
                break;
            case "-":
                temp = expression.replace(sign,'-');
                break;
            case "*":
                temp = expression.replace(sign,'*');
                break;
            case "/":
                temp = expression.replace(sign,'/');
                break;
        }
        return temp;
    }

    //연산 메소드
    String operation(String expression) {
        String tmp;
        String sign = "";

        if (expression.contains("+"))
            sign = "+";
        else if (expression.contains("-"))
            sign = "-";
        else if (expression.contains("*"))
            sign = "*";
        else if (expression.contains("/"))
            sign = "/";

        StringTokenizer st = new StringTokenizer(expression, sign);
        int r = 0;

        switch (sign) {
            case "+":
                r = Integer.parseInt(st.nextToken());
                r += Integer.parseInt(st.nextToken());
                break;
            case "-":
                r = Integer.parseInt(st.nextToken());
                r -= Integer.parseInt(st.nextToken());
                break;
            case "*":
                r = Integer.parseInt(st.nextToken());
                r *= Integer.parseInt(st.nextToken());
                break;
            case "/":
                r = Integer.parseInt(st.nextToken());
                r /= Integer.parseInt(st.nextToken());
                break;
        }
        tmp = Integer.toString(r);
        return tmp;
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


