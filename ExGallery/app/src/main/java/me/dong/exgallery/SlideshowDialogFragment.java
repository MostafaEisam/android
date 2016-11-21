package me.dong.exgallery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import me.dong.exgallery.model.Image;

/**
 * Created by Dong on 2016-11-21.
 */

public class SlideshowDialogFragment extends DialogFragment {

    private String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ArrayList<Image> mImages;
    private ViewPager mViewPager;
    private MyViewPagerAdapter mMyViewPagerAdapter;
    private TextView tvlblCount, tvlblTitle, tvlblDate;
    private int selectedPosition = 0;

    static SlideshowDialogFragment newInstance() {
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_slider, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tvlblCount = (TextView) view.findViewById(R.id.tv_lbl_count);
        tvlblTitle = (TextView) view.findViewById(R.id.tv_lbl_title);
        tvlblDate = (TextView) view.findViewById(R.id.tv_lbl_date);

        mImages = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG, "images size: " + mImages.size());

        mMyViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mMyViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                displayMetaInfo(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        setCurrentItem(selectedPosition);

        return view;
    }

    private void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position, false);
        displayMetaInfo(position);
    }

    private void displayMetaInfo(int position) {
        tvlblCount.setText((position + 1) + " of " + mImages.size());

        Image image = mImages.get(position);
        tvlblTitle.setText(image.getName());
        tvlblDate.setText(image.getTimestamp());
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater mLayoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            return mImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = mLayoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            ImageView ivPreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image = mImages.get(position);

            Glide.with(getActivity()).load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPreview);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
