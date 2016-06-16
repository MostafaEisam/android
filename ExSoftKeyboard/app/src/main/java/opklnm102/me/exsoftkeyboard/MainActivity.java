package opklnm102.me.exsoftkeyboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * SoftKeyboard는 안드로이드 컴포넌트 중 서비스에 해당
 * 배경에서 항상 대기하고 있다가 문자입력이 필요할 때 입력기를 보여주는 역할
 * 언제나 실행 중이라는 면에서 데몬
 * 서비스 자체는 눈에 보이지 않으며 입력이 필요할 때 키보드 뷰를 열어주는 역할
 *
 * SoftKeyboard의 기능을 제공하는 InputMethodService는 Service를 상속
 * OS는 문자입력이 필요할 때 이 클래스의 콜백메소드를 호출
 *
 * void onIninializeInterface()
 * UI생성 전에 가장 먼저 호출. 초기화 작업
 * 서비스가 처음 생성, 설정이 변경될 때 호출
 * 사용할 키보드를 이 단계에서 미리 생성
 *
 * void onBindInput()
 * 새로운 에디트와 연결될 때 호출
 * 에디트 사이를 이동하여 입력 포커스가 바뀔 때마다 호출
 *
 * void onStartInput(EditorInfo info, boolean restarting)
 * 입력을 시작할 때 호출. 입력상태를 초기화
 * info - 입력받을 정보의 종류등의 정보를 제공(문자, 숫자인지 판별하는 키의 캡션, 배치 등을 조정)
 * restarting - 같은 편집기에서 텍스트 변경등으로 인해 입력을 재시작하는 것인지 표시
 *
 * View onCreateInputView() - 입력창(화면 키보드, 키보드 배치를 생성하여 뷰를 리턴)
 * View onCreateCandidatesView() - 후보창(키보드 위쪽에 입력할 문자로 시작하는 후보 표시, 선택)
 * View onCreateExtractTextView() - 추출창(풀 화면 모드에서 입력받은 내용을 보여주는)
 * 3개 모두 필요없을시 생성X(음성입력), default는 null
 *
 * void onStartInputView(EditorInfo info, boolean restarting)
 * 입력창이 보이고 입력을 시작할 때 호출
 * onStartInput은 일반적인 설정
 * 여기서 뷰별로 필요한 초기화
 *
 * 입력창은 KeyboardView클래스(키보드를 보여주는 뷰, 껍데기)로 구현.
 * 키보드를 화면에 그리고 터치로부터 키입력을 받아들인다.
 *.내부에 표시되는 키보드는 Keyboard클래스(알맹이)로 구현
 * KeyboardView는 딱 1개만 있으면 되지만 Keyboard는 입력모드에 따라 여러개 필요
 *
 *
 *
 *
 * 아너림 - 한글오토마타 제작에 대한 기법이 총망라
 * http://www.soen.kr/project/anerim/
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
