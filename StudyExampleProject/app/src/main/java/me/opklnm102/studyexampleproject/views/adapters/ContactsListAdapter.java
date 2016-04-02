package me.opklnm102.studyexampleproject.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.opklnm102.studyexampleproject.views.viewholders.ContactsFooterItemViewHolder;
import me.opklnm102.studyexampleproject.views.viewholders.ContactsHeaderItemViewHolder;
import me.opklnm102.studyexampleproject.views.viewholders.ContactsListItemViewholder;
import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.models.ContactItem;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<ContactItem> mContactList;

    public static final int HEADER = 1;
    public static final int FOOTER = 2;


//    private static OnItemClickListener listener;
//
//    public interface  OnItemClickListener{
//        void onItemClick(View itemView, int posiotion);
//    }
//
//    public void setOnItemClickListener(OnItemCLickListener listener){
//        this.listener = listener;
//    }

    private OnItemAddListener mOnItemAddListener;

    public void setOnItemAddListener(OnItemAddListener listener){
        mOnItemAddListener = listener;
    }

    public interface  OnItemAddListener{
        void onItemAdd(ContactItem contactItem);
    }

    public void initData() {
        for (int i = 0; i < 20; i++) {
            ContactItem contactItem = new ContactItem(R.mipmap.ic_launcher, "김" + i, "010 " + i);
            addItem(contactItem, i);
        }
    }

    public void addHeader(){
//        mContactList.add(0, "header");
        notifyItemInserted(0);
    }

    public void addFooter(){
//        mContactList.add(mContactList.size(), "footer");
        notifyItemInserted(mContactList.size());
    }

    public ContactsListAdapter(Context context) {
        mContext = context;
        mContactList = new ArrayList<>();

        initData();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return HEADER;
        } else if (position == mContactList.size() + 1) {
            return FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case HEADER:
                View headerView = LayoutInflater.from(mContext).inflate(R.layout.header_list_contacts, parent, false);
                viewHolder = new ContactsHeaderItemViewHolder(headerView, mOnItemAddListener);
                break;
            case FOOTER:
                View footerView = LayoutInflater.from(mContext).inflate(R.layout.footer_list_contacts, parent, false);
                viewHolder = new ContactsFooterItemViewHolder(footerView);
                break;
            default:
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_contacts, parent, false);
                viewHolder = new ContactsListItemViewholder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case HEADER:

                break;
            case FOOTER:
                ((ContactsFooterItemViewHolder)holder).bind("마지막");
                break;
            default:
                ContactItem contactItem = mContactList.get(position - 1);
                ((ContactsListItemViewholder)holder).bind(contactItem);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size() + 2;
    }

    public void addItem(ContactItem contactItem, int position) {
        mContactList.add(position, contactItem);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mContactList.remove(position);
        notifyItemRemoved(position);
    }
}
