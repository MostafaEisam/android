package me.dong.android_testing_codelabs.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.dong.android_testing_codelabs.Injection;
import me.dong.android_testing_codelabs.R;
import me.dong.android_testing_codelabs.addnote.AddNoteActivity;
import me.dong.android_testing_codelabs.data.Note;
import me.dong.android_testing_codelabs.notedetail.NoteDetailActivity;

public class NotesFragment extends Fragment implements NotesContract.View {

    private static final int REQUEST_ADD_NOTE = 1;

    private NotesContract.Presenter mPresenter;

    private NotesAdapter mListAdapter;

    NoteItemListener mNoteItemListener = new NoteItemListener() {
        @Override
        public void onNoteClick(Note clickedNote) {
            mPresenter.openNoteDetails(clickedNote);
        }
    };

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new NotesAdapter(new ArrayList<Note>(0), mNoteItemListener);
        mPresenter = new NotesPresenter(Injection.provideNotesRepository(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadNotes(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If a note was successfully added, show snackbar
        if (REQUEST_ADD_NOTE == requestCode && Activity.RESULT_OK == resultCode) {
            Snackbar.make(getView(), getString(R.string.successfully_saved_note_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView rvNotes = (RecyclerView) root.findViewById(R.id.notes_list);
        rvNotes.setAdapter(mListAdapter);

        int numColumns = getContext().getResources().getInteger(R.integer.num_notes_columns);

        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        // Set up floating action button
        FloatingActionButton fab
                = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_notes);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewNote();
            }
        });

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadNotes(true);
            }
        });

        return root;
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if(getView() == null){
            return;
        }

        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showNotes(List<Note> notes) {
        mListAdapter.replaceData(notes);
    }

    @Override
    public void showAddNote() {
        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        startActivityForResult(intent, REQUEST_ADD_NOTE);
    }

    @Override
    public void showNoteDetaulUi(String noteId) {
        // in it's own Activity, since it makes more sense that way and it gives us the flexibility
        // to show some Intent stubbing.
        Intent intent = new Intent(getContext(), NoteDetailActivity.class);
        intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId);
        startActivity(intent);
    }


    public interface NoteItemListener {
        void onNoteClick(Note clickedNote);
    }


}
