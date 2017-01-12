package me.dong.recyclerviewstudy.ui.boxes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.dong.recyclerviewstudy.R;
import me.dong.recyclerviewstudy.model.TextBox;


public class BoxTextViewHolder extends RecyclerView.ViewHolder implements IBaseViewHolder<TextBox> {

    @BindView(R.id.et_text)
    EditText mEtText;

    public static BoxTextViewHolder newInstance(@NonNull ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_box_list, parent, false);
        return new BoxTextViewHolder(itemView);
    }

    public BoxTextViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(TextBox textBox) {

    }
}
