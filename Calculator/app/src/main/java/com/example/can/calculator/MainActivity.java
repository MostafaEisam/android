
package com.example.can.calculator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    String CalcResult = "";
    String result = "";
    TextView eTv, rTv;

    ArrayList<String> alPostfix = new ArrayList<>();  //후위식
    boolean zero = false;
    int bracketCount = 0;  //'('의 수
    boolean bracketCheck = false;  //'('가 있는지 판단

    //1은 초기상태, 2는 숫자입력, 3은 부호입력, 4는 .입력, 5는 (입력
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
            R.id.C,  // 초기화
            R.id.Backspace,  // <-
            R.id.CE,  // 부호전까지 지운다.
            R.id.Dot,  // .
            R.id.Bracket  // ()
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
        //초기상태
        if (status == 1) {
            switch (v.getId()) {
                case R.id.Zero:
                    zero = true;
                    status = 2;
                    CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 2;
                    CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 2;
                    CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 2;
                    CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 2;
                    CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 2;
                    CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 2;
                    CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 2;
                    CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 2;
                    CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 2;
                    CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // '+'
                case R.id.Minus:  // '-'
                case R.id.Mul:  // '*'
                case R.id.Divide:  // '/'
                case R.id.Asign:  // '='
                case R.id.Backspace:  // <-  지웠을 때 이전 상태로 돌아간다.
                case R.id.CE:  // 부호전까지 지운다.
                    status = 1;
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    zero = false;
                    alPostfix.clear();
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
                case R.id.Dot:  // 0.이 입력됨
                    status = 4;
                    CalcResult += "0.";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Bracket:  // '('가 입력된다.
                    status = 5;
                    if (bracketCount == 0)  // '('가 있는지 판단
                        bracketCheck = false;

                    if (bracketCheck) {  // '('있으면 ')'출력
                        bracketCount--;
                        CalcResult += ")";
                    } else {               // ')'없으면 '('출력
                        bracketCount++;
                        CalcResult += "(";
                    }
                    eTv.setText(CalcResult);
                    break;
            }
        } else if (status == 2) {  //숫자입력 했을 때
            switch (v.getId()) {
                case R.id.Zero:
                    status = 2;
                    if (zero) {
                        CalcResult = "0";
                    } else
                        CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 2;
                    if (zero) {
                        CalcResult = "1";
                        zero = false;
                    } else
                        CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 2;
                    if (zero) {
                        CalcResult = "2";
                        zero = false;
                    } else
                        CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 2;
                    if (zero) {
                        CalcResult = "3";
                        zero = false;
                    } else
                        CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 2;
                    if (zero) {
                        CalcResult = "4";
                        zero = false;
                    } else
                        CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 2;
                    if (zero) {
                        CalcResult = "5";
                        zero = false;
                    } else
                        CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 2;
                    if (zero) {
                        CalcResult = "6";
                        zero = false;
                    } else
                        CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 2;
                    if (zero) {
                        CalcResult = "7";
                        zero = false;
                    } else
                        CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 2;
                    if (zero) {
                        CalcResult = "8";
                        zero = false;
                    } else
                        CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 2;
                    if (zero) {
                        CalcResult = "9";
                        zero = false;
                    } else
                        CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // +
                    status = 3;
                    CalcResult = signCheck(CalcResult, "+");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Minus:  // -
                    status = 3;
                    CalcResult = signCheck(CalcResult, "-");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Mul:  // *
                    status = 3;
                    CalcResult = signCheck(CalcResult, "*");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Divide:  // /
                    status = 3;
                    CalcResult = signCheck(CalcResult, "/");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Asign:  // =
                    status = 1;
                    if(bracketCount > 0){
                        for(int i=0; i<bracketCount; i++)
                            CalcResult += ')';
                    }
                    result = operation(CalcResult);
                    if (result.equals("division by zero"))
                        Toast.makeText(this, "Error : " + result, Toast.LENGTH_LONG).show();
                    eTv.setText(CalcResult + "=");
                    rTv.setText(result);
                    zero = false;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    zero = false;
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
                case R.id.Backspace:
                    status = backSpace(CalcResult);
                    CalcResult = CalcResult.substring(0, CalcResult.length() - 1);
                    eTv.setText(CalcResult);
                    break;
                case R.id.CE:  // 부호전까지 지운다.
                    CalcResult = clearError(CalcResult);
                    if (CalcResult.length() < 1)
                        status = 1;
                    else
                        status = 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Dot:  // .이 입력된다.
                    status = 4;
                    zero = false;
                    CalcResult += ".";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Bracket:  //  *(가입력된다.
                    status = 5;
                    if (bracketCount == 0)  // '('가 있는지 판단
                        bracketCheck = false;

                    if (bracketCheck) {  // '('있으면 ')'출력
                        bracketCount--;
                        CalcResult += ")";
                    } else {               // ')'없으면 '('출력
                        bracketCount++;
                        CalcResult += "*(";
                    }
                    eTv.setText(CalcResult);
                    break;
            }
        } else if (status == 3) {  //부호를 눌렀을 때(중복 부호처리)
            switch (v.getId()) {
                case R.id.Zero:
                    status = 2;
                    zero = true;
                    CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 2;
                    CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 2;
                    CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 2;
                    CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 2;
                    CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 2;
                    CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 2;
                    CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 2;
                    CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 2;
                    CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 2;
                    CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // +
                    status = 3;
                    CalcResult = signCheck(CalcResult, "+");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Minus:  // -
                    status = 3;
                    CalcResult = signCheck(CalcResult, "-");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Mul:  // *
                    status = 3;
                    CalcResult = signCheck(CalcResult, "*");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Divide:  // /
                    status = 3;
                    CalcResult = signCheck(CalcResult, "/");
                    eTv.setText(CalcResult);
                    break;
                case R.id.Asign:  // =
                    status = 1;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    rTv.setText("");
                    Toast.makeText(this, "= Error : 수식이 바르지 않습니다.", Toast.LENGTH_LONG).show();
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
                case R.id.Backspace:  // <-
                    status = backSpace(CalcResult);
                    CalcResult = CalcResult.substring(0, CalcResult.length() - 1);
                    eTv.setText(CalcResult);
                    break;
                case R.id.CE:  // 부호전까지 지운다.
                    CalcResult = clearError(CalcResult);
                    if (CalcResult.length() < 0)
                        status = 1;
                    else
                        status = 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Dot:  // 0.입력된다.
                    status = 4;
                    CalcResult += "0.";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Bracket:  // '('만 입력된다.
                    status = 5;
                    bracketCount++;
                    CalcResult += "(";
                    eTv.setText(CalcResult);
                    break;
            }
        } else if (status == 4) {  // '.' 눌렀을 때
            switch (v.getId()) {
                case R.id.Zero:
                    status = 2;
                    CalcResult += 0;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 2;
                    CalcResult += 1;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 2;
                    CalcResult += 2;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 2;
                    CalcResult += 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 2;
                    CalcResult += 4;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 2;
                    CalcResult += 5;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 2;
                    CalcResult += 6;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 2;
                    CalcResult += 7;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 2;
                    CalcResult += 8;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 2;
                    CalcResult += 9;
                    eTv.setText(CalcResult);
                    break;
                //부호작동 안함
//                case R.id.Plus:  // '+'
//                case R.id.Minus:  // '-'
//                case R.id.Mul:  // '*'
//                case R.id.Divide:  / '/'
                case R.id.Asign:  // =
                    status = 1;
                    result = operation(CalcResult);
                    eTv.setText(CalcResult + "=");
                    if (result.equals("division by zero"))
                        Toast.makeText(this, "Error : " + result, Toast.LENGTH_LONG).show();
                    rTv.setText(result);
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
                case R.id.Backspace:  // <-
                    status = backSpace(CalcResult);
                    CalcResult = CalcResult.substring(0, CalcResult.length() - 1);
                    eTv.setText(CalcResult);
                    break;
                case R.id.CE:  // 부호전까지 지운다.
                    CalcResult = clearError(CalcResult);
                    if (CalcResult.length() < 0)
                        status = 1;
                    else
                        status = 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Dot:  // 입력불가.
                    status = 4;
                    break;
                case R.id.Bracket:  // ()
                    status = 5;
                    if (bracketCount == 0)  // '('가 있는지 판단
                        bracketCheck = false;

                    if (bracketCheck) {  // '('있으면 ')'출력
                        bracketCount--;
                        CalcResult += ")";
                    } else {               // ')'없으면 '('출력
                        bracketCount++;
                        CalcResult += "*(";
                    }
                    eTv.setText(CalcResult);
                    break;
            }
        } else if (status == 5) {  // '(' 눌렀을 때
            switch (v.getId()) {
                case R.id.Zero:
                    status = 2;
                    zero = true;
                    if (bracketCount == 0)
                        CalcResult += "*0";
                    else
                        CalcResult += 0;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.One:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*1";
                    else
                        CalcResult += 1;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Two:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*2";
                    else
                        CalcResult += 2;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Three:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*3";
                    else
                        CalcResult += 3;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Four:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*4";
                    else
                        CalcResult += 4;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Five:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*5";
                    else
                        CalcResult += 5;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Six:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*6";
                    else
                        CalcResult += 6;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Seven:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*7";
                    else
                        CalcResult += 7;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Eight:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*8";
                    else
                        CalcResult += 8;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Nine:
                    status = 2;
                    if (bracketCount == 0)
                        CalcResult += "*9";
                    else
                        CalcResult += 9;
                    bracketCheck = true;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Plus:  // '+'
                    if (bracketCount == 0) {
                        status = 3;
                        CalcResult = signCheck(CalcResult, "+");
                        eTv.setText(CalcResult);
                        break;
                    }
                case R.id.Minus:  // '-'
                    if (bracketCount == 0) {
                        status = 3;
                        CalcResult = signCheck(CalcResult, "-");
                        eTv.setText(CalcResult);
                        break;
                    }
                case R.id.Mul:  // '*'
                    if (bracketCount == 0) {
                        status = 3;
                        CalcResult = signCheck(CalcResult, "*");
                        eTv.setText(CalcResult);
                        break;
                    }
                case R.id.Divide:  // '/'
                    if (bracketCount == 0) {
                        status = 3;
                        CalcResult = signCheck(CalcResult, "/");
                        eTv.setText(CalcResult);
                        break;
                    }
                case R.id.Asign:  // '='
                    status = 1;
                    if (bracketCount == 0) {  //'('가 없거나 짝이 맞을 경우
                        result = operation(CalcResult);
                        if (result.equals("division by zero"))
                            Toast.makeText(this, "Error : " + result, Toast.LENGTH_LONG).show();
                        eTv.setText(CalcResult + "=");
                        rTv.setText(result);
                    } else {  // '('가 짝이 안맞을 경우
                        if (numberCheck(CalcResult)) {  //'('안에 수가 있을 때 '('를 닫고 계산
                            for (int i = 0; i < bracketCount; i++)
                                CalcResult += ')';
                            result = operation(CalcResult);
                            if (result.equals("division by zero"))
                                Toast.makeText(this, "Error : " + result, Toast.LENGTH_LONG).show();
                            eTv.setText(CalcResult + "=");
                            rTv.setText(result);
                        } else {  // 없다면 오류 처리
                            rTv.setText("");
                            Toast.makeText(this, "= Error : 수식이 바르지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                    zero = false;
                    bracketCheck = false;
                    bracketCount = 0;
                    CalcResult = "";
                    result = "";
                    alPostfix.clear();
                    break;
                case R.id.C:  // 초기화
                    status = 1;
                    CalcResult = "";
                    result = "";
                    bracketCheck = false;
                    bracketCount = 0;
                    alPostfix.clear();
                    eTv.setText("ExpressionText");
                    rTv.setText("ResultText");
                    break;
                case R.id.Backspace:  // <-
                    status = backSpace(CalcResult);
                    CalcResult = CalcResult.substring(0, CalcResult.length() - 1);
                    eTv.setText(CalcResult);
                    break;
                case R.id.CE:  // 부호전까지 값만 지운다.
                    CalcResult = clearError(CalcResult);
                    if (CalcResult.length() < 0)
                        status = 1;
                    else
                        status = 3;
                    eTv.setText(CalcResult);
                    break;
                case R.id.Dot:  //0.입력된다..
                    status = 4;
                    CalcResult += "0.";
                    eTv.setText(CalcResult);
                    break;
                case R.id.Bracket:  // ()
                    status = 5;
                    if (bracketCount == 0)  // '('가 있는지 판단
                        bracketCheck = false;
                    if (bracketCheck) {  // '('있으면 ')'출력
                        bracketCount--;
                        CalcResult += ")";
                    } else {
                        if (bracketCount > 0)
                            CalcResult += "(";  // 연속될 경우'('
                        else
                            CalcResult += "*(";  //처음일 경우 '*('
                        bracketCount++;
                    }
                    eTv.setText(CalcResult);
                    break;
            }
        }
    }

    //중복 부호 처리 메소드 -> 수정할것
    String signCheck(String expression, String signFlag) {
        boolean sign = false;
        int index = expression.length() - 1;
        String temp;

        //식의 마지막에 어떤 부호가 있는지 찾고
        if (expression.charAt(index) == '+' || expression.charAt(index) == '-' || expression.charAt(index) == '*' || expression.charAt(index) == '/')
            sign = true;

        //호출한 버튼의 부호로 바꾼다.
        if (sign)
            temp = expression.substring(0, index);
        else
            temp = expression.substring(0, index + 1);

        switch (signFlag) {
            case "+":
                temp += "+";
                break;
            case "-":
                temp += "-";
                break;
            case "*":
                temp += "*";
                break;
            case "/":
                temp += "/";
                break;
        }
        return temp;
    }

    int Prec(String op) { //연산자 우선순위
        switch (op) {
            case "(":
            case ")":
                return 0;
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
        }
        return -1;
    }

    //연산 메소드
    String operation(String expression) {
        /********************괄호, 연산자,숫자 분리 *****************************/
        ArrayList<String> al = new ArrayList<>();  //중위식
        String tmp;
        int start, end;

        for (start = 0, end = 0; end < expression.length(); end++) {
            switch (expression.charAt(end)) {
                case '+':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add("+");
                    break;
                case '-':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add("-");
                    break;
                case '*':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add("*");
                    break;
                case '/':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add("/");
                    break;
                case '(':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add("(");
                    break;
                case ')':
                    tmp = expression.substring(start, end);
                    start = end + 1;
                    al.add(tmp);
                    al.add(")");
                    break;
            }
        }
        tmp = expression.substring(start, end);
        al.add(tmp);
        /********************괄호, 연산자,숫자 분리 끝 *****************************/

        infixToPostfix(al);  //중위식->후위식 변환 호출

        /********************후위식 계산*****************************/
        Stack<String> s = new Stack<>();  //피연산자 스택
        String operation;
        String result;
        BigDecimal divByzero = new BigDecimal("0");

        for (int i = 0; i < alPostfix.size(); i++) {
            operation = alPostfix.get(i);
            if (operation.equals("+") || operation.equals("-") || operation.equals("*") || operation.equals("/")) {
                BigDecimal op2 = new BigDecimal(s.pop());
                BigDecimal op1 = new BigDecimal(s.pop());
                switch (operation) {
                    case "+":
                        op1 = op1.add(op2);
                        s.push(op1.toString());
                        break;
                    case "-":
                        op1 = op1.subtract(op2);
                        s.push(op1.toString());
                        break;
                    case "*":
                        op1 = op1.multiply(op2);
                        s.push(op1.toString());
                        break;
                    case "/":
                        if (op2.compareTo(divByzero) == 0)
                            return result = "division by zero";
                        op1 = op1.divide(op2);
                        s.push(op1.toString());
                        break;
                }
            } else {
                if (!operation.equals(""))
                    s.push(operation);
            }
        }
        result = s.pop();
        /********************후위식 계산 끝****************************/
        /********************마지막 .0제거 ****************************/
        int endIndex = result.length();

        if (endIndex > 1) {
            tmp = result.substring(endIndex - 2, endIndex);
            if (tmp.equals(".0"))  // 3.0일때 '.0'제거
                result = result.substring(0, endIndex - 2);
            else if (tmp.equals("0."))  // 0.일때 '.'제거
                result = result.substring(0, endIndex - 1);
            if (result.contains(".")) {  // x.xx0000일때 뒤 '0'제거
                int zeroCount = 0;
                for (int i = 1; i < endIndex; i++) {
                    if (result.charAt(endIndex - i) == '0')
                        zeroCount++;
                    else
                        break;
                }
                result = result.substring(0, endIndex - zeroCount);
            }
        }
        return result;
    }

    void infixToPostfix(ArrayList<String> al) {  //중위식->후위식 변환(스택이용)
        Stack<String> s = new Stack<>();  //연산자 스택
        String tmp;

        for (int i = 0; i < al.size(); i++) {  //중위식을 돌면서
            tmp = al.get(i);
            switch (tmp) {
                case "+":
                case "-":
                case "*":
                case "/":  //연산자면 우선순위 비교
                    while (!s.isEmpty() && Prec(tmp) <= Prec(s.peek())) {  //높으면 팝
                        alPostfix.add(s.pop());
                    }
                    s.push(tmp);  //낮으면 푸쉬
                    break;
                case "(":  //"("면 푸쉬
                    s.push(tmp);
                    break;
                case ")":  //")"면 "("만날때 까지 모두 pop
                    String bracket = "(";
                    while (!bracket.equals(s.peek()))
                        alPostfix.add(s.pop());
                    s.pop();  //스택에서 "("제거
                    break;
                default:  //피연산자면 식에 copy
                    alPostfix.add(tmp);
                    break;
            }
        }
        while (!s.isEmpty())  //식이 끝나고 스택에 남은 연산자 팝
            alPostfix.add(s.pop());
    }

    int backSpace(String expression) {  //backspace기능
        int index = expression.length();
        int status = 0;

        //괄호 지웠을때 처리
        if (expression.charAt(index - 1) == ')')
            bracketCount++;
        else if (expression.charAt(index - 1) == '(')
            bracketCount--;

        //전 상태를 알아낸다.
        if (index < 2)
            status = 1;
        else {
            switch (expression.charAt(index - 2)) {
                case '0':
                    zero = true;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    status = 2;
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                    status = 3;
                    break;
                case '.':
                    status = 4;
                    break;
                case '(':
                case ')':
                    status = 5;
                    break;
            }
        }
        return status;
    }

    boolean numberCheck(String expression) {  //계산식에 숫자가 존재하는지 검사
        for (int i = 0; i < expression.length(); i++) {
            if ('0' < expression.charAt(i) && expression.charAt(i) < '9')
                return true;  //숫자가 존재하기만하면 true
        }
        return false;  //하나도 없으면  false
    }

    String clearError(String expression) {  //CE기능
        String tmp;
        int idx;

        for (idx = expression.length() - 1; idx >= 0; idx--) {
            if (expression.charAt(idx) == '+' || expression.charAt(idx) == '-' ||
                    expression.charAt(idx) == '*' || expression.charAt(idx) == '/'
                    || expression.charAt(idx) == '(')
                break;
        }
        tmp = expression.substring(0, idx + 1);
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