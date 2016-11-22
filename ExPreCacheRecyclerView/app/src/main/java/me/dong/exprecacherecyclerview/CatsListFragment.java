package me.dong.exprecacherecyclerview;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CatsListFragment extends Fragment {

    RecyclerView mRvCats;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRvCats = (RecyclerView) view.findViewById(R.id.rv_cats);

        // setup adapter
        List<Cat> catList = new ArrayList<>();
        String[] catsUrls = getResources().getStringArray(R.array.cat_pics);
        for (int i = 0; i < catsUrls.length; i++) {
            catList.add(new Cat(catsUrls[i], "Cat at position " + i));
        }
        CatsRecyclerAdapter adapter = new CatsRecyclerAdapter(getActivity(), catList);

        // setup layout manager
        PreCachingLayoutManager layoutManager = new PreCachingLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(getActivity()));

        mRvCats.setLayoutManager(layoutManager);
        mRvCats.setAdapter(adapter);
    }
}
