package me.dong.android_testing_codelabs.notes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.dong.android_testing_codelabs.R;
import me.dong.android_testing_codelabs.data.Note;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Dong on 2016-08-31.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> mNotes;
    private NotesFragment.NoteItemListener mNoteItemListener;

    public NotesAdapter(List<Note> notes, NotesFragment.NoteItemListener listener) {
        setList(notes);
        mNoteItemListener = listener;
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        return new NotesViewHolder(itemView, mNoteItemListener);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Note note = mNotes.get(position);

        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public Note getItem(int position) {
        return mNotes.get(position);
    }

    public void replaceData(List<Note> notes) {
        setList(notes);
        notifyDataSetChanged();
    }

    private void setList(List<Note> notes) {
        mNotes = checkNotNull(notes);
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;

        public TextView tvDescription;
        private NotesFragment.NoteItemListener mNoteItemListener;

        public NotesViewHolder(View itemView, NotesFragment.NoteItemListener listener) {
            super(itemView);

            mNoteItemListener = listener;
            tvTitle = (TextView) itemView.findViewById(R.id.note_detail_title);
            tvDescription = (TextView) itemView.findViewById(R.id.note_detail_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Note note = getItem(position);
            mNoteItemListener.onNoteClick(note);
        }
    }
}
