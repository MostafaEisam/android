package com.example.dong.secondapp;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Dong on 2015-07-08.
 */
public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "com.example.dong.secondapp.image_path";

    public static ImageFragment newInstance(String imagePath){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_IMAGE_PATH, imagePath);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        //미니멀리스트(최소한의 요소로 최대한의 효과를 이룸)를 성취하기 위해 프래그먼트의 스타일을
        //제목이 없는 DialogFragment.STYLE_NO_TITLE로 설정
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return fragment;
    }

    /*
    ImageFragment는 AlertDialog에서 제공하는 제목, 버튼들이 필요 없다. 그리고 그런 것들이 없어도 일을
     처리할 수 있다면, onCreateDialog()를 오버라이딩하여 Dialog를 사용하는 것보다 onCreateView()를 오버라이딩하고
      간단한 View를 사용하는 것이 더 명쾌하고, 빠르며, 유연성이 좋다.
     */

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mImageView = new ImageView(getActivity());
        String path = (String)getArguments().getSerializable(EXTRA_IMAGE_PATH);
        BitmapDrawable image = PictureUtils.getScaledDrawable(getActivity(), path);

        mImageView.setImageDrawable(image);

        return mImageView;
    }

    //뷰가 필요없어지면 클린업
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PictureUtils.cleanImageView(mImageView);
    }
}
