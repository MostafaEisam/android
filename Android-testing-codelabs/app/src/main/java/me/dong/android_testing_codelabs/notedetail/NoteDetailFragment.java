package me.dong.android_testing_codelabs.notedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import me.dong.android_testing_codelabs.Injection;
import me.dong.android_testing_codelabs.R;
import me.dong.android_testing_codelabs.util.EspressoIdlingResource;

/**
 * Main UI for the note detail screen.
 */
public class NoteDetailFragment extends Fragment implements NoteDetailContract.View {

    public static final String ARGUMENT_NOTE_ID = "NOTE_ID";

    private NoteDetailContract.Presenter mPresenter;

    private TextView mTvDetailTitle;

    private TextView mTvDetailDescription;

    private ImageView mIvDetailImage;

    public NoteDetailFragment() {
        // Required empty public constructor
    }

    public static NoteDetailFragment newInstance(String noteId) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new NoteDetailPresenter(Injection.provideNotesRepository(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_note_detail, container, false);
        mTvDetailTitle = (TextView) root.findViewById(R.id.note_detail_title);
        mTvDetailDescription = (TextView) root.findViewById(R.id.note_detail_description);
        mIvDetailImage = (ImageView) root.findViewById(R.id.note_detail_image);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        String noteId = getArguments().getString(ARGUMENT_NOTE_ID);
        mPresenter.openNote(noteId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active){
            mTvDetailTitle.setText("");
            mTvDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingNote() {
        mTvDetailTitle.setText("");
        mTvDetailDescription.setText(getString(R.string.no_data));
    }

    @Override
    public void hideTitle() {
        mTvDetailTitle.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        mTvDetailTitle.setVisibility(View.VISIBLE);
        mTvDetailTitle.setText(title);
    }

    @Override
    public void showImage(String imageUrl) {
        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment();  // App is busy until further notice.

        mIvDetailImage.setVisibility(View.VISIBLE);

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mIvDetailImage){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        EspressoIdlingResource.decrement();  // App is idle
                    }
                });
    }

    @Override
    public void hideImage() {
        mIvDetailImage.setImageDrawable(null);
        mIvDetailImage.setVisibility(View.GONE);
    }

    @Override
    public void hideDescription() {
        mTvDetailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        mTvDetailDescription.setVisibility(View.VISIBLE);
        mTvDetailDescription.setText(description);
    }
}
