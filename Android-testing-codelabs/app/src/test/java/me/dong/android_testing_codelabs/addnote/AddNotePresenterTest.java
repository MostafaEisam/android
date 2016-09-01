package me.dong.android_testing_codelabs.addnote;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import me.dong.android_testing_codelabs.data.Note;
import me.dong.android_testing_codelabs.data.NotesRepository;
import me.dong.android_testing_codelabs.util.ImageFile;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AddNotePresenter}.
 */
public class AddNotePresenterTest {

    @Mock
    private NotesRepository mNotesRepository;
    @Mock
    private AddNoteContract.View mAddNoteView;
    @Mock
    private ImageFile mImageFile;

    private AddNotePresenter mAddNotePresenter;

    @Before
    public void setupAddNotePresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAddNotePresenter = new AddNotePresenter(mNotesRepository, mAddNoteView, mImageFile);
    }

    @Test
    public void saveNoteToRepository_showsSuccessMessageUi() {
        // When the presenter is asked to save a note
        mAddNotePresenter.saveNote("New Note title", "Some Note Description");

        // Then a note is,
        verify(mNotesRepository).saveNote(any(Note.class));  // saved to the model
        verify(mAddNoteView).showNotesList();  // show in the UI
    }

    @Test
    public void saveNote_emptyNoteShowsErrorUi() {
        // When the presenter is asked to save an empty note
        mAddNotePresenter.saveNote("", "");

        // Then an empty not error is shown in the UI
        verify(mAddNoteView).showEmptyNoteError();
    }

    @Test
    public void takePicture_createsFileAndOpenCamera() throws IOException {
        // When the presenter is asked to take an image
        mAddNotePresenter.takePicture();

        // Then an image file is created and camera is opened
        verify(mImageFile).create(anyString(), anyString());
        verify(mImageFile).getPath();
        verify(mAddNoteView).openCamera(anyString());
    }

    @Test
    public void imageAvaillable_savesImageAndUpdatesUiWithThumbnail(){
        // Given an a stubbed image file
        String imageUrl = "path/to/file";
        when(mImageFile.exists()).thenReturn(true);
        when(mImageFile.getPath()).thenReturn(imageUrl);

        // When an image is made available to the presenter
        mAddNotePresenter.imageAvailable();

        // Then the preview image of the stubbed image is shown in the UI
        verify(mAddNoteView).showImagePreview(contains(imageUrl));
    }

    @Test
    public void imageAvailable_fileDoesNotExistShowsErrorUi(){
        // Given the image file does not exist
        when(mImageFile.exists()).thenReturn(false);

        // When an image is made avaulable to the presenter
        mAddNotePresenter.imageAvailable();

        // Then an error is shown in the UI and the image file is deleted
        verify(mAddNoteView).showImageError();
        verify(mImageFile).delete();
    }

    @Test
    public void noImageAvailable_showsErrorUi(){
        // When the presenter is notified that image capturing failed
        mAddNotePresenter.imageCaptureFailed();

        // Then an error is shown in the Ui and the image file is deleted
        verify(mAddNoteView).showImageError();
        verify(mImageFile).delete();
    }



}
