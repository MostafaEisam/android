package me.opklnm102.exhelloreactive.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.opklnm102.exhelloreactive.AppInfoViewHolder;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.Subscriber;
import rx.observables.AsyncOnSubscribe;

public class AppInfoListAdapter extends RecyclerView.Adapter<AppInfoViewHolder> {

    private Context mContext;

    private List<AppInfo> mAppInfoList;


    public AppInfoListAdapter(Context context) {
        mContext = context;
        mAppInfoList = new ArrayList<>();
    }

    public void addAppInfos(List<AppInfo> appInfos) {
        mAppInfoList.clear();
        mAppInfoList.addAll(appInfos);
        notifyDataSetChanged();
    }

    public void addAppInfo(int position, AppInfo appInfo) {
        if (position < 0) {
            position = 0;
        }
        mAppInfoList.add(position, appInfo);
        notifyItemInserted(position);
    }


    @Override
    public AppInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_appinfo, parent, false);
        return new AppInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppInfoViewHolder holder, int position) {
        AppInfo appInfo = mAppInfoList.get(position);

        holder.bind(appInfo);
    }

    @Override
    public int getItemCount() {
       return mAppInfoList == null ? 0 : mAppInfoList.size();
    }


}
