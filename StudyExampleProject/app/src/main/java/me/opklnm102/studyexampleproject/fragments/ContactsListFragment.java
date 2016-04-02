package me.opklnm102.studyexampleproject.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.utils.DividerItemDecoration;
import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.views.adapters.ContactsListAdapter;

public class ContactsListFragment extends Fragment {

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

        rvContacts = (RecyclerView) view.findViewById(R.id.recyclerView_contacts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(linearLayoutManager);
        rvContacts.setHasFixedSize(true);
        rvContacts.scrollToPosition(0);

        mContactsListAdapter = new ContactsListAdapter(getActivity());
        mContactsListAdapter.setOnItemAddListener(new ContactsListAdapter.OnItemAddListener() {
            @Override
            public void onItemAdd(ContactItem contactItem) {
                mContactsListAdapter.addItem(contactItem, mContactsListAdapter.getItemCount() - 2);
                rvContacts.scrollToPosition(mContactsListAdapter.getItemCount() - 1);  //go to bottom, 안가진다....
            }
        });


        rvContacts.setAdapter(mContactsListAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        rvContacts.addItemDecoration(itemDecoration);


    }
}
