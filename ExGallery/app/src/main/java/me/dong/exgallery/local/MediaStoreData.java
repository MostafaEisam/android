package me.dong.exgallery.local;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A data model containing data for a single media item.
 */
public class MediaStoreData implements Parcelable {

    public final long rowId;
    public final Uri uri;
    public final String mimeType;
    public final long dateModified;
    public final int orientation;
    public final Type type;
    public final long dateTaken;

    public MediaStoreData(long rowId, Uri uri, String mimeType, long dateModified, int orientation, Type type, long dateTaken) {
        this.rowId = rowId;
        this.uri = uri;
        this.mimeType = mimeType;
        this.dateModified = dateModified;
        this.orientation = orientation;
        this.type = type;
        this.dateTaken = dateTaken;
    }

    public static final Creator<MediaStoreData> CREATOR = new Creator<MediaStoreData>() {
        @Override
        public MediaStoreData createFromParcel(Parcel in) {
            return new MediaStoreData(in);
        }

        @Override
        public MediaStoreData[] newArray(int size) {
            return new MediaStoreData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    MediaStoreData(Parcel in) {
        rowId = in.readLong();
        uri = Uri.parse(in.readString());
        mimeType = in.readString();
        dateTaken = in.readLong();
        dateModified = in.readLong();
        orientation = in.readInt();
        type = Type.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(rowId);
        parcel.writeString(uri.toString());
        parcel.writeString(mimeType);
        parcel.writeLong(dateTaken);
        parcel.writeLong(dateModified);
        parcel.writeInt(orientation);
        parcel.writeString(type.name());
    }

    @Override
    public String toString() {
        return "MediaStoreData{"
                + "rowId=" + rowId
                + ", uri=" + uri
                + ", mimeType='" + mimeType + '\''
                + ", dateModified=" + dateModified
                + ", orientation=" + orientation
                + ", type=" + type
                + ", dateTaken=" + dateTaken
                + '}';
    }

    /**
     * The type of data.
     */
    public enum Type{
        VIDEO,
        IMAGE
    }
}
