package me.dong.exexpandablerecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRvBasic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRvBasic = (RecyclerView) findViewById(R.id.rv_basic);
        mRvBasic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        List<Item> itemList = new ArrayList<>();

        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_HEADER, "AAA"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "aaa"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "bbb"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ccc"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ddd"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_HEADER, "BBB"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "aaa"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "bbb"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ccc"));
        itemList.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ddd"));

        Item item = new Item(ExpandableListAdapter.VIEW_TYPE_HEADER, "CCC");
        item.invisibleChildren = new ArrayList<>();
        item.invisibleChildren.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "aaa"));
        item.invisibleChildren.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "bbb"));
        item.invisibleChildren.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ccc"));
        item.invisibleChildren.add(new Item(ExpandableListAdapter.VIEW_TYPE_CHILD, "ddd"));

        itemList.add(item);

        mRvBasic.setAdapter(new ExpandableListAdapter(itemList));
    }
}
