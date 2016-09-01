package me.dong.android_testing_codelabs.notes;

import android.support.annotation.NonNull;

import java.util.List;

import me.dong.android_testing_codelabs.data.Note;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface NotesContract {

    /**
     * Presenter -> View
     */
    interface View {
        void setProgressIndicator(boolean active);

        void showNotes(List<Note> notes);

        void showAddNote();

        void showNoteDetaulUi(String noteId);
    }

    /**
     * View -> Presenter
     */
    interface Presenter {
        void loadNotes(boolean forceUpdate);

        void addNewNote();

        void openNoteDetails(@NonNull Note requestedNote);
    }
}
