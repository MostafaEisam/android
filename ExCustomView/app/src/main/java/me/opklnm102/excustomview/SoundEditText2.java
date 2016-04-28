package me.opklnm102.excustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016-04-28.
 */
public class SoundEditText2 extends View {


    SoundPool mSoundPool = null;
    int mClick1, mClick2;
    int mSound;
    float mVolume = 1.0f;
    float mSpeed = 1.0f;

    // new연산자로 생성할 때
    public SoundEditText2(Context context) {
        super(context);
        init(context, null);
    }

    // XML에서 전개할 때
    public SoundEditText2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SoundEditText2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //초기화 작업업
    void init(Context context, AttributeSet attrs) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build();

        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        mClick1 = mSoundPool.load(context, R.raw.chicks, 1);
        mClick2 = mSoundPool.load(context, R.raw.chicks, 1);
        mSound = mClick1;

        if(attrs != null){
            TypedArray ar = context.obtainStyledAttributes(attrs, R.styleable.SoundEditText2);  //정의한 attr을 읽어온다.
            mVolume = ar.getFloat(R.styleable.SoundEditText2_volume, 1.0f);
            mSpeed = ar.getFloat(R.styleable.SoundEditText2_speed, 1.0f);
            mSound = ar.getInt(R.styleable.SoundEditText2_sound, mClick1);
            ar.recycle();
        }

    }

    //EditText에 글자변경이 생기면 호출(처음 빈문자열로 초기화시에도 호출)
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        if (mSoundPool != null) {
            mSoundPool.play(mSound, mVolume, mVolume, 0, 0, mSpeed);
        }
    }
}
