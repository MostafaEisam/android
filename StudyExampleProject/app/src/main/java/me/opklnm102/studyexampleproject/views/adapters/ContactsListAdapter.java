package me.opklnm102.studyexampleproject.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.opklnm102.studyexampleproject.models.ImageItem;
import me.opklnm102.studyexampleproject.views.viewholders.ContactsFooterItemViewHolder;
import me.opklnm102.studyexampleproject.views.viewholders.ContactsHeaderItemViewHolder;
import me.opklnm102.studyexampleproject.views.viewholders.ContactsListItemViewholder;
import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.views.viewholders.ImageListItemViewHolder;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<Object> mContactList;

    public static final int HEADER = 1;
    public static final int FOOTER = 2;
    public static final int IMAGE = 3;

    //item add event
    private OnItemAddListener mOnItemAddListener;

    public void setOnItemAddListener(OnItemAddListener listener) {
        mOnItemAddListener = listener;
    }

    public interface OnItemAddListener {
        void onItemAdd(ContactItem contactItem);
    }

    //item click event
    private OnListItemClickListener mOnListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        mOnListItemClickListener = listener;
    }

    public interface OnListItemClickListener{
        void onListItemClick(ContactItem contactItem);
    }

    public void initData() {

        mContactList.clear();
        notifyDataSetChanged();

        for (int i = 0; i < 1; i++) {
            if (i != 0 && i % 5 == 0) {
                ImageItem imageItem = new ImageItem(R.mipmap.ic_launcher);
                addItem(imageItem, i);
            } else {
                ContactItem contactItem = new ContactItem(R.mipmap.ic_launcher, "김" + i, "010 " + i);
                addItem(contactItem, i);
            }
        }
    }

    public void addHeader() {
//        mContactList.add(0, "header");
        notifyItemInserted(0);
    }

    public void addFooter() {
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
        } else if (mContactList.get(position - 1) instanceof ImageItem) {
            return IMAGE;
        }
        return super.getItemViewType(position - 1);  //그냥 ListItem Type = 0
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
            case IMAGE:
                View itemViewImage = LayoutInflater.from(mContext).inflate(R.layout.item_list_image, parent, false);
                viewHolder = new ImageListItemViewHolder(itemViewImage);
                break;
            default:
                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list_contacts, parent, false);
                viewHolder = new ContactsListItemViewholder(itemView, mOnListItemClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case HEADER:  //data bind 할께 없어서 비워둠
                break;
            case FOOTER:  //data bind 할께 없어서 비워둬야 맞지만 억지로 넣어봄
                ((ContactsFooterItemViewHolder) holder).bind("마지막");
                break;
            case IMAGE:
                ImageItem imageItem = (ImageItem) mContactList.get(position - 1);
                ((ImageListItemViewHolder) holder).bind(imageItem);
                break;
            default:
                ContactItem contactItem = (ContactItem) mContactList.get(position - 1);
                ((ContactsListItemViewholder) holder).bind(contactItem);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mContactList.size() + 2;
    }

    //Todo: position 부분좀 숫자 값 통일할 필요가 있음
    public void addItem(Object object, int position) {
        mContactList.add(position, object);
        notifyItemInserted(position + 2);
    }

    //Todo: remove posiotion 수정, 제대로 동작 안할 듯
    public void removeItem(int position) {
        mContactList.remove(position);
        notifyItemRemoved(position - 1);
    }
}
