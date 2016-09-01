package me.dong.android_testing_codelabs.data;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */
public class InMemoryNotesRepositoryTest {

    private final static String NOTE_TITLE = "title";

    private static List<Note> NOTES = Lists.newArrayList(new Note("Title1", "Description1"),
            new Note("Title2", "Description2"));

    private InMemoryNotesRepository mInMemoryNotesRepository;

    @Mock
    private NotesServiceApi mNotesServiceApi;

    @Mock
    private NotesRepository.GetNoteCallback mGetNoteCallback;

    @Mock
    NotesRepository.LoadNotesCallback mLoadNotesCallback;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<NotesServiceApi.NotesServiceCallback> mNotesServiceCallbackCaptor;

    @Before
    public void setupNotesRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mInMemoryNotesRepository = new InMemoryNotesRepository(mNotesServiceApi);
    }

    /**
     * 캐시가 된 경우
     */
    @Test
    public void getNotes_repositoryCachesAfterFirstApiCall() {
        // Given a setup Captor to capture callbacks
        // When two calls are issued to the notes repository
        twoLoadCallsToRepository(mLoadNotesCallback);

        // Then notes where only requested once from Service API
        verify(mNotesServiceApi).getAllNotes(any(NotesServiceApi.NotesServiceCallback.class));
    }

    /**
     * 캐시를 지웠을 경우
     */
    @Test
    public void invalidateCache_doesNotCallTheServiceApi() {
        // Given a setup Captor to capture callbacks
        twoLoadCallsToRepository(mLoadNotesCallback);

        //
        mInMemoryNotesRepository.refreshData();
        mInMemoryNotesRepository.getNotes(mLoadNotesCallback);  // Third call to API

        // The notes where requested twice from the Service API (Caching on first and third call)
        verify(mNotesServiceApi, times(2)).getAllNotes(any(NotesServiceApi.NotesServiceCallback.class));
    }

    /**
     * 그냥 호출할 경우
     */
    @Test
    public void getNotes_requestsAllNotesFromServiceApi() {
        // When notes are requested from the notes repository
        mInMemoryNotesRepository.getNotes(mLoadNotesCallback);

        // Then notes are loaded from the service API
        verify(mNotesServiceApi).getAllNotes(any(NotesServiceApi.NotesServiceCallback.class));
    }

    @Test
    public void saveNote_savesNoteToServiceAPIAndInvalidatesCache() {
        // Given a stub note with title and description
        Note newNote = new Note(NOTE_TITLE, "Some Note Description");

        // When a note is saved to the notes repository
        mInMemoryNotesRepository.saveNote(newNote);

        // Then the notes cache is cleared
        assertThat(mInMemoryNotesRepository.mCachedNotes, is(nullValue()));
    }

    @Test
    public void getNote_requestsSingleNoteFromServiceApi() {
        // When a note is requested from the notes repository
        mInMemoryNotesRepository.getNote(NOTE_TITLE, mGetNoteCallback);

        // Then the note is loaded from the service API
        verify(mNotesServiceApi).getNote(eq(NOTE_TITLE), any(NotesServiceApi.NotesServiceCallback.class));
    }

    /**
     * Convenience method that issues two calls to the notes repository
     * 캐시 기능을 확인하기 위해
     */
    private void twoLoadCallsToRepository(NotesRepository.LoadNotesCallback callback) {
        // When notes are requested from repository
        mInMemoryNotesRepository.getNotes(callback);  // First call to API

        // Use the Mockito Captor to capture the callback
        verify(mNotesServiceApi).getAllNotes(mNotesServiceCallbackCaptor.capture());

        // Trigger callback so notes are cached
        mNotesServiceCallbackCaptor.getValue().onLoaded(NOTES);

        mInMemoryNotesRepository.getNotes(callback);  // Second call to API
    }
}
