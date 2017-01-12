package me.dong.recyclerviewstudy.ui.boxes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dong.recyclerviewstudy.common.OnItemClickListener;
import me.dong.recyclerviewstudy.R;
import me.dong.recyclerviewstudy.model.ImageBox;


public class BoxImageViewHolder extends RecyclerView.ViewHolder implements IBaseViewHolder<ImageBox> {

    @BindView(R.id.iv_image)
    ImageView ivImage;

    OnItemClickListener mOnItemClickListener;

    public static BoxImageViewHolder newInstance(@NonNull ViewGroup parent, OnItemClickListener listener) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_box_list, parent, false);
        return new BoxImageViewHolder(itemView, listener);
    }

    public BoxImageViewHolder(View itemView, OnItemClickListener listener) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        mOnItemClickListener = listener;
    }

    @Override
    public void bind(ImageBox imageBox) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick();
            }
        });
    }
}
