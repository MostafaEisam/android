package me.dong.android_testing_codelabs.data;

import android.support.annotation.Nullable;

import com.google.common.base.Objects;

import java.util.UUID;

/**
 * Immutable model class for a Note.
 */
public class Note {

    private final String mId;
    @Nullable
    private final String mTitle;
    @Nullable
    private final String mDescription;
    @Nullable
    private final String mImageUrl;

    public Note(String title, String description) {
        this(title, description, null);
    }

    public Note(String title, String description, String imageUrl) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mDescription = description;
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }

    @Nullable
    public String getImageUrl() {
        return mImageUrl;
    }

    public boolean isEmpty() {
        return (mTitle == null || "".equals(mTitle)) &&
                (mDescription == null || "".equals(mDescription));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;
        return Objects.equal(mId, note.mId) &&
                Objects.equal(mId, note.mId) &&
                Objects.equal(mId, note.mId) &&
                Objects.equal(mId, note.mId);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(mId, mTitle, mDescription, mImageUrl);
    }
}
