package me.opklnm102.studyexampleproject.views.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.activities.DetailInfoActivity;
import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.views.adapters.ContactsListAdapter;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsListItemViewholder extends RecyclerView.ViewHolder {

    private final Context mContext;

    public final ImageView ivProfile;
    public final TextView tvName;
    public final TextView tvPhoneNumber;

    public final View mView;

    ContactsListAdapter.OnListItemClickListener listener;

    public ContactsListItemViewholder(View itemView, ContactsListAdapter.OnListItemClickListener listener) {
        super(itemView);

        mContext = itemView.getContext();

        mView = itemView;

        this.listener = listener;

        ivProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
        tvName = (TextView) itemView.findViewById(R.id.textView_name);
        tvPhoneNumber = (TextView) itemView.findViewById(R.id.textView_phone_number);
    }

    public void bind(final ContactItem contactItem) {
        ivProfile.setImageResource(contactItem.getProfileImg());
        tvName.setText(contactItem.getName());
        tvPhoneNumber.setText(contactItem.getPhoneNumber());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener != null){
                    listener.onListItemClick(contactItem);
                }
            }
        });
    }

}
