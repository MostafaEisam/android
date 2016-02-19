package me.dong.rxandroidex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        리엑티브 프로그래밍의 가장 핵심
        Observable - 데이터의 강을 만든다.
        Subscriber - 강에서 데이터를 하나씩 건진다.
         */
        Observable<String> simpleObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello RxAndroid!!");
                subscriber.onCompleted();
            }
        });

        simpleObservable
                .subscribe(new Subscriber<String>() {

                    //스트림의 종료
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "complete!");
                    }

                    //에러 신호를 전달
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "error: " + e.getMessage());
                    }

                    //새로운 데이터를 전달
                    @Override
                    public void onNext(String s) {
                        ((TextView) findViewById(R.id.textView)).setText(s);
                    }
                });

        /*
        subscriber를 구성할 때 항상 onCompleted, onNext, onError를 다루는 것은 불편
        RxJava는 평의를 위해 구성이 누락된 인터페이스 제공
         */

        //1. onNext()만 다룰 때
        simpleObservable
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {   //onNext()
                        ((TextView) findViewById(R.id.textView)).setText(s);
                    }
                });

        //2. onNext(), onError()를 다룰 때
        simpleObservable
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {  //onNext()
                        ((TextView) findViewById(R.id.textView)).setText(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {  //onError()

                    }
                });

        //3. onNext(), onError(), onCompleted() 다룰 때
        simpleObservable
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {   //onNext()
                        ((TextView) findViewById(R.id.textView)).setText(s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {  //onError()

                    }
                }, new Action0() {
                    @Override
                    public void call() {  //onCompleted()

                    }
                });

        /*
        - 데이터 가공 Map -
        한 데이터를 다른 데이터로 바꾸는 오퍼레이터
        원본의 데이터는 변경하지 않고 새로운 스트림을 만든다.
        ex) 스트림의 데이터에 *10하기
        simpleObservable
                .map(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer * 10;
                    }
                })
         */
        //map을 이용하여 문자열을 대문자로 바꾸기
        //Observable의 스트림을 대문자료 변환(새로운 스트림을 만드는) map 호출하고 Subscriber에 연결
        simpleObservable
                //Func1<인자의 자료형, 리턴의 자료형>
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return s.toUpperCase();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ((TextView) findViewById(R.id.textView)).setText(s);
                    }
                });

        //map 이용시 다른 타입으로도 바꿀 수 있다.(String -> Integer)
        simpleObservable
                .map(new Func1<String, Integer>() {
                    @Override
                    public Integer call(String s) {
                        return s.length();
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        ((TextView) findViewById(R.id.textView)).setText("length: " + integer);
                    }
                });

        //Observable을 쉽게 만들어주는 유틸리티
        //단일 데이터 - just
        //연속적인 데이터(배열, 컬렉션) - from
        Observable<String> stringObservable = Observable.just("Hello RxAndroid!");
        Observable<Integer> integerObservable = Observable.from(new ArrayList<Integer>());


        /*
        retrolambda 사용
         */
        stringObservable
                .map(text -> text.length())
                .subscribe(length -> ((TextView) findViewById(R.id.textView)).setText("length: " + length));

        /*
        UI는 복잡한 상호작용의 연속(한쪽을 움직이면 다른쪽이 바뀌고)
        Observable과 오퍼레이터등을 합성하여 UI를 효과적으로 구조화 할 수 있다.
         */
        //map에 전달받은 event를 무시하고 랜덤 값으로 바꾼다.
        RxView
                .clicks(findViewById(R.id.button))
                .map(event -> new Random().nextInt())
                .subscribe(value -> {
                    TextView tv = (TextView) findViewById(R.id.textView);
                    tv.setText("number: " + value.toString());
                }, throwable -> {
                    Log.e(TAG, "Error: " + throwable.getMessage());
                });

        /*
        옵저버블 병합
        여러 경로로 온 이벤트를 동시에 처리해야하는 경우
         */
        Observable<String> lefts = RxView.clicks(findViewById(R.id.button_left))
                .map(event -> "left");

        Observable<String> rights = RxView.clicks(findViewById(R.id.button_right))
                .map(event -> "right");

        Observable<String> together = Observable.merge(lefts, rights);

        //1. 바로 set
        together.subscribe(text -> ((TextView) findViewById(R.id.textView)).setText(text));

        //2. 가공하고 set
        together.map(text -> text.toUpperCase())
                .subscribe(text -> Toast.makeText(this, text, Toast.LENGTH_LONG).show());

        /*
        스캔 - 병합된 데이터를 누적적으로 처리할 수 있는 도구
         */
        Observable<Integer> minuses = RxView.clicks(findViewById(R.id.button_minus))
                .map(event -> -1);
        Observable<Integer> pluses = RxView.clicks(findViewById(R.id.button_plus))
                .map(event -> 1);
        Observable<Integer> togetherInteger = Observable.merge(minuses, pluses);
        //1씩 증가
        togetherInteger.scan(0, (sum, number) -> sum + 1)
                .subscribe(count -> ((TextView) findViewById(R.id.textView)).setText(count.toString()));
        //+-1씩 증가
        togetherInteger.scan(0, (sum, number) -> sum + number)
                .subscribe(number -> ((TextView) findViewById(R.id.textView)).setText(number.toString()));

        /*
        한번에 처리하는 컴바인
        여러 조건이 완성될 때 처리햐야할 이벤트들
        ex) 패스워드 재확인
        UI가 변경되었을 때 복합적으로 동작하는 UI를 쉽게 다룰 수 있다.
         */
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkbox1);
        EditText editText1 = (EditText) findViewById(R.id.editText1);

        //RxCompoundButton.checkedChanges() - 체크박스의 이벤트 처리, EditText의 활성화 여부 처리
        Observable<Boolean> checks1 = RxCompoundButton.checkedChanges(checkBox1);
        checks1.subscribe(check -> editText1.setEnabled(check));

        //editText를 Observable로 만든다.
        //StringUtils::isEmpty - 메소드 레퍼런스, StringUtils의 isEmpty()호출하라는 의미
        Observable<Boolean> textExists1 = RxTextView.textChanges(editText1)
                .map(StringUtils::isEmpty);

        //checks와 textExists 두가지 옵저버블 값을 가져와서 둘중 하나의 값이 변경될 때 마다
        //뒤의 람다함수((check, exist) -> !check || exist)가 호출되어
        //Observale<Boolean>에 데이터가 흘러가게 된다.
        //결국 이 코드는 두가지 경우에 대해 참이 되는 코드
        // 1. 체크박스가 꺼저있는 경우
        // 2. 체크박스가 켜져있으면서 글이 있는 경우
        //체크박스는 이 필드가 필수적인 필드인지 체크하는 용도
        Observable<Boolean> textValidations1 = Observable
                .combineLatest(checks1, textExists1, (check, exist) -> !check || exist);

        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkbox2);
        EditText editText2 = (EditText) findViewById(R.id.editText2);

        Observable<Boolean> checks2 = RxCompoundButton.checkedChanges(checkBox2);

        checks2.subscribe(check -> editText2.setEnabled(check));

        Observable<Boolean> textExists2 = RxTextView.textChanges(editText2)
                .map(StringUtils::isEmpty);

        Observable<Boolean> textValidations2 = Observable
                .combineLatest(checks2, textExists2, (check, exist) -> !check || exist);

        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkbox3);
        EditText editText3 = (EditText) findViewById(R.id.editText3);

        Observable<Boolean> checks3 = RxCompoundButton.checkedChanges(checkBox3);

        checks3.subscribe(check -> editText3.setEnabled(check));

        Observable<Boolean> textExists3 = RxTextView.textChanges(editText3)
                .map(StringUtils::isEmpty);

        Observable<Boolean> textValidations3 = Observable
                .combineLatest(checks3, textExists3, (check, exist) -> !check || exist);

        Button button = (Button) findViewById(R.id.button);

        //벨리데이션 값이 변경될 때마다 벨리데이션 값 셋 모두 올바른지 확인하여 버튼의 활성화 여부를 변경
        Observable.combineLatest(textValidations1, textValidations2, textValidations3,
                (validation1, validation2, validation3) ->
                        validation1 && validation2 && validation3).subscribe(validation -> button.setEnabled(validation));

        /*
        스케쥴러
        해당 Observable, Operator, Subscribe를 어떤 스레드에서 수행할지 결정
        스케쥴러가 어떤 부분을 맞게 된느지는 subscribeOn과 observeOn으로 지정
        독립적으로 사용 가능 - Observable, Operator, Subscribe 모델밖에서 별도로 사용 가능
         */
        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(new Action0() {
            @Override
            public void call() {
                //Todo: 실행할 메소드 호출
            }
        });

        /*
        Retrofit
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.naaaaaa.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        retrofit.create(BackendService.class);

        /*
        Realm
         */
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Person> persons = realm.where(Person.class).findAll();
        Person person = persons.first();

        Observable<Realm> realmObservable = realm.asObservable();
        Observable<RealmResults<Person>> realmResultsObservable = persons.asObservable();
        Observable<Person> personObservable = person.asObservable();

        //비동기
        realm.where(Person.class).equalTo("name", "John").findAllAsync().asObservable()
                .filter(new Func1<RealmResults<Person>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Person> persons) {
                        //Ignore unloaded results
                        return persons.isLoaded();
                    }
                })
                .subscribe(new Action1<RealmResults<Person>>() {
                    @Override
                    public void call(RealmResults<Person> persons) {
                        //Show persons...
                    }
                });
    }

    public interface BackendService {
        @GET("user")
        Call<JsonObject> getUser();

        @GET("user")
        Observable<JsonObject> getUserRx();

    }
}
