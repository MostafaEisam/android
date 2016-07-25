package me.dong.exmvp;


public interface MainMVP {

    /**
     * View mandatory methods. Available to Presenter
     * Presenter -> View
     * View가 구현
     */
    interface RequiredViewOps {
        void showToast(String msg);

        void showAlert(String msg);
    }

    /**
     * Operations offered from Presenter to View
     * View -> Presenter
     * Presenter가 구현
     */
    interface PresenterOps {
        void onConfigurationChanged(RequiredViewOps view);

        void onDestroy(boolean isChangingConfig);

        void newNote(String textNote);

        void deletaNote(Note note);
    }

    /**
     * Operations offered from Presenter to Model
     * Model -> Presenter
     * Presenter가 구현
     */
    interface RequiredPresenterOps {
        void onNoteInsert(Note newNote);

        void onNoteRemoved(Note noteRemoved);

        void onError(String errorMsg);

        // Any other returning operation Model -> Presenter
    }

    /**
     * Model operations offered to Presenter
     * Presenter -> Model
     * Model이 구현
     */
    interface ModelOps {
        void insertNote(Note note);

        void removeNote(Note note);

        void onDestroy();
        // Any other data operation
    }
}
