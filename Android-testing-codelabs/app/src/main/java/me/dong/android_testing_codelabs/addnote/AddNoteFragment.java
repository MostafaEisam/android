package me.dong.android_testing_codelabs.addnote;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.io.IOException;

import me.dong.android_testing_codelabs.Injection;
import me.dong.android_testing_codelabs.R;
import me.dong.android_testing_codelabs.util.EspressoIdlingResource;

import static com.google.common.base.Preconditions.checkState;

/**
 * Main UI for the add note screen. Users can enter a note title and description. Images can be
 * added to notes by clicking on the options menu.
 */
public class AddNoteFragment extends Fragment implements AddNoteContract.View {

    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0X1001;

    private AddNoteContract.Presenter mPresenter;

    private TextView mTvTitle;

    private TextView mTvDescription;

    private ImageView mIvThumbnail;

    public AddNoteFragment() {
        // Required empty public constructor
    }


    public static AddNoteFragment newInstance() {
        AddNoteFragment fragment = new AddNoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new AddNotePresenter(Injection.provideNotesRepository(), this, Injection.provideImageFile());

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.fab_add_notes);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveNote(mTvTitle.getText().toString(),
                        mTvDescription.getText().toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_note, container, false);
        mTvTitle = (TextView) root.findViewById(R.id.add_note_title);
        mTvDescription = (TextView) root.findViewById(R.id.add_note_description);
        mIvThumbnail = (ImageView) root.findViewById(R.id.add_note_image_thumbnail);

        setHasOptionsMenu(true);
        setRetainInstance(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.take_picture:
                try{
                    mPresenter.takePicture();
                }catch (IOException ioe){
                    if(getView() != null){
                        Snackbar.make(getView(), getString(R.string.take_picture_error),
                                Snackbar.LENGTH_SHORT).show();
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_addnote_options_menu_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showEmptyNoteError() {
        Snackbar.make(mTvTitle, getString(R.string.empty_note_message), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNotesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void openCamera(String saveTo) {
        // Open the camera to take a picture.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is a camera app installed to handle our Intent
        if(takePictureIntent.resolveActivity(getContext().getPackageManager()) != null){
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(saveTo));
            startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
        }else {
            Snackbar.make(mTvTitle, getString(R.string.cannot_connect_to_camera_message),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showImagePreview(@NonNull String imageUrl) {
        checkState(!TextUtils.isEmpty(imageUrl), "imageUrl cannot be null or empty!");
        mIvThumbnail.setVisibility(View.VISIBLE);

        // The image is loaded in a different thread so in order to UI-test this, an idling resource
        // is used to specify when the app is idle.
        EspressoIdlingResource.increment(); // App is busy until further notice.

        // This app uses Glide for image loading
        Glide.with(this)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new GlideDrawableImageViewTarget(mIvThumbnail){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        EspressoIdlingResource.decrement();  // Set app as idle.
                    }
                });
    }

    @Override
    public void showImageError() {
        Snackbar.make(mTvTitle, getString(R.string.cannot_connect_to_camera_message),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If an image is received, display it on the ImageView
        if(REQUEST_CODE_IMAGE_CAPTURE == requestCode && Activity.RESULT_OK == resultCode){
            mPresenter.imageAvailable();
        }else {
            mPresenter.imageCaptureFailed();
        }
    }
}
