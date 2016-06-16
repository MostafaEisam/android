package opklnm102.me.exsoftkeyboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;

/**
 * 고급기능을 위해서는 일부 메소드 오버라이딩
 */
public class MiniKeyboardView extends KeyboardView {

    public MiniKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MiniKeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public MiniKeyboardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
