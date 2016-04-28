package me.opklnm102.excustomview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by opklnm102 on 2016-04-28.
 */
/*
   EditText에 문자열을 입력하면 TextView에 현재까지 입력된 문자의 개수를 보여준다.
   항상같이 사용되므로 새로운 위젯으로 정의
 */
public class NumEditWidget extends LinearLayout implements TextWatcher {

    EditText mEditText;
    TextView mTextView;

    public NumEditWidget(Context context) {
        super(context);
        init();
    }

    public NumEditWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumEditWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //방법1 직접 전개
        setOrientation(VERTICAL);
        mEditText = new EditText(getContext());
        mTextView = new TextView(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addView(mEditText, params);
        addView(mTextView, params);

        //방법2 XML 전개. 편집하기 쉽다. 배포시 XML을 포함해야한다.
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.numeditwidget, this, true);
        mEditText = (EditText) findViewById(R.id.editText_limedit);
        mTextView = (TextView) findViewById(R.id.textView_limedit);

        mEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextView.setText("Now Length : " + s.length() + " Characters");
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
