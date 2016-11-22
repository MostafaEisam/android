package me.dong.exprecacherecyclerview;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatsRecyclerAdapter extends RecyclerView.Adapter<CatsRecyclerAdapter.CatViewHolder> {

    public static final String TAG = CatsRecyclerAdapter.class.getSimpleName();

    private final List<Cat> mCats;
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final Picasso.Builder mPicassoBuilder;

    public CatsRecyclerAdapter(Context context, List<Cat> cats) {
        this.mContext = context;
        this.mCats = cats;
        this.mLayoutInflater = LayoutInflater.from(context);

        mPicassoBuilder = new Picasso.Builder(mContext);
        mPicassoBuilder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_cat_list, parent, false);
        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        Cat current = mCats.get(position);

        mPicassoBuilder.build()
                .load(current.url)
                .into(holder.ivCat, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, " onSuccess");
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, " onError");
                    }
                });

        holder.tvDescription.setText(current.description);
    }

    @Override
    public int getItemCount() {
        return mCats.size();
    }

    static class CatViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCat;
        TextView tvDescription;

        public CatViewHolder(View itemView) {
            super(itemView);
            this.ivCat = (ImageView) itemView.findViewById(R.id.iv_cat_image);
            this.tvDescription = (TextView) itemView.findViewById(R.id.tv_cat_description);
        }
    }
}
