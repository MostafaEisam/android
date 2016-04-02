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
import me.opklnm102.studyexampleproject.models.Contact;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<Object> mContactList;

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

    private OnItemAddListener mOnItemAddListener = new OnItemAddListener() {
        @Override
        public void onItemAdd(Contact contact) {
            mContactList.add(mContactList.size() - 1, contact);

        }
    };

//    public void setOnItemAddListener(OnItemAddListener listener){
//        mOnItemAddListener = listener;
//    }

    public interface  OnItemAddListener{
        void onItemAdd(Contact contact);
    }

    public void initData() {
        for (int i = 0; i < 20; i++) {
            Contact contact = new Contact(R.mipmap.ic_launcher, "김" + i, "010 " + i);
            addItem(contact, i);
        }

        addHeader();
        addFooter();
    }

    public void addHeader(){
        mContactList.add(0, "header");
        notifyItemInserted(0);
    }

    public void addFooter(){
        mContactList.add(mContactList.size(), "footer");
        notifyItemInserted(mContactList.size());
    }

    public ContactsListAdapter(Context context) {
        mContext = context;
        mContactList = new ArrayList<>();

        initData();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0 && mContactList.get(position) instanceof String && "header".equals(((String)mContactList.get(position)))) {
            return HEADER;
        } else if (position == mContactList.size() - 1 && mContactList.get(position) instanceof String && "footer".equals(((String)mContactList.get(position)))) {
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
                Contact contact = (Contact)mContactList.get(position);
                ((ContactsListItemViewholder)holder).bind(contact);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public void addItem(Contact contact, int position) {
        mContactList.add(position, contact);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mContactList.remove(position);
        notifyItemRemoved(position);
    }
}
