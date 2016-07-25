package me.dong.exmvp.model;


import me.dong.exmvp.MainMVP;
import me.dong.exmvp.presenter.MainPresenter;
import me.dong.exmvp.Note;

/**
 * Created by Dong on 2016-07-24.
 */
public class MainModel implements MainMVP.ModelOps {

    // Presenter reference
    private MainMVP.RequiredPresenterOps mPresenter;

    public MainModel(MainMVP.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
    }

    // Insert Note in DB
    @Override
    public void insertNote(Note note) {
        // data business logic
        // ...
        mPresenter.onNoteInsert(note);
    }

    // Removes Note from DB
    @Override
    public void removeNote(Note note) {
        // data business logic
        // ...
        mPresenter.onNoteRemoved(note);
    }

    /**
     * Sent from {@link MainPresenter#onDestroy(boolean)}
     * Should stop/kill operations that could be running
     * and aren't needed anymore
     */
    @Override
    public void onDestroy() {
        // destroying actions
    }
}
