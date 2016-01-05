package com.example.dong.sixthapp;

import android.net.Uri;
import android.util.Log;

import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dong on 2015-07-11.
 */
public class FlickrFetchr {
    public static final String TAG = "FlickrFetchr";

    public static final String PREF_SEARCH_QUERY = "searchQuery";
    public static final String PREF_LAST_RESULT_ID = "lastResultId";

    private static final String ENDPOINT = "https://api.flickr.com/services/rest/";
    private static final String API_KEY = "d4b167ab5f677c8efcc201ff7cb0b105";
    private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH = "flickr.photos.search";
    private static final String PARAM_EXTRAS = "extras";
    private static final String PARAM_TEXT = "text";

    private static final String EXTRA_SMALL_URL = "url_s";

    private static final String XML_PHOTO = "photo";

    //URL로부터 바이트 데이터들을 다운
    byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);

        //URL을 참조하는 연결객체 생성
        //HttpURLConnection로 캐스팅함으로써 HTTP에 특화된 인터페이스들을 사용할 수 있다.
        //ex) 요청(request) 메소드, 응답(response) 코드, 스트리밍(streaming) 메소드 등
        //getInputStream() or getOutputStream()(POST호출)을 호출할 때 까지 연결하지 않는다.
        //그때까지는 적법한 응답 코드를 받을 수 없다.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            //일단 연결이 되면 연결의 데이터가 더이상 없을때까지 read()를 반복 호출
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    //    public void fetchItems()
    public ArrayList<GalleryItem> downloadGalleryItems(String url) {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String xmlString = getUrl(url);
            Log.i(TAG, "Received xml: " + xmlString);

            //XmlPullParser생성
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            parseItems(items, parser);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }catch (XmlPullParserException xppe){
            Log.e(TAG, "Failed to parse items", xppe);
        }
        return items;
    }

    public ArrayList<GalleryItem> fetchItems(){
        //Uri.Builder.appendQueryParameter(String, String)은 쿼리 문자열을 자동으로 올바르게 만든다.
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method", METHOD_GET_RECENT)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                .build().toString();

        Log.i(TAG, url);

        return downloadGalleryItems(url);
    }

    public ArrayList<GalleryItem> search(String query){
        String url = Uri.parse(ENDPOINT).buildUpon()
                .appendQueryParameter("method",METHOD_SEARCH)
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
                .appendQueryParameter(PARAM_TEXT, query)
                .build().toString();
        return downloadGalleryItems(url);
    }

    void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.next();

        while (eventType != XmlPullParser.END_DOCUMENT) {  //문서의 끝을 만나면 종료
            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
                String owner = parser.getAttributeValue(null, "owner");

                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                item.setOwner(owner);
                items.add(item);
            }
            eventType = parser.next();
        }
    }
}
