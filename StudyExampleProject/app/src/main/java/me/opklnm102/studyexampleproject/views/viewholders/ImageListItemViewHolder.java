package me.opklnm102.studyexampleproject.views.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.opklnm102.studyexampleproject.R;
import me.opklnm102.studyexampleproject.models.ContactItem;
import me.opklnm102.studyexampleproject.models.ImageItem;

/**
 * Created by Administrator on 2016-04-03.
 */
public class ImageListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final Context mContext;

    public final ImageView ivImage;

    public final View mView;

    public ImageListItemViewHolder(View itemView) {
        super(itemView);

        mContext = itemView.getContext();

        mView = itemView;
        ivImage = (ImageView) itemView.findViewById(R.id.imageView_image);

        mView.setOnClickListener(this);
    }

    public void bind(ImageItem imageItem) {
        ivImage.setImageResource(imageItem.getImgId());
    }

    @Override
    public void onClick(View v) {

//        Intent intent = new Intent(mContext, );
//            mContext.startActivity(intent);

    }
}

