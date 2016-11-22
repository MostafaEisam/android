package me.dong.exgallery.local;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.util.Collections;
import java.util.List;

import me.dong.exgallery.R;

/**
 * Displays {@link me.dong.exgallery.local.MediaStoreData} in a recycler view.
 */
public class GalleryImageListAdapter extends RecyclerView.Adapter<GalleryImageListAdapter.ListViewHolder>
        implements ListPreloader.PreloadSizeProvider<MediaStoreData>,
        ListPreloader.PreloadModelProvider<MediaStoreData> {

    private final List<MediaStoreData> data;
    private final int screenWidth;
    private final Context mContext;

    private int[] actualDimensions;

    public GalleryImageListAdapter(Context context, List<MediaStoreData> data) {
        this.data = data;
        this.mContext = context;
        screenWidth = getScreenWidth(context);

        setHasStableIds(true);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.item_gallery_image_list, parent, false);
        itemView.getLayoutParams().width = screenWidth;

        if (actualDimensions == null) {
            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (actualDimensions == null) {
                        actualDimensions = new int[]{itemView.getWidth(), itemView.getHeight()};
                    }
                    itemView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        MediaStoreData current = data.get(position);

        Key signature =
                new MediaStoreSignature(current.mimeType, current.dateModified, current.orientation);

        Glide.with(mContext)
                .load(current.uri)
                .signature(signature)  // for manual caching
                .fitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).rowId;
    }

    @Override
    public List<MediaStoreData> getPreloadItems(int position) {
        return Collections.singletonList(data.get(position));
    }

    @Override
    public GenericRequestBuilder getPreloadRequestBuilder(MediaStoreData item) {
        MediaStoreSignature signature =
                new MediaStoreSignature(item.mimeType, item.dateModified, item.orientation);
        return Glide.with(mContext)
                .load(item.uri)
                .signature(signature)
                .fitCenter();
    }

    @Override
    public int[] getPreloadSize(MediaStoreData item, int adapterPosition, int perItemPosition) {
        return actualDimensions;
    }

    // Display#getSize(Point)
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @SuppressWarnings("deprecation")
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final int result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            result = size.x;
        } else {
            result = display.getWidth();
        }
        return result;
    }

    public static final class ListViewHolder extends RecyclerView.ViewHolder {

        private final ImageView image;

        public ListViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
