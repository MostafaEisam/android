package me.dong.recyclerviewstudy.ui.boxes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.dong.recyclerviewstudy.common.OnItemClickListener;
import me.dong.recyclerviewstudy.R;
import me.dong.recyclerviewstudy.factory.BoxFactory;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE_SELECT_IMAGE = 1000;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_box)
    RecyclerView mRvBox;

    Unbinder mUnbinder;

    private BoxListAdapter mBoxListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        initBoxList();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    private void initBoxList() {
        mBoxListAdapter = new BoxListAdapter(MainActivity.this);
        mBoxListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });
        mRvBox.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRvBox.setHasFixedSize(true);
        mRvBox.setAdapter(mBoxListAdapter);
    }

    @OnClick({R.id.btn_add_text_box, R.id.btn_add_image_box})
    public void onClickAddBox(View view) {
        switch (view.getId()) {
            case R.id.btn_add_text_box:
                mBoxListAdapter.addBox(mBoxListAdapter.getItemCount(), BoxFactory.BoxType.TEXT);
//                BusProvider.getInstance().post(new BoxAddEvent());
                break;
            case R.id.btn_add_image_box:
                mBoxListAdapter.addBox(mBoxListAdapter.getItemCount(), BoxFactory.BoxType.IMAGE);
//                BusProvider.getInstance().post(new BoxAddEvent());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                //String name_Str = getImageNameToUri(data.getData());
//
//                //이미지 데이터를 비트맵으로 받아온다.
//                data.getData()
            }
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
