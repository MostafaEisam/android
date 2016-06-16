package opklnm102.me.exsoftkeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

public class MiniKeyboardService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    KeyboardView mKeyboardView;
    MiniKeyboard mEnglish;
    MiniKeyboard mNumber;
    MiniKeyboard mSymbol;

    private int mLastDisplayWidth;

    //키보드 생성, 폭이 바뀐 경우만 재생성
    @Override
    public void onInitializeInterface() {
        if (mEnglish != null) {
            int displayWidth = getMaxWidth();
            if (displayWidth == mLastDisplayWidth) return;
            mLastDisplayWidth = displayWidth;
        }
        mEnglish = new MiniKeyboard(this, R.xml.english);
        mNumber = new MiniKeyboard(this, R.xml.number);
        mSymbol = new MiniKeyboard(this, R.xml.symbol);
    }

    //입력뷰 생성하고 영문 키보드로 초기화
    @Override
    public View onCreateInputView() {
        mKeyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.minikeyboard, null);
        mKeyboardView.setOnKeyboardActionListener(this);
        mKeyboardView.setKeyboard(mEnglish);
        return mKeyboardView;
    }

    //입력 시작시 초기화 - 특별히 초기화할 내용이 없음
    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
    }

    //입력끝 - 키보드를 닫는다.
    @Override
    public void onFinishInput() {
        super.onFinishInput();
        if (mKeyboardView != null) {
            mKeyboardView.closing();
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        mKeyboardView.setKeyboard(mEnglish);
        mKeyboardView.closing();
    }

    //문자 입력을 받았을 때를 처리. 기능키 먼저 처리하고 문자키 처리
    //primaryCode - 눌러진 키의 코드
    //keyCodes - 눌러진 키의 주변키(오타 수정을 위해 사용)
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        if (primaryCode == Keyboard.KEYCODE_SHIFT) {
            //영문일 때는 대소문자 토클, 숫자나 기호일 때는 두 키보드 교체
            Keyboard current = mKeyboardView.getKeyboard();
            if (current == mEnglish) {
                mKeyboardView.setShifted(!mKeyboardView.isShifted());
            } else if (current == mNumber) {
                mKeyboardView.setKeyboard(mSymbol);
                mNumber.setShifted(true);
                mSymbol.setShifted(true);
            } else if (current == mSymbol) {
                mKeyboardView.setKeyboard(mNumber);
                mNumber.setShifted(false);
                mSymbol.setShifted(false);
            }
        } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
            //영문일 때 숫자로, 숫자나 기호일 때는 영문으로
            Keyboard current = mKeyboardView.getKeyboard();
            if (current == mEnglish) {
                mKeyboardView.setKeyboard(mNumber);
            } else {
                mKeyboardView.setKeyboard(mEnglish);
            }
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
            //Del키 코드를 보내 문자 삭제
            keyDownUp(KeyEvent.KEYCODE_DEL);
        } else {
            //쉬프트 상태이면 대문자로 변환
            if (isInputViewShown()) {
                if (mKeyboardView.isShifted()) {
                    primaryCode = Character.toUpperCase(primaryCode);
                }
            }

            //문자를 편집기로 보낸다.
            getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
        }
    }

    //키를 눌렀다가 떼는 동작을 하는 도우미 메소드
    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    //문자열이 입력되었다는 뜻, 이모티콘처럼 키하나로 여러개의 문자가 동시에 입력될 때 호출
    @Override
    public void onText(CharSequence text) {
    }

    //키보드 드래그시 호출
    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}
