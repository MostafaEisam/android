package com.example.user.imageloader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    String serchMethods = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList";
    String api_key = "&api_key=d4b167ab5f677c8efcc201ff7cb0b105";
    String date = "";
    String per_page = "&per_page=500";
    String extras = "&extras=url_m";
    String page = "";
    String format = "&format=json";

    String request = serchMethods + api_key + date + per_page + extras + page + format;

    TextView tV;
    String JSONdatal;
    final static int GET_JSON = 0;
    final static int DO_STH = 1;
    ProgressDialog progressDialog;

    Button getJSON;
    TextView showJSON;
    MessageHandler handler;

    ArrayList photoList = null;  //사진 URL을 저장할 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoList = new ArrayList<Photo>();

        handler = new MessageHandler();

        getJSON = (Button) findViewById(R.id.button);
        showJSON = (TextView) findViewById(R.id.textview);

        showJSON.setMovementMethod(new ScrollingMovementMethod());
        showJSON.setEllipsize(TextUtils.TruncateAt.END);

        getJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please wait.....");
                progressDialog.show();

                new Thread() {
                    @Override
                    public void run() {
                        JSONdatal = readJSON();
                        jsonParse(JSONdatal);
                        handler.sendEmptyMessage(GET_JSON);
                    }
                }.start();
            }
        });
    }

    //결과를 textView에 뿌리는 핸들러
    public class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;

             if(what == GET_JSON){
//                    if (tV == null)
//                        tV = (TextView) findViewById(R.id.textview);
//
//                    tV.setText(JSONdatal);

                    Intent intent = new Intent(getApplicationContext(),ImageLsitActivity.class);
                    intent.putExtra("list",photoList);

                    progressDialog.dismiss();
                    startActivity(intent);
            }
        }
    }

    //REST 요청 후,JSON 응답 텍스트를 String으로 반환
    public String readJSON() {
        StringBuilder JSONdata = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        byte[] buffer = new byte[1024];

        try {
            HttpGet httpGetRequest = new HttpGet(request);
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);

            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {  //서버가 요청한 페이지를 제공했다면
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();

                    try {
                        int bytesRead = 0;
                        BufferedInputStream bis = new BufferedInputStream(inputStream);

                        while ((bytesRead = bis.read(buffer)) != -1) {
                            String line = new String(buffer, 0, bytesRead);

                            JSONdata.append(line);
                        }
                    } catch (Exception e) {
                        Log.e("log", Log.getStackTraceString(e));
                    } finally {
                        try {
                            inputStream.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("log", Log.getStackTraceString(e));
        } finally {
            httpClient.getConnectionManager().shutdown();
            return JSONdata.toString();
        }
    }

    //JSON응답을 파싱
    public void jsonParse(String jsonString){
        if(jsonString == null) return;

        jsonString = jsonString.replace("jsonFlickrApi(","");
        jsonString = jsonString.replace(")","");

        try{
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONObject photos = jsonObject.getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");

            for(int i=0; i<photo.length(); i++){
                JSONObject photoInfo = photo.getJSONObject(i);
                String title = photoInfo.getString("title");
                String photoUrl = photoInfo.getString("url_m");

                //파싱확인
                //Log.e("title",title);
               //Log.e("imageUrl",photoUrl);

                Photo p = new Photo(title,photoUrl);

                photoList.add(p);
            }
        }catch(JSONException e){
            Log.e("log",Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
