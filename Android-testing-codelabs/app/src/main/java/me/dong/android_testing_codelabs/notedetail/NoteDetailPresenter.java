package me.dong.android_testing_codelabs.notedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.dong.android_testing_codelabs.data.Note;
import me.dong.android_testing_codelabs.data.NotesRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link NoteDetailFragment}), retrieves the data and updates
 * the UI as required.
 */
public class NoteDetailPresenter implements NoteDetailContract.Presenter {

    private final NotesRepository mNotesRepository;

    private final NoteDetailContract.View mNoteDetailView;

    public NoteDetailPresenter(@NonNull NotesRepository notesRepository,
                               @NonNull NoteDetailContract.View noteDetailView){
        mNotesRepository = checkNotNull(notesRepository,  "notesRepository cannot be null!");
        mNoteDetailView = checkNotNull(noteDetailView, "noteDetailView cannot be null!");
    }

    @Override
    public void openNote(@Nullable String noteId) {
        if(null == noteId || noteId.isEmpty()){
            mNoteDetailView.showMissingNote();
            return;
        }

        mNoteDetailView.setProgressIndicator(true);
        mNotesRepository.getNote(noteId, new NotesRepository.GetNoteCallback() {
            @Override
            public void onNoteLoaded(Note note) {
                mNoteDetailView.setProgressIndicator(false);
                if(null == note){
                    mNoteDetailView.showMissingNote();
                }else {
                    showNote(note);
                }
            }
        });
    }

    private void showNote(Note note){
        String title = note.getTitle();
        String description = note.getDescription();
        String imageUrl = note.getImageUrl();

        if(title != null && title.isEmpty()){
            mNoteDetailView.hideTitle();
        }else {
            mNoteDetailView.showTitle(title);
        }

        if(description != null && description.isEmpty()){
            mNoteDetailView.hideDescription();
        }else {
            mNoteDetailView.showDescription(description);
        }

        if(imageUrl != null){
            mNoteDetailView.showImage(imageUrl);
        }else {
            mNoteDetailView.hideImage();
        }
    }
}
