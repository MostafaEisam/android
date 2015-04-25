package com.example.dong.doitmission_10;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;


public class MainActivity extends ActionBarActivity {
    public static int spacing = -45;

   // Book book[] = new Book[5];

    public int mPosition;

//    ImageView iV;
//    TextView nameTv,companyTv,dateTv,authorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        book[0] = new Book("미움받을 용기", "인플루엔셜", "2014.11.17", "기시미 이치로");
//        book[1] = new Book("지적대화를 위한 넓고 얕은 지식", "한빛비즈", "2014.12.05", "채사장");
//        book[2] = new Book("마법천자문 31","아울북","2015.03.23","올댓스토리");
//        book[3] = new Book("7번읽기 공부법","위즈덤하우스","2015.03.26","야마구치 마유");
//        book[4] = new Book("그림의 힘","에이트포인트","2015.03.02","김선현");

        iV = (ImageView)findViewById(R.id.imageView);
        nameTv = (TextView)findViewById(R.id.nametextView);
        companyTv = (TextView)findViewById(R.id.companytextView);
        dateTv = (TextView)findViewById(R.id.datetextView);
        authorTv = (TextView)findViewById(R.id.authortextView);

        CoverFlow coverFlow = (CoverFlow) findViewById(R.id.coverflow);
        ImageAdapter coverImageAdapter = new ImageAdapter(this);
        coverFlow.setAdapter(coverImageAdapter);

        coverFlow.setSpacing(spacing);
        coverFlow.setSelection(2, true);
        coverFlow.setAnimationDuration(2000);

        if(mPosition == coverFlow.getCenterOfCoverflow()){





        }
    }

    public void BookInformation(){

    }

    public class ImageAdapter extends BaseAdapter {
        int itemBackground;
        private Context mContext;
        private FileInputStream outstream;

        private Integer[] mBookIds = {R.drawable.book01, R.drawable.book02, R.drawable.book03, R.drawable.book04, R.drawable.book05};

        private ImageView[] mImages;

        public ImageAdapter(Context c) {
            mContext = c;
            mImages = new ImageView[mBookIds.length];
        }

        public int getCount() {
            return mBookIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView img = new ImageView(mContext);
            img.setImageResource(mBookIds[position]);
            mPosition = position;
            img.setLayoutParams(new CoverFlow.LayoutParams(400, 300));
            img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
            drawable.setAntiAlias(true);

            return img;
        }

        public float getScale(boolean focused, int offset) {
            return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
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
