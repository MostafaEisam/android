package me.opklnm102.excustomview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/*
   XML문서는 aapt툴에 의해 이진형태로 컴파일되어 실행 파일에 포함되며 inflater가 이정보를 읽어 각 위젯을 생성
   inflater는 XML문서에 기록된 속성집합을 하나의 객체로 포장하여 위젯의 생성자로 전달하며 위젯 스스로 초기화하도록 지시
   속성목록이 생성자의 2번째 인수인 AttributeSet객체(초기화할 속성목록과 속성값이 컴파일되어 있다)
 */
public class AttrButton extends Button {

    String strAttr = "";

    public AttrButton(Context context) {
        super(context);
    }

    public AttrButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        // AttributeSet의 내용을 읽어온다.
        String name;
        String value;
        for(int i=0; i<attrs.getAttributeCount(); i++){
            name = attrs.getAttributeName(i);
            value = attrs.getAttributeValue(i);
            strAttr += (name + " = " + value + "\n");
        }
    }

    public AttrButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
