package me.dong.android_testing_codelabs.notes;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import me.dong.android_testing_codelabs.data.Note;
import me.dong.android_testing_codelabs.data.NotesRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link NotesPresenter}
 */
public class NotesPresenterTest {

    private static List<Note> NOTES = Lists.newArrayList(new Note("Title1", "Description1"),
            new Note("Title2", "Description2"));

    private static List<Note> EMPTY_NOTES = new ArrayList<>(0);

    @Mock
    private NotesRepository mNotesRepository;
    @Mock
    private NotesContract.View mNotesView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<NotesRepository.LoadNotesCallback> mLoadNotesCallbackArgumentCaptor;

    private NotesPresenter mNotesPresenter;

    @Before
    public void setupNotesPresenter(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mNotesPresenter = new NotesPresenter(mNotesRepository, mNotesView);
    }

    @Test
    public void loadNotesFromRepositoryAndLoadIntoView(){
        // Given an initialized NotesPresenter with initialized notes
        // When loading of Notes is requested
        mNotesPresenter.loadNotes(true);

        // Callback is captured and invoked with stubbed notes
        verify(mNotesRepository).getNotes(mLoadNotesCallbackArgumentCaptor.capture());
        mLoadNotesCallbackArgumentCaptor.getValue().onNotesLoaded(NOTES);

        // Then progress indicator is hidden and notes are shown in Ui
        verify(mNotesView).setProgressIndicator(false);
        verify(mNotesView).showNotes(NOTES);
    }

    @Test
    public void clickOnFab_showsAddsNoteUi(){
        // When adding a new note
        mNotesPresenter.addNewNote();

        // Then add note Ui is shown
        verify(mNotesView).showAddNote();
    }

    @Test
    public void clickOnNote_showsDetailUi(){
        // Given a stubbed note
        Note requestNote = new Note("Details Requested", "For this note");

        // When open note details is requested
        mNotesPresenter.openNoteDetails(requestNote);

        // Then note detail Ui is shown
        verify(mNotesView).showNoteDetaulUi(any(String.class));
    }
}
