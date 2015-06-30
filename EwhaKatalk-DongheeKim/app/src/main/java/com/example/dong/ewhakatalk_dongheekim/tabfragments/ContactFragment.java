package com.example.dong.ewhakatalk_dongheekim.tabfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dong.example.pinnedheaderlistview.PinnedHeaderListView;
import com.example.dong.ewhakatalk_dongheekim.ContactSectionedAdapter;
import com.example.dong.ewhakatalk_dongheekim.Profile;
import com.example.dong.ewhakatalk_dongheekim.R;

import java.util.ArrayList;


public class ContactFragment extends BaseFragment {

	ArrayList<Profile> profiles;
	boolean bookMark;

	PinnedHeaderListView listView;
    ContactSectionedAdapter sectionedAdapter;


	public static ContactFragment newInstance(String title, int activatedIcon, int defaultIcon) {

		ContactFragment newFragment = new ContactFragment();
		newFragment.setTitle(title);
		newFragment.setActivatedIcon(activatedIcon);
		newFragment.setDefaultIcon(defaultIcon);

		return newFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		profiles = new ArrayList<>();
		bookMark = false;

		for(int i=0; i<100; i++){
			Profile profile = new Profile("홍"+i,"www....","kaka");
			profiles.add(profile);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact, container, false);

        listView = (PinnedHeaderListView)view.findViewById(R.id.pinnedListView);

        RelativeLayout header = (RelativeLayout)LayoutInflater.from(getActivity()).inflate(R.layout.header_item,null);
        listView.addHeaderView(header);

        sectionedAdapter = new ContactSectionedAdapter(getActivity(),profiles,bookMark);
        listView.setAdapter(sectionedAdapter);

		return view;
	}


}
