package com.example.user.imageloader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2015-05-12.
 */
public class ImageLsitActivity extends AppCompatActivity {
    ListView listView;
    List list;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        list = (ArrayList<Photo>) getIntent().getSerializableExtra("list");
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ImageAdapter(this));

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        ImageLoader.getInstance().init(config.build());
    }

    class ImageAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        //private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        //private DisplayImageOptions options;

        private ImageAdapter(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//            options = new DisplayImageOptions.Builder()
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .considerExifParams(true)
//                    .displayer(new RoundedBitmapDisplayer(20)).build();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.textItem);
                holder.image = (ImageView) view.findViewById(R.id.imageItem);
                view.setTag(holder);
                Log.e("holder","new");
            } else {
                Log.e("holder","exist");
                holder = (ViewHolder) view.getTag();
            }

            Photo p = (Photo) list.get(position);

            Log.e("photo Title",p.getTitle());
            Log.e("photo URL",p.getImageUrl());

            holder.text.setText(p.getTitle());

            ImageLoader.getInstance().displayImage(p.getImageUrl(),holder.image);//,options,animateFirstListener);

            return view;
        }
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
    }

//    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
//
//        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
//
//        @Override
//        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//            if (loadedImage != null) {
//                ImageView imageView = (ImageView) view;
//                boolean firstDisplay = !displayedImages.contains(imageUri);
//                if (firstDisplay) {
//                    FadeInBitmapDisplayer.animate(imageView, 500);
//                    displayedImages.add(imageUri);
//                }
//            }
//        }
//    }
}
