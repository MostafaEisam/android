package me.dong.exexpandablerecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dong on 2016-11-20.
 */

public class ListHeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView tvHeaderTitle;
    public ImageView ivExpandToggle;
    public Item refferalItem;

    public ListHeaderViewHolder(View itemView) {
        super(itemView);
        tvHeaderTitle = (TextView) itemView.findViewById(R.id.tv_header_title);
        ivExpandToggle = (ImageView) itemView.findViewById(R.id.iv_expand_toggle);
    }
}
