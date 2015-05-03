package com.example.dong.doitmission_11;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    Animation animation;

    ContentResolver resolver;
    Cursor cursor;
    int count;
    int phoneCount;
    List<Integer> phoneIndexList = new ArrayList<>();
    List<Contact> contactList = new ArrayList<Contact>();  //전화번호부의 정보 저장
    int recordIndex = 0;

    LinearLayout personLaayout;
    ImageView personImage;  //연락처 이미지
    TextView personName;  //연락처 이름
    TextView personMobile;  //연락처 번호
    TextView personCount;  //연락처 수

    public static boolean running = false;
    public static final int INTER_ANIMATION = 5000;  //sleep

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personLaayout = (LinearLayout) findViewById(R.id.personLayout);
        personImage = (ImageView) findViewById(R.id.personImage);
        personName = (TextView) findViewById(R.id.nameTv);
        personMobile = (TextView) findViewById(R.id.phonenumTv);

        personCount = (TextView) findViewById(R.id.personCount);

        //애니메이션 설정
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        ContactAnimationListener animationListener = new ContactAnimationListener();
        animation.setAnimationListener(animationListener);

        queryContacts();  //연락처 정보 얻기

        ContactAnimationThread thread = new ContactAnimationThread();
        thread.start();
    }

    class ContactAnimationThread extends Thread {
        public void run() {
            running = true;
            while (running) {
                try {
                    Thread.sleep(INTER_ANIMATION);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        personLaayout.startAnimation(animation);
                    }
                });
            }
        }
    }

    private class ContactAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            recordIndex++;
            if (recordIndex >= phoneIndexList.size())
                recordIndex = 0;

            int index = phoneIndexList.get(recordIndex);
            displayContact(index);

            personCount.setText((recordIndex + 1) + "/" + phoneCount);
        }
    }

    private void displayContact(int index) {
        if (cursor == null)
            queryContacts();

        cursor.moveToPosition(index);

        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        if (name != null)
            personName.setText(name);
        else
            personName.setText("");

        String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
        if (hasPhoneNumber.equalsIgnoreCase("1"))
            hasPhoneNumber = "true";
        else
            hasPhoneNumber = "false";

        String phoneNumber = null;

        if(Boolean.parseBoolean(hasPhoneNumber)){
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = " +id,null,null);
            while (phones.moveToNext()){
                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phones.close();
        }

        if(phoneNumber != null)
            personMobile.setText(phoneNumber);
        else
            personMobile.setText("");


        Bitmap bm = loadPhoto(cursor.getLong(0));
        if (bm != null)
            personImage.setImageBitmap(bm);
        else
            personImage.setImageDrawable(getResources().getDrawable(R.drawable.abc_ic_voice_search_api_mtrl_alpha));
    }

    //연락처 가져오기
    public void phoneBook() {
        Cursor cursor = getURI();                    // 전화번호부 가져오기
        phoneCount = cursor.getCount();                // 전화번호부의 갯수 저장
        Log.d("phoneCount", "phoneCount = " + phoneCount);

        if (cursor.moveToFirst()) {
            do {
                // 요소값 얻기
                String phoneNumber = cursor.getString(1).replaceAll("-", "");
                phoneNumber = phoneNumber.replaceAll(" ", "");

                if (phoneNumber.length() == 10) {
                    phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-"
                            + phoneNumber.substring(6);
                } else if (phoneNumber.length() > 8) {
                    phoneNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-"
                            + phoneNumber.substring(7);
                }

                Contact contact = new Contact();
                contact.setPhotoId(cursor.getLong(0));  //ID 얻기
                contact.setPhoneNum(phoneNumber);
                contact.setName(cursor.getString(2));  //이름 얻기

                Log.i("count", contact.getName() + " " + contact.getPhoneNum());

                contactList.add(contact);

            } while (cursor.moveToNext());
        }
    }

    //이미지 가저오기
    private Bitmap loadPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), contactUri);

        if (input != null) {
            return BitmapFactory.decodeStream(input);
        }
        return null;
    }

    private Cursor getURI() {
        //주소록 URI
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        //검색할 컬럼 정하기
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,  //연락처ID -> 사진정보 가져오는데 사용
                ContactsContract.CommonDataKinds.Phone.NUMBER,  //연락처
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME  //연락처 이름
        };

        //쿼리 날려서 커서 얻기
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        //커서 리턴
        return getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
    }

    private void queryContacts() {
        //주소록 URI
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        cursor = getContentResolver().query(uri, null, null, null, null);
        count = cursor.getCount();

        phoneCount = 0;
        phoneIndexList.clear();
        while (cursor.moveToNext()) {
            String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhoneNumber.equalsIgnoreCase("1")) {
                int curPosition = cursor.getPosition();
                phoneIndexList.add(curPosition);
                phoneCount++;
            }
        }

        int index = phoneIndexList.get(recordIndex);
        displayContact(index);

        personCount.setText("1" + "/" + phoneCount);
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
