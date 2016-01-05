package com.example.dong.secondapp;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Dong on 2015-05-26.
 */
//싱글톤 기법
public class CrimeLab {

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminallntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;  //s는 static의 접두사
    private Context mContext;

    private CrimeLab(Context context) {
        mContext = context;
        mSerializer = new CriminallntentJSONSerializer(mContext, FILENAME);
        // mCrimes = new ArrayList<>(); 대신 밑에 코드 생성
        //CrimeLab 싱글톤 인스턴스가 최초로 액세스될 때 범죄 데이터 로딩
        //만약 없으면 새로운 ArrayList생성
        try{

            mCrimes = mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG,"Error loading crimes : ",e);
        }
    }

    //애플리케이션 컨텍스트 -> 애플리케이션 전체에서 사용할 수 있는 컨텍스트
    public static CrimeLab get(Context context){
        if(sCrimeLab == null)
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        return sCrimeLab;
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c){
        mCrimes.remove(c);
    }

    //어디서 호출할까?
    //프래그먼트의 생명주기인 onPause()가 제일 확실한 선택
    public boolean saveCirmes(){
        try{
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,"crimes saved to file");
            return true;
        }catch (Exception e){
            Log.e(TAG,"Error saving crimes: ",e);
            return false;
        }
    }

    public ArrayList<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime c : mCrimes){
            if(c.getId().equals(id))
                return c;
        }
        return null;
    }
}
