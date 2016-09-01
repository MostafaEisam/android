package me.dong.android_testing_codelabs.notedetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface NoteDetailContract {

    /**
     * Presenter -> View
     */
    interface View {
        void setProgressIndicator(boolean active);

        void showMissingNote();

        void hideTitle();

        void showTitle(String title);

        void showImage(String imageUrl);

        void hideImage();

        void hideDescription();

        void showDescription(String description);
    }

    /**
     * View -> Presenter
     */
    interface Presenter {
        void openNote(@Nullable String noteId);
    }
}
