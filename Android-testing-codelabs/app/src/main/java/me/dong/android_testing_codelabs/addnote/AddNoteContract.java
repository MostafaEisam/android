package me.dong.android_testing_codelabs.addnote;

import android.support.annotation.NonNull;

import java.io.IOException;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AddNoteContract {

    /**
     * Presenter -> View
     */
    interface View {
        void showEmptyNoteError();

        void showNotesList();

        void openCamera(String saveTo);

        void showImagePreview(@NonNull String uri);

        void showImageError();
    }

    /**
     * View -> Presenter
     */
    interface Presenter {
        void saveNote(String title, String description);

        void takePicture() throws IOException;

        void imageAvailable();

        void imageCaptureFailed();
    }
}
