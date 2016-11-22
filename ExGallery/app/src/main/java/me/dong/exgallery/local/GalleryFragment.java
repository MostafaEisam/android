package me.dong.exgallery.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.dong.exgallery.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class GalleryFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<MediaStoreData>> {

    public static final String TAG = GalleryFragment.class.getSimpleName();

    RecyclerView mRvGalleryImage;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        setRetainInstance(true);
        getLoaderManager().initLoader(R.id.loader_id_media_store_data, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        mRvGalleryImage = (RecyclerView) rootView.findViewById(R.id.rv_gallery_image);
        mRvGalleryImage.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
        mRvGalleryImage.setHasFixedSize(true);
        return rootView;
    }

    @Override
    public Loader<List<MediaStoreData>> onCreateLoader(int id, Bundle args) {
        return new MediaStoreDataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MediaStoreData>> loader, List<MediaStoreData> data) {
        GalleryImageListAdapter adapter =
                new GalleryImageListAdapter(getActivity(), data);
        RecyclerViewPreloader<MediaStoreData> preloader =
                new RecyclerViewPreloader<>(adapter, adapter, 20);
        mRvGalleryImage.addOnScrollListener(preloader);
        mRvGalleryImage.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<MediaStoreData>> loader) {
        // Do nothing.
    }
}
