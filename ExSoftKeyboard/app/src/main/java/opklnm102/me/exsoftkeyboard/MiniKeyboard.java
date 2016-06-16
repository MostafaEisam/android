package opklnm102.me.exsoftkeyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;

/**
 * 고급기능을 위해서는 일부 메소드 오버라이딩
 */
public class MiniKeyboard extends Keyboard {

    public MiniKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public MiniKeyboard(Context context, int xmlLayoutResId, int modeId, int width, int height) {
        super(context, xmlLayoutResId, modeId, width, height);
    }

    public MiniKeyboard(Context context, int xmlLayoutResId, int modeId) {
        super(context, xmlLayoutResId, modeId);
    }

    public MiniKeyboard(Context context, int layoutTemplateResId, CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
}
