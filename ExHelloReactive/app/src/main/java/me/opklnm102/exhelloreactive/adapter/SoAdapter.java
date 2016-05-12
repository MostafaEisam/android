package me.opklnm102.exhelloreactive.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.opklnm102.exhelloreactive.App;
import me.opklnm102.exhelloreactive.R;
import me.opklnm102.exhelloreactive.api.openweathermap.OpenWeatherMapApiManager;
import me.opklnm102.exhelloreactive.api.openweathermap.models.WeatherResponse;
import me.opklnm102.exhelloreactive.api.stackexchange.models.User;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.jakewharton.rxbinding.internal.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2016-05-12.
 */
public class SoAdapter extends RecyclerView.Adapter<SoAdapter.ViewHolder> {

    private static ViewHolder.OpenProfileListener sProfileListener;

    private List<User> mUserList = new ArrayList<>();

    public SoAdapter(List<User> userList) {
        mUserList = userList;
    }

    public void updateUsers(List<User> userList) {
        mUserList.clear();
        mUserList.addAll(userList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_so, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mUserList.size()) {
            User user = mUserList.get(position);
            holder.setUser(user);
        }
    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }

    public void setOpenProfileListener(ViewHolder.OpenProfileListener listener) {
        sProfileListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;

        @BindView(R.id.textView_name)
        TextView tvName;

        @BindView(R.id.textView_city)
        TextView tvCity;

        @BindView(R.id.textView_reputation)
        TextView tvReputation;

        @BindView(R.id.imageView_user)
        ImageView ivUser;

        @BindView(R.id.imageView_city)
        ImageView ivCity;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mView = itemView;
        }

        private Observable<Bitmap> loadBitmap(String url) {
            return Observable
                    .create(subscriber -> {
                        ImageLoader.getInstance().displayImage(url, ivCity, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                subscriber.onError(failReason.getCause());
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                subscriber.onNext(loadedImage);
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                subscriber.onError(new Throwable("Image loading cancelled"));
                            }
                        });
                    });
        }

        public void setUser(User user) {
            tvName.setText(user.getDisplayName());
            tvCity.setText(user.getLocation());
            tvReputation.setText(String.valueOf(user.getReputation()));

            ImageLoader.getInstance().displayImage(user.getProfileImage(), ivUser);

            displayWeatherInfos(user);

            RxView.clicks(mView)
                    .subscribe(onClickEvent -> {
                        checkNotNull(sProfileListener, "Must implement OpenProfileListener");

                        String url = user.getWebsiteUrl();
                        if (url != null && !url.equals("") && !url.contains("search")) {
                            sProfileListener.open(url);
                        } else {
                            sProfileListener.open(user.getLink());
                        }
                    });
        }

        private void displayWeatherInfos(User user) {
            String location = user.getLocation();
            int separatorPosition = getSeparatorPosition(location);

            if (isCityValid(location)) {
                String city = getCity(location, separatorPosition);
                OpenWeatherMapApiManager.getInstance()
                        .getForecastByCity(city)
                        .filter(weatherResponse -> weatherResponse != null)
                        .filter(weatherResponse -> weatherResponse.getWeather().size() > 0)
                        .flatMap(weatherResponse -> {
                            String url = getWeatherIconUrl(weatherResponse);
                            return loadBitmap(url);
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Bitmap>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                App.L.error(e.toString());
                            }

                            @Override
                            public void onNext(Bitmap bitmap) {
                                ivCity.setImageBitmap(bitmap);
                            }
                        });
            }
        }

        private String getWeatherIconUrl(WeatherResponse weatherResponse) {
            return "http://openweathermap.org/img/w/" + weatherResponse.getWeather().get(0).getIcon() + ".png";
        }

        private boolean isCityValid(String location) {
            int separatorPosition = getSeparatorPosition(location);
            return !"".equals(location) && separatorPosition > -1;
        }

        private int getSeparatorPosition(String location) {
            int separatorPosition = -1;

//            checkNotNull(location, "Location can't be null");
            if(location != null){
                separatorPosition = location.indexOf(",");
            }
            return separatorPosition;
        }

        private String getCity(String location, int position) {
            if (location != null) {
                return location.substring(0, position);
            } else {
                return "";
            }
        }

        public interface OpenProfileListener {
            public void open(String url);
        }
    }
}
