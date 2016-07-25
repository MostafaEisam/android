package me.dong.exmvp.presenter;


import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import me.dong.exmvp.view.MainActivity;
import me.dong.exmvp.MainMVP;
import me.dong.exmvp.Note;
import me.dong.exmvp.model.MainModel;

/**
 * Created by Dong on 2016-07-24.
 */
public class MainPresenter implements MainMVP.RequiredPresenterOps, MainMVP.PresenterOps {

    // Layer View reference
    private WeakReference<MainMVP.RequiredViewOps> mView;

    // Layer Model reference
    private MainMVP.ModelOps mModel;

    // Configuration change state
    private boolean mIsChangingConfig;

    public MainPresenter(MainMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new MainModel(this);
    }

    /**
     * Sent from Activity after a configuration changes
     *
     * @param view View reference
     */
    @Override
    public void onConfigurationChanged(MainMVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Receives {@link MainActivity#onDestroy()} event
     *
     * @param isChangingConfig Config change state
     */
    @Override
    public void onDestroy(boolean isChangingConfig) {
        mView = null;
        mIsChangingConfig = isChangingConfig;
        if (!isChangingConfig) {
            mModel.onDestroy();
        }
    }

    /**
     * Called by user interaction from {@link MainActivity}
     * creates a new Note
     */
    @Override
    public void newNote(String textNote) {
        Note note = new Note();
        note.setText(textNote);
        note.setDate(getDate());
        mModel.insertNote(note);
    }

    private Date getDate() {
        Calendar c = Calendar.getInstance();
        return new Date(c.getTimeInMillis());
    }

    /**
     * Called from {@link MainActivity},
     * Removes a Note
     */
    @Override
    public void deletaNote(Note note) {
        mModel.removeNote(note);
    }

    /**
     * Called from {@link MainModel}
     * when a Note is inserted successfully
     */
    @Override
    public void onNoteInsert(Note newNote) {
        mView.get().showToast("New register added at " + newNote.getDate());
    }

    /**
     * Receives call from {@link MainModel}
     * when Note is removed
     */
    @Override
    public void onNoteRemoved(Note noteRemoved) {
        mView.get().showToast("Note removed");
    }

    @Override
    public void onError(String errorMsg) {
        mView.get().showAlert(errorMsg);
    }
}
