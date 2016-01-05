package com.example.dong.eighthapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.Date;

/**
 * Created by Dong on 2015-07-17.
 */
public class RunDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "runs.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_RUN = "run";
    private static final String COLUMN_RUN_ID = "_id";
    private static final String COLUMN_RUN_START_DATE = "start_date";

    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDER = "provider";
    private static final String COLUMN_LOCATION_RUN_ID = "run_id";

    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //"run" 테이블을 생성한다.
        db.execSQL("create table run (" +
                "_id integer primary key autoincrement, start_date integer)");

        //"location" 테이블을 생성한다.
        db.execSQL("create table location (" +
                " timestamp integer, latitude real, longitude real, altitude real," +
                " provider varchar(100), run_id integer references run(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //데이터베이스 스키마 변경 시 필요한 코드는 여기에 넣는다.
    }

    //run테이블에 새로운 데이터 하나를 추가하고 그것의 ID를 반환
    //ContentValues를 이용해 열의 이름과 값을 연관
    public long insertRun(Run run) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    public long insertLocation(long runId, Location location) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude());
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDER, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);
        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);
    }

    public RunCursor queryRuns() {
        //SQL의 "select * from run order by start_date asc"와 동일하다.
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN,
                null, null, null, null, null, COLUMN_RUN_START_DATE + " asc");
        return new RunCursor(wrapped);
    }

    /*
    하나의 이동 검색하기
    */
    public RunCursor queryRun(long id) {
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN,
                null,  //모든 열(SQL의 *)
                COLUMN_RUN_ID + " = ?", //특정 이동 ID를 찾는다.(SQL의 where).
                new String[]{String.valueOf(id)},  //검색 값
                null,  //SQL의 group by
                null,  //SQL의 order by
                null,  //SQL의 having
                "1");  //SQL의 limit 1 row
        return new RunCursor(wrapped);
    }

    /*
    특정 이동의 마지막 위치를 쿼리
    지정된 이동(Run)과 관련된 가장 최근 위치 데이터를 찾아서 LocationCursor에 반환
     */
    public LocationCursor queryLastLocationForRun(long runId) {
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null,  //모든 열(SQL의 *)
                COLUMN_LOCATION_RUN_ID + " = ?",  //주어진 이동 ID를 찾는다(SQL의 where).
                new String[]{String.valueOf(runId)},
                null,  //SQL의 group by
                null,  //SQL의 having
                COLUMN_LOCATION_TIMESTAMP + " desc",  //가장 최근순으로 정렬(SQL의 order by)
                "1");  //SQL의 limit 1
        return new LocationCursor(wrapped);
    }

    /*
    특정 이동의 위치들 쿼리하기
    쿼리결과 오름차순 정렬, 모든 위치 데이터를 반환
     RunMapFragment에게 위치 데이터를 주기 위해 RunManager에서 사용
     */
    public LocationCursor queryLocationsForRun(long runId) {
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null,
                COLUMN_LOCATION_RUN_ID + " = ?",  //주어진 이동 ID를 찾는다(SQL의 where)
                new String[]{String.valueOf(runId)},
                null,  //SQL의 group by
                null,  //SQL의 having
                COLUMN_LOCATION_TIMESTAMP + " asc");  //타임스탬프 순으로 정렬(SQL의 order by)
        return new LocationCursor(wrapped);
    }

    /*
    'run'테이블의 행들을 반환하는 커서를 편리하게 사용하도록 해주는 클래스
    getRun() 메소드에서는 현재의 행을 나타내는 Run 인스턴스를 반환한다.
    *run테이블에 있는  데이터들을 Run객체로 변환해주는 것
     */
    public static class RunCursor extends CursorWrapper {

        public RunCursor(Cursor cursor) {
            super(cursor);
        }

        /*
        현재의 행을 나타내는 Run객체를 반환하거나
        또는 현재의 행이 부적합하면 null을 반환
         */
        public Run getRun() {
            if (isBeforeFirst() || isAfterLast())
                return null;

            Run run = new Run();
            long runId = getLong(getColumnIndex(COLUMN_RUN_ID));
            run.setId(runId);
            long startDate = getLong(getColumnIndex(COLUMN_RUN_START_DATE));
            run.setStartDate(new Date(startDate));
            return run;
        }
    }

    /*
    RunCursor와 유사한 목적
    *location테이블에 있는  데이터들을 Location객체로 변환해주는 것
     */
    public static class LocationCursor extends CursorWrapper {

        public LocationCursor(Cursor c) {
            super(c);
        }

        public Location getLocation() {
            if (isBeforeFirst() || isAfterLast())
                return null;

            //Location 생성자에서 사용할 수 있게 위치 제공자를 먼저 얻는다.
            String provider = getString(getColumnIndex(COLUMN_LOCATION_PROVIDER));
            Location loc = new Location(provider);

            //나머지 위치 데이터 속성들을 Location객체에 설정한다.
            loc.setLongitude(getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE)));
            loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE)));
            loc.setAltitude(getDouble(getColumnIndex(COLUMN_LOCATION_ALTITUDE)));
            loc.setTime(getLong(getColumnIndex(COLUMN_LOCATION_TIMESTAMP)));
            return loc;
        }
    }
}
