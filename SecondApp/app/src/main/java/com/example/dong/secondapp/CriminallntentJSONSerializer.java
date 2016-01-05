package com.example.dong.secondapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Dong on 2015-06-23.
 */
public class CriminallntentJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CriminallntentJSONSerializer(Context context, String filename) {
        mContext = context;
        mFilename = filename;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws JSONException,IOException{
        //JSON 객체가 저장되는 배열을 생성한다.
        JSONArray array = new JSONArray();
        for(Crime c : crimes){
            array.put(c.toJSON());
            //JSON 직렬화를 별도 클래스에 두는 장점
            //단위 테스트가 쉬움 -> 다른 코드에 의존하지 않기 때문
            //Context의 인스턴스와 파일 이름을 인자로 받는다. -> Context를 구현한 객체만 있으면 코드의 변경 없이 재사용

            //파일을 디스크에 쓴다.
            Writer writer = null;

            try {
                //자동으로 파일이름 앞에 샌드박스에 있는 경로를 붙인 후 그 파일을 생성하고 쓰기 위해 연다.
                //경로를 직접 주고 싶으면 Context.getFileDir()을 호출, 다른 퍼미션을 갖는 파일을 생성할 필요가 있다면
                //openFileOutput()이 필요
                OutputStream os = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(os);
                writer.write(array.toString());
            }finally {
                if (writer != null)
                    writer.close();
            }
        }
    }

    //파일시스템으로부터 범죄 데이터를 로딩하기
    public ArrayList<Crime> loadCrimes() throws JSONException, IOException {
        ArrayList<Crime> crimes = new ArrayList<>();
        BufferedReader reader = null;

        try {
            //파일을 열고 데이터를 읽어서 StringBuilder객체로 만든다.
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            //JSONTokener객체를 사용해서 JSON객체를 파싱한다.
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //JSONObject이 저장된 JSONArray로부터 Crime객체가 저장되는 ArrayList를 생성한다.
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException e){
            //이 예외는 무시한다. 이 앱의 최초 사용 시 데이터가 없어서 발생하지만 무시해도 된다.
        }finally {
            if(reader != null)
                reader.close();
        }
        return crimes;
    }
}
