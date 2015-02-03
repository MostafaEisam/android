
package com.example.can.calculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    String CalcResult = "";
    String result = "";
    TextView eTv, rTv;

    //boolean operator = false;
    //boolean cont = false;

    //1은 숫자입력,2는 부호입력, 3...?
    int status = 1;

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
        if (status == 1) {  //숫자입력 했을 때(초기상태)
            switch (v.getId()) {
                case R.id.Zero:
                    status = 1;
                    CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 1;
                    CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 1;
                    CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 1;
                    CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 1;
                    CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 1;
                    CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 1;
                    CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 1;
                    CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 1;
                    CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 1;
                    CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // +
                    status = 2;
                    CalcResult += "+";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Minus:  // -
                    status = 2;
                    CalcResult += "-";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Mul:  // *
                    status = 2;
                    CalcResult += "*";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Divide:  // /
                    status = 2;
                    CalcResult += "/";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Asign:  // =
                    status = 1;
                    result = operation(CalcResult);
                    eTv.setText(CalcResult + "=");
                    rTv.setText(result);
                    //operator = false;
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    //cont = false;
                    //operator = false;
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
            }
        } else if (status == 2) {  //부호를 눌렀을 때(중복 부호처리)
            switch (v.getId()) {
                case R.id.Zero:
                    status = 1;
                    CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 1;
                    CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 1;
                    CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 1;
                    CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 1;
                    CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 1;
                    CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 1;
                    CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 1;
                    CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 1;
                    CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 1;
                    CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // +
                    status = 2;
                    CalcResult = signCheck(CalcResult, "+");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Minus:  // -
                    status = 2;
                    CalcResult = signCheck(CalcResult, "-");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Mul:  // *
                    status = 2;
                    CalcResult = signCheck(CalcResult, "*");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Divide:  // /
                    status = 2;
                    CalcResult = signCheck(CalcResult, "/");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Asign:  // =
                    status = 1;
                    CalcResult = "";
                    result = "";
                    rTv.setText("=오류:수식이 바르지 않습니다.");  //토스트로 띄우자
                    //operator = false;
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    //cont = false;
                    //operator = false;
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
            }

        }

//        switch (v.getId()) {
//            case R.id.Zero:
//                CalcResult += 0;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.One:
//                CalcResult += 1;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Two:
//                CalcResult += 2;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Three:
//                CalcResult += 3;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Four:
//                CalcResult += 4;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Five:
//                CalcResult += 5;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Six:
//                CalcResult += 6;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Seven:
//                CalcResult += 7;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Eight:
//                CalcResult += 8;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Nine:
//                CalcResult += 9;
//                eTv.setText(CalcResult);
//                break;
//            case R.id.Plus:  // +
//                if (cont) {  //연속된 계산일 경우
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"+");
//                        eTv.setText(CalcResult);
//                        operator = false;
//                    }
//                    else{
//                        operator = true;
//                        result = operation(CalcResult);
//                        CalcResult = result;
//                        CalcResult += "+";
//                        eTv.setText(CalcResult);
//                        rTv.setText(result);
//                        //operator = false;
//                    }
//                } else {  //처음 부호일 경우
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"+");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        cont = true;
//                        CalcResult += "+";
//                        eTv.setText(CalcResult);
//                    }
//                }
//                break;
//            case R.id.Minus:  // -
//                if (cont) {  //연속된 계산일 경우
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"-");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        result = operation(CalcResult);
//                        CalcResult = result;
//                        CalcResult += "-";
//                        eTv.setText(CalcResult);
//                        rTv.setText(result);
//                        //operator = false;
//                    }
//                } else {  //처음 부호일 경우
//                    // operator = true;
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"-");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        cont = true;
//                        CalcResult += "-";
//                        eTv.setText(CalcResult);
//                    }
//                }
//                break;
//            case R.id.Mul:  // *
//                if (cont) {  //연속된 계산일 경우
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"*");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        result = operation(CalcResult);
//                        CalcResult = result;
//                        CalcResult += "*";
//                        eTv.setText(CalcResult);
//                        rTv.setText(result);
//                        //operator = false;
//                    }
//                } else {  //처음 부호일 경우
//                    // operator = true;
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"*");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        cont = true;
//                        CalcResult += "*";
//                        eTv.setText(CalcResult);
//                    }
//                }
//                break;
//            case R.id.Divide:  // /
//                if (cont) {  //연속된 계산일 경우
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"/");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        result = operation(CalcResult);
//                        CalcResult = result;
//                        CalcResult += "/";
//                        eTv.setText(CalcResult);
//                        rTv.setText(result);
//                        //operator = false;
//                    }
//                } else {  //처음 부호일 경우
//                    // operator = true;
//                    if(operator){  //이미 부호가 있을 경우(연속된 부호 처리)
//                        CalcResult = signCheck(CalcResult,"/");
//                        eTv.setText(CalcResult);
//                    }
//                    else{
//                        operator = true;
//                        cont = true;
//                        CalcResult += "/";
//                        eTv.setText(CalcResult);
//                    }
//                }
//                break;
//            case R.id.Asign:  // =
//                result = operation(CalcResult);
//                eTv.setText(CalcResult + "=");
//                rTv.setText(result);
//                operator = false;
//                break;
//            case R.id.C:  // 초기화
//                CalcResult = "";
//                result = "";
//                cont = false;
//                operator = false;
//                eTv.setText("ExpressionText");
//                rTv.setText("ResultText");
//                break;
//        }
    }

    //중복 부호 처리 메소드
    String signCheck(String expression, String signFlag) {
        char sign = ' ';
        String temp = "";

        //식에 어떤 부호가 있는지 찾고
        if (expression.contains("+"))
            sign = '+';
        else if (expression.contains("-"))
            sign = '-';
        else if (expression.contains("*"))
            sign = '*';
        else if (expression.contains("/"))
            sign = '/';

        //호출한 버튼의 부호로 바꾼다.
        switch (signFlag) {
            case "+":
                temp = expression.replace(sign, '+');
                break;
            case "-":
                temp = expression.replace(sign, '-');
                break;
            case "*":
                temp = expression.replace(sign, '*');
                break;
            case "/":
                temp = expression.replace(sign, '/');
                break;
        }
        return temp;
    }

    int Prec(String op){ //연산자 우선순위
        switch(op){
            case "(": case ")": return 0;
            case "+": case "-": return 1;
            case "*": case "/": return 2;
        }
        return -1;
    }

    //연산 메소드
    String operation(String expression) {
//        String tmp;
//        String sign = "";
//
//        if (expression.contains("+"))
//            sign = "+";
//        else if (expression.contains("-"))
//            sign = "-";
//        else if (expression.contains("*"))
//            sign = "*";
//        else if (expression.contains("/"))
//            sign = "/";
//
//        StringTokenizer st = new StringTokenizer(expression, sign);
//        int r = 0;
//
//        switch (sign) {
//            case "+":
//                r = Integer.parseInt(st.nextToken());
//                r += Integer.parseInt(st.nextToken());
//                break;
//            case "-":
//                r = Integer.parseInt(st.nextToken());
//                r -= Integer.parseInt(st.nextToken());
//                break;
//            case "*":
//                r = Integer.parseInt(st.nextToken());
//                r *= Integer.parseInt(st.nextToken());
//                break;
//            case "/":
//                r = Integer.parseInt(st.nextToken());
//                r /= Integer.parseInt(st.nextToken());
//                break;
//        }
//        tmp = Integer.toString(r);
//        return tmp;

        ArrayList<String> al = new ArrayList<>();
        String tmp = "";
        //괄호, 연산자,숫자 분리
        for (int start = 0, end = 0; end < expression.length(); end++) {
            switch (expression.charAt(end)) {
                case '+':
                    tmp = expression.substring(start, end);
                    start = end;
                    al.add(tmp);
                    al.add("+");
                    break;
                case '-':
                    tmp = expression.substring(start, end);
                    start = end;
                    al.add(tmp);
                    al.add("-");
                    break;
                case '*':
                    tmp = expression.substring(start, end);
                    start = end;
                    al.add(tmp);
                    al.add("*");
                    break;
                case '/':
                    tmp = expression.substring(start, end);
                    start = end;
                    al.add(tmp);
                    al.add("/");
                    break;
            }
        }

        infixToPostfix(al);
        return tmp;
    }

    void infixToPostfix(ArrayList<String> al){
        Stack<String> s = new Stack<>();
        ArrayList<String> alPostfix = new ArrayList<>();
        String tmp="";

        for(int i=0; i<al.size(); i++){
            tmp = al.get(i);
            switch(tmp){
                case "+":case "-":case "*":case "/":
                    while(!s.isEmpty() && Prec(tmp) <= Prec(s.peek())){
                        alPostfix.add(s.pop());
                    }
                    s.push(tmp); break;
                case "("
            }
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


