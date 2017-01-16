package me.dong.switchcompatcustom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class SwitchWithText extends RelativeLayout {

    final class TextPosition {
        public static final int TOP = 0x11;
        public static final int BOTTOM = 0x12;
        public static final int LEFT = 0x13;
        public static final int RIGHT = 0x14;
    }

    final class TextAlign {
        public static final int TOP = 0x11;
        public static final int BOTTOM = 0x12;
        public static final int LEFT = 0x13;
        public static final int RIGHT = 0x14;
        public static final int BASELINE = 0x15;
        public static final int CENTER = 0x16;
    }

    private static final int DEFAULT_TEXT_POSITION = TextPosition.RIGHT;
    private static final int DEFAULT_TEXT_ALIGN = TextAlign.CENTER;

    private String mTextOn;
    private String mTextOff;
    private int mTextPosition;
    private int mTextAlign;

    TextView mTvSwitchStatus;
    SwitchCompat mSwitchCompat;

    public SwitchWithText(Context context) {
        this(context, null, 0);
    }

    public SwitchWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = inflate(context, R.layout.switch_with_text, this);
        mTvSwitchStatus = (TextView) view.findViewById(R.id.tv_switch_status);
        mSwitchCompat = (SwitchCompat) view.findViewById(R.id.switchCompat);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SwitchWithText);
        mTextPosition = typedArray.getInt(R.styleable.SwitchWithText_textPosition, DEFAULT_TEXT_POSITION);
        mTextAlign = typedArray.getInt(R.styleable.SwitchWithText_textAlign, DEFAULT_TEXT_ALIGN);
        mTextOn = typedArray.getString(R.styleable.SwitchWithText_textOn);
        mTextOff = typedArray.getString(R.styleable.SwitchWithText_textOff);
        typedArray.recycle();

        RelativeLayout.LayoutParams params =
                (LayoutParams) mTvSwitchStatus.getLayoutParams();
        switch (mTextPosition) {
            case TextPosition.TOP:
                params.addRule(RelativeLayout.ABOVE, R.id.switchCompat);
                break;
            case TextPosition.BOTTOM:
                params.addRule(RelativeLayout.BELOW, R.id.switchCompat);
                break;
            case TextPosition.LEFT:
                params.addRule(RelativeLayout.LEFT_OF, R.id.switchCompat);
                break;
            case TextPosition.RIGHT:
                params.addRule(RelativeLayout.RIGHT_OF, R.id.switchCompat);
                break;
        }

        switch (mTextAlign) {
            case TextAlign.TOP:
                params.addRule(RelativeLayout.ALIGN_TOP, R.id.switchCompat);
                break;
            case TextAlign.BOTTOM:
                params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.switchCompat);
                break;
            case TextAlign.LEFT:
                params.addRule(RelativeLayout.ALIGN_LEFT, R.id.switchCompat);
                break;
            case TextAlign.RIGHT:
                params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.switchCompat);
                break;
            case TextAlign.BASELINE:
                params.addRule(RelativeLayout.ALIGN_BASELINE, R.id.switchCompat);
                break;
            case TextAlign.CENTER:
                params.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.switchCompat);
                break;
        }
        mTvSwitchStatus.setLayoutParams(params);

        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mTvSwitchStatus.setText(mTextOn);
                } else {
                    mTvSwitchStatus.setText(mTextOff);
                }
            }
        });

        if (mSwitchCompat.isChecked()) {
            mTvSwitchStatus.setText(mTextOn);
        } else {
            mTvSwitchStatus.setText(mTextOff);
        }
    }

    public void setTextPosition(int textPosition) {
        this.mTextPosition = textPosition;
    }

    public void setTextAlign(int textAlign) {
        this.mTextAlign = textAlign;
    }

    public void setTextOn(String text) {
        this.mTextOn = text;
    }

    public void setTextOff(String text) {
        this.mTextOff = text;
    }
}
