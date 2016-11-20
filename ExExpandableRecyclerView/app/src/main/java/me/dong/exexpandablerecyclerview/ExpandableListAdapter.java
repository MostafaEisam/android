package me.dong.exexpandablerecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_CHILD = 1;

    private List<Item> mItemList;

    public ExpandableListAdapter(List<Item> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                View itemView = LayoutInflater.from(context).inflate(R.layout.item_header_list, parent, false);
                return new ListHeaderViewHolder(itemView);
            case VIEW_TYPE_CHILD:
                TextView tvItem = new TextView(context);
                tvItem.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                tvItem.setTextColor(0x88000000);
                tvItem.setLayoutParams(
                        new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(tvItem) {
                };
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = mItemList.get(position);
        switch (item.type){
            case VIEW_TYPE_HEADER:
                final ListHeaderViewHolder headerViewHolder = (ListHeaderViewHolder) holder;
                ((ListHeaderViewHolder) holder).refferalItem = item;
                headerViewHolder.tvHeaderTitle.setText(item.text);
                if(item.invisibleChildren == null){
                    headerViewHolder.ivExpandToggle.setImageResource(R.drawable.circle_minus);
                }else {
                    headerViewHolder.ivExpandToggle.setImageResource(R.drawable.circle_plus);
                }

                headerViewHolder.ivExpandToggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(item.invisibleChildren == null){
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = mItemList.indexOf(headerViewHolder.refferalItem);
                            while (mItemList.size() > pos + 1 && mItemList.get(pos + 1).type == VIEW_TYPE_CHILD){
                                item.invisibleChildren.add(mItemList.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            headerViewHolder.ivExpandToggle.setImageResource(R.drawable.circle_plus);
                        }else {
                            int pos = mItemList.indexOf(headerViewHolder.refferalItem);
                            int index = pos + 1;
                            for(Item i : item.invisibleChildren){
                                mItemList.add(index, i);
                                index++;
                            }
                            notifyItemRangeChanged(pos + 1, index - pos -1);
                            headerViewHolder.ivExpandToggle.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;
            case VIEW_TYPE_CHILD:
                ((TextView)holder.itemView).setText(item.text);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).type;
    }
}
