package me.opklnm102.studyexampleproject.views.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.models.Contact;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsListItemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final Context mContext;

    public final ImageView ivProfile;
    public final TextView tvName;
    public final TextView tvPhoneNumber;

    public final View mView;

    public ContactsListItemViewholder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        mView = itemView;
        ivProfile = (ImageView) itemView.findViewById(R.id.imageView_profile);
        tvName = (TextView) itemView.findViewById(R.id.textView_name);
        tvPhoneNumber = (TextView) itemView.findViewById(R.id.textView_phone_number);

        mView.setOnClickListener(this);
    }

    public void bind(Contact contact) {
        ivProfile.setImageResource(contact.getProfileImg());
        tvName.setText(contact.getName());
        tvPhoneNumber.setText(contact.getPhoneNumber());
    }

    @Override
    public void onClick(View v) {

//        Intent intent = new Intent(mContext, );
//            mContext.startActivity(intent);

    }
}
