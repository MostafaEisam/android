package me.opklnm102.exhelloreactive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.opklnm102.exhelloreactive.adapter.SoAdapter;
import me.opklnm102.exhelloreactive.api.stackexchange.SeApiManager;

public class SoActivity extends AppCompatActivity implements SoAdapter.ViewHolder.OpenProfileListener {

    @BindView(R.id.toolbar)
    Toolbar mTtoolbar;

    @BindView(R.id.recyclerView_so)
    RecyclerView rvSo;

    @BindView(R.id.swipeRefreshLayout_so)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private SoAdapter mSoAdapter;

    Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so);
        mUnbinder = ButterKnife.bind(this);

        setSupportActionBar(mTtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSoAdapter = new SoAdapter(new ArrayList<>());
        mSoAdapter.setOpenProfileListener(this);

        rvSo.setLayoutManager(new LinearLayoutManager(this));
        rvSo.setAdapter(mSoAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this::refreshList);

        refreshList();
    }

    //사용자 리스트를 가져오고 보여준다.
    private void refreshList() {
        showRefresh(true);
        SeApiManager.getInstance().getMostPopularSOusers(10)
                .subscribe(users ->{
                    showRefresh(false);
                    mSoAdapter.updateUsers(users);
                },error->{
                    App.L.error(error.toString());
                    showRefresh(false);
                });
    }

    //ProgressBar와 RecyclerView를 보이거나 숨긴다.
    private void showRefresh(boolean show) {
        mSwipeRefreshLayout.setRefreshing(show);
        int visibility = show ? View.GONE : View.VISIBLE;
        rvSo.setVisibility(visibility);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void open(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
