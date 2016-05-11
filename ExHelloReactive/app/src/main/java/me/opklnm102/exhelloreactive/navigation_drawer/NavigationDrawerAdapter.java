package me.opklnm102.exhelloreactive.navigation_drawer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.R;

/**
 * Created by Administrator on 2016-05-09.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerItemViewHolder> {

    List<NavigationItem> mNavigationItemList;

    NavigationDrawerCallbacks mCallbacks;

    int mSelectedPosition;

    int mTouchedPosition = -1;

    public NavigationDrawerAdapter(List<NavigationItem> navigationItems){
        mNavigationItemList = navigationItems;
    }

    public NavigationDrawerCallbacks getNavigationDrawerCallbacks(){
        return mCallbacks;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks){
        mCallbacks = navigationDrawerCallbacks;
    }

    @Override
    public DrawerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer,parent, false);
        return new DrawerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerItemViewHolder holder, int position) {

        NavigationItem item = mNavigationItemList.get(position);

        holder.textView.setText(item.getText());
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(item.getDrawable(), null, null, null);

        holder.itemView.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    touchPosition(position);
                    return false;
                case MotionEvent.ACTION_CANCEL:
                    touchPosition(-position);
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return false;
                case MotionEvent.ACTION_UP:
                    touchPosition(-position);
                    return false;
            }
            return true;
        });
        holder.itemView.setOnClickListener(v -> {
            if(mCallbacks != null){
                mCallbacks.onNavigationDrawerItemSelected(position);
            }
        });

        if(mSelectedPosition == position || mTouchedPosition == position){
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.selected_gray));
        }else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void touchPosition(int position){
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if(lastPosition >= 0){
            notifyItemChanged(lastPosition);
        }
        if (position >= 0){
            notifyItemChanged(position);
        }
    }

    public void selectPosition(int position){
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mNavigationItemList != null ? mNavigationItemList.size() : 0;
    }

    public static class DrawerItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textView_item_name)
        public TextView textView;

        public DrawerItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
