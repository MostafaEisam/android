package me.opklnm102.excustomview;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by opklnm102 on 2016-04-28.
 */
public class SoundEditText extends EditText {

    SoundPool mSoundPool = null;
    int mClick;

    // new연산자로 생성할 때
    public SoundEditText(Context context) {
        super(context);
        init(context);
    }

    // XML에서 전개할 때
    public SoundEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SoundEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //초기화 작업업
   void init(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(6).build();

        } else {
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        mClick = mSoundPool.load(context, R.raw.chicks, 1);
    }

    //EditText에 글자변경이 생기면 호출(처음 빈문자열로 초기화시에도 호출)
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        if (mSoundPool != null) {
            mSoundPool.play(mClick, 1, 1, 0, 0, 1);
        }
    }


}
