package me.dong.exviewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-03-30.
 */
public class HelloFragment1 extends Fragment {

    private String title;
    private int page;

    public static HelloFragment1 newInstance(int page, String title){
        HelloFragment1 helloFragment1 = new HelloFragment1();
        Bundle args = new Bundle();
        args.putInt("someInt",page);
        args.putString("someTitle", title);
        helloFragment1.setArguments(args);
        return helloFragment1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hello1, container, false);
        TextView tvLabel = (TextView)view.findViewById(R.id.textView_label);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
