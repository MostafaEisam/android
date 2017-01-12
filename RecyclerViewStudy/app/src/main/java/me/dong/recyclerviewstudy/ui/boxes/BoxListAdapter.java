package me.dong.recyclerviewstudy.ui.boxes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.dong.recyclerviewstudy.common.OnItemClickListener;
import me.dong.recyclerviewstudy.common.Constants;
import me.dong.recyclerviewstudy.factory.BoxFactory;
import me.dong.recyclerviewstudy.model.ABox;

public class BoxListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ABox> mBoxes;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public BoxListAdapter(Context context) {
        this.mContext = context;
        this.mBoxes = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return mBoxes.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.VIEW_TYPE_IMAGE:
                return BoxImageViewHolder.newInstance(parent, mOnItemClickListener);
            case Constants.VIEW_TYPE_TEXT:
                return BoxTextViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ABox aBox = mBoxes.get(position);
        ((IBaseViewHolder<ABox>) holder).bind(aBox);
    }

    @Override
    public int getItemCount() {
        return mBoxes.size();
    }

    public void addBox(int position, BoxFactory.BoxType type){
        ABox aBox = BoxFactory.getBox(type);
        mBoxes.add(position, aBox);
        notifyItemInserted(position);
    }
}
