package me.opklnm102.studyexampleproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import me.opklnm102.studyexampleproject.activities.DetailInfoActivity;
import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.utils.DividerItemDecoration;
import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.views.adapters.ContactsListAdapter;

public class ContactsListFragment extends Fragment {

    public static final int REQUEST_CODE_CONTACTS_LIST_FRAGMENT = 1000;

    private static final String ARG_TITLE = "title";
    private static final String ARG_PAGE = "page";

    private String title;
    private int page;

    RecyclerView rvContacts;
    ContactsListAdapter mContactsListAdapter;

    public ContactsListFragment() {
        // Required empty public constructor
    }

    public static ContactsListFragment newInstance(String title, int page) {
        ContactsListFragment fragment = new ContactsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            page = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rvContacts.setLayoutManager(linearLayoutManager);
        // 위의 3줄을 1줄로 요약
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        //We can also enable optimizations if all item views are of the same height and width for significantly smoother scrolling
        rvContacts.setHasFixedSize(true);

        // Optionally customize the position you want to default scroll to
        rvContacts.scrollToPosition(0);

        mContactsListAdapter = new ContactsListAdapter(getActivity());
        mContactsListAdapter.setOnItemAddListener(new ContactsListAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(ContactItem contactItem) {
                mContactsListAdapter.addItem(contactItem, mContactsListAdapter.getItemCount() - 2);

                //go to bottom, 딜레이 없이는 안가진다....
//                rvContacts.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rvContacts.scrollToPosition(mContactsListAdapter.getItemCount() - 1);
//                    }
//                }, 500);

                //go to bottom, 이건 가진다.
                rvContacts.smoothScrollToPosition(mContactsListAdapter.getItemCount() - 1);
            }
        });

        mContactsListAdapter.setOnListItemClickListener(new ContactsListAdapter.OnListItemClickListener() {
            @Override
            public void onListItemClick(ContactItem contactItem) {
                Intent intent = new Intent(getActivity(), DetailInfoActivity.class);
                intent.putExtra("profileImg", contactItem.getProfileImg());
                intent.putExtra("name", contactItem.getName());
                intent.putExtra("phoneNumber", contactItem.getPhoneNumber());
                startActivityForResult(intent, REQUEST_CODE_CONTACTS_LIST_FRAGMENT);
            }
        });

        rvContacts.setAdapter(mContactsListAdapter);

        //RecyclerView set ItemDecoration
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvContacts.addItemDecoration(itemDecoration);


        //RecyclerView set animator
        rvContacts.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
    }

    private void initView(View view) {
        rvContacts = (RecyclerView) view.findViewById(R.id.recyclerView_contacts);
    }

    @Override
    public void onResume() {
        super.onResume();
        mContactsListAdapter.initData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), "취소", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_CODE_CONTACTS_LIST_FRAGMENT) {
            String name = data.getStringExtra("name");
            Toast.makeText(getActivity(), name + "에서 나옴", Toast.LENGTH_SHORT).show();
        }
    }
}
