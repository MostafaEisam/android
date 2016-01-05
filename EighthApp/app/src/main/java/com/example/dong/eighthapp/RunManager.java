package com.example.dong.eighthapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LightingColorFilter;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Dong on 2015-07-15.
 */
/*
LocationManager와의 소통과 현재의 이동에 관한 더 자세한 것을 관리하기 위해서
만든 싱글톤 클래스
 */
public class RunManager {
    private static final String TAG = "RunManager";

    private static final String PREFS_FILE = "runs";
    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";

    public static final String ACTION_LOCATION = "com.example.dong.eighthapp.ACTION_LOCATION";

    private static final String TEST_PROVIDER = "TEST_PROVIDER";

    private static RunManager sRunManager;
    private Context mAppContext;
    private LocationManager mLocationManager;
    private RunDatabaseHelper mHelper;
    private SharedPreferences mPrefs;
    private long mCurrentRunId;

    //private 생성자를 사용함으로써 싱글톤 클래스로 만들 수 있다. 그리고
    //이 클래스의 인스턴스를 생성하려면 RunManager.get(Context)를 호출해야 한다.
    private RunManager(Context appContext) {
        mAppContext = appContext;
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);
        mHelper = new RunDatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public static RunManager getInstance(Context context) {
        if (sRunManager == null) {
            //애플리케이션 컨택스트를 사용한다.
            sRunManager = new RunManager(context.getApplicationContext());
        }
        return sRunManager;
    }

    /*
    위치 갱신 정보가 발생할 때 브로드캐스트되는 Intent를 생성
    App에서 이벤트를 식별하기 위해 커스텀 액션 이름을 사용
    shouldCreate인자를 사용해서 시스템에서 새로운 PendingIntent를 생성해야 하는지의 여부를
    PendingIntent.getBroadcast()에 (flags를 통해서) 알려준다.
     */
    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    /*
    GPS 제공자(provider)를 통해서 가능한 자주 위치 갱신 정보를 달라고 LocationManager에게 알려준다.
    requestLocationUpdates()에서 최소대기시간(1/000초 단위)과 위치를 갱신할 최소 거리(M)를 매개변수로 받는다.
    매개변수들은 우리가 납들할만한 최대 값으로 조정되어야 하며, 그러면서도 사용자들에게 좋은 사용자 경험을 제공해야 한다.
    사용자가 어디 있는지 대충 알아도 되면 큰값을 지정해도 된다.
     */
    public void startLocationUpdates() {
        String provider = LocationManager.GPS_PROVIDER;

        //만일 테스트 제공자가 있고 동작 가능하면 그것을 사용한다.
        if (mLocationManager.getProvider(TEST_PROVIDER) != null && mLocationManager.isProviderEnabled(TEST_PROVIDER)) {
            provider = TEST_PROVIDER;
        }
        Log.d(TAG, "Using provider " + provider);

        //만일 마지막 인식 위치가 있으면 그것을 알아내어 브로드캐스팅한다.
        Location lastKnown = mLocationManager.getLastKnownLocation(provider);
        if (lastKnown != null) {
            //시간을 현재로 재설정한다.
            lastKnown.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnown);
        }


        //LocationManager에게 위치 갱신 정보를 요청한다.
        PendingIntent pi = getLocationPendingIntent(true);
        mLocationManager.requestLocationUpdates(provider, 0, 0, pi);
    }

    private void broadcastLocation(Location location) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        mAppContext.sendBroadcast(broadcast);
    }

    public void stopLocationUpdates() {
        PendingIntent pi = getLocationPendingIntent(false);
        if (pi != null) {
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    /*
    getLocationPendingIntent(false) 호출 후 결과가 null인지 검사
    PendingIntent가 os에 등록되었는지 여부를 판단
     */
    public boolean isTrackingRun() {
        return getLocationPendingIntent(false) != null;
    }

    /*
    새로운 이동 객체를 생성
    insertRun()을 호출해 그것의 데이터를 DB에 추가
    이동의 기록을 시작하기 위해 startTrackingRun(run)을 호출
    시작 버튼에 대한 응답으로 RunFragment에서 사용
     */
    public Run startNewRun() {
        //이동 데이터를 DB에 추가한다.
        Run run = insertRun();

        //이동 데이터 기록을 시작한다.
        startTrackingRun(run);
        return run;
    }

    /*
    기존 이동의 기록을 다시 시작할 때 사용
    Run객체의 ID를 인스턴스 변수와 공유 프레퍼런스 모두에 저장
    App이 완전히 끝났더라도 나중에 공유 프레퍼런스에 저장된 것을 읽을 수 있다.
     */
    public void startTrackingRun(Run run) {
        //이동 ID를 보존한다.
        mCurrentRunId = run.getId();

        //공유 프레퍼런스에 그것을 저장한다.
        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();

        //위치 갱신 정보 요청을 시작한다.
        startLocationUpdates();
    }

    /*
    위치 갱신 정보 요청을 중단시키고 현재 이동의 ID를
    인스턴스 변수와 공유 프레퍼런스 모두에서 삭제
     */
    public void stopRun() {
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    private Run insertRun() {
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    /*
    queryRun(long)의 결과를 받아서 첫 번째 행의 이동 데이터를(만일 갖고 있다면)Run객체로 반환
     */
    public Run getRun(long id){
        Run run = null;
        RunDatabaseHelper.RunCursor cursor = mHelper.queryRun(id);
        cursor.moveToFirst();
        //행이 하나 있다면 그 데이터를 Run객체로 만든다.
        if(!cursor.isAfterLast())
            run = cursor.getRun();
        cursor.close();
        return run;
    }

    /*
    특정 Run객체를 인자로 받은 다음에 그것이 현재의 이동객체인지 확인
     */
    public boolean isTrackingRun(Run run){
        return run != null && run.getId() == mCurrentRunId;
    }

    /*
    SQL쿼리를 실행하는 일을 하는 것이 아니고 새로 생성되어 반환되는 RunCursor객체에게 커서를 제공
     */
    public RunDatabaseHelper.RunCursor queryRuns(){
        return mHelper.queryRuns();
    }

    //현재 추적(위치 갱신 정보를 요청)중인 이동의 위치 데이터를 DB에 추가
    public void insertLocation(Location loc){
        if(mCurrentRunId != -1){
            mHelper.insertLocation(mCurrentRunId, loc);
        }else{
            Log.e(TAG, "Location received with no tracking run; ignoring.");
        }
    }

    public Location getLastLocationForRun(long runId){
        Location location = null;
        RunDatabaseHelper.LocationCursor cursor = mHelper.queryLastLocationForRun(runId);
        cursor.moveToFirst();
        //주어진 Run객체의 마지막 위치 데이터를 쿼리하여 있으면
        //그 데이터를 Location객체로 변환하고 반환한다.
        if(!cursor.isAfterLast())
            location = cursor.getLocation();
        cursor.close();
        return location;
    }

    /*
    특정 이동의 위치들 쿼리하기
    RunMapFragment는 이메소드를 사용해 위치 데이터들을 로드할 수 있다.
     */
    public RunDatabaseHelper.LocationCursor queryLocationsForRun(long runId){
        return mHelper.queryLocationsForRun(runId);
    }
}
