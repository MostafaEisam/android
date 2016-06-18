package opklnm102.me.annivlistwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Collections;


public class AnnivListFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    int mAppWidgetId;
    ArrayList<String> mAnnivArrayList = new ArrayList<>();

    public AnnivListFactory(Context context, Intent intent){
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    /*
    DB에 정보가 있다면 여기서 쿼리를 날려 커서를 얻어놔야 한다.
     */
    @Override
    public void onCreate() {
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
        mAnnivArrayList.add("1월 1일(음) 설날");
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mAnnivArrayList.size();
    }

    //가장 중요
    //각 항목 클릭시 동작 처리 -> PendingIntent 이용
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.list_item_anniv);
        remoteViews.setTextViewText(R.id.textView_anniv, mAnnivArrayList.get(position));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //항목 뷰가 1종류임을 명시
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
