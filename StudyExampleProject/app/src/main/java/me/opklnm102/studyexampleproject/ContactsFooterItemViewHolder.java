package me.opklnm102.studyexampleproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-04-02.
 */
public class ContactsFooterItemViewHolder  extends RecyclerView.ViewHolder {

    public final TextView tvTitle;

    public final View mView;

    public ContactsFooterItemViewHolder(View itemView) {
        super(itemView);

        mView = itemView;

        tvTitle = (TextView) itemView.findViewById(R.id.textView_title);
    }

    public void bind(String strTitle) {
        tvTitle.setText(strTitle);
    }
}
