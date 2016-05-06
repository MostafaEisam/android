package me.opklnm102.exhelloreactive;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.model.AppInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AppInfoViewHolder  extends RecyclerView.ViewHolder{

    @BindView(R.id.textView_name)
    public TextView tvName;

    @BindView(R.id.imageView_icon)
    public ImageView ivIcon;

    public AppInfoViewHolder(View itemView){
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(AppInfo appInfo){
        tvName.setText(appInfo.getName());
        getBitmap(appInfo.getIcon())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ivIcon::setImageBitmap);
    }

    private Observable<Bitmap> getBitmap(final String icon){
        return Observable.create(subscriber -> {
            subscriber.onNext(BitmapFactory.decodeFile(icon));
            subscriber.onCompleted();
        });
    }
}
