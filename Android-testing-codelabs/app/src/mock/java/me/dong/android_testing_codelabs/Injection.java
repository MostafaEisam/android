package me.dong.android_testing_codelabs;

import me.dong.android_testing_codelabs.data.FakeNotesServiceApiImpl;
import me.dong.android_testing_codelabs.data.NoteRepositories;
import me.dong.android_testing_codelabs.data.NotesRepository;
import me.dong.android_testing_codelabs.util.FakeImageFileImpl;
import me.dong.android_testing_codelabs.util.ImageFile;

/**
 * Enables injection of mock implementations for {@link ImageFile} and
 * {@link NotesRepository} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static ImageFile provideImageFile(){
        return new FakeImageFileImpl();
    }

    public static NotesRepository provideNotesRepository(){
        return NoteRepositories.getInMemoryRepoInstance(new FakeNotesServiceApiImpl());
    }
}
