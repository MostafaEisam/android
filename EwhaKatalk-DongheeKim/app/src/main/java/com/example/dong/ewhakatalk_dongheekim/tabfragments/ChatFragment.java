package com.example.dong.ewhakatalk_dongheekim.tabfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dong.ewhakatalk_dongheekim.R;


public class ChatFragment extends BaseFragment {

	public static ChatFragment newInstance(String title, int activatedIcon, int defaultIcon) {

		ChatFragment newFragment = new ChatFragment();
		newFragment.setTitle(title);
		newFragment.setActivatedIcon(activatedIcon);
		newFragment.setDefaultIcon(defaultIcon);

		return newFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);

		TextView tvNumber = (TextView) view.findViewById(R.id.tvNumber);
		tvNumber.setText(getTitle());

		return view;
	}


}
