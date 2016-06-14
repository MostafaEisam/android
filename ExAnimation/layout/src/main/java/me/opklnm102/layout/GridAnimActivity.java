package me.opklnm102.layout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 리스트뷰와 유사하되 그리드뷰는 2차원의 도표 형태이므로 더 다양한 옵션 필요
 * LayoutAnimationController를 상속한 GridLayoutAnimationController객체로 지정
 *
 * 다음 속성을 추가로 가진다.
 * columnDelay - 각 열의 애니메이션을 시작할 지연시간이며 지속시간에 대한 비율
 * rowDelay - 각 행의 애니메이션을 시작할 지연시간이며 지속시간에 대한 비율
 * direction - 애니메이션 방향 지정, 복수지정 가능
 *             left_to_right
 *             right_to_left
 *             top_to_bottom
 *             bottom_to_top
 * directionPriority - 방향의 우선순위 지정.(default. none)
 *                     행과 열이 같은 우선순위를 가지지만 row나 column으로 지정하면 특정 방향이 우선 진행
 */
public class GridAnimActivity extends AppCompatActivity {

    GridView gvImage;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_anim);

        gvImage = (GridView) findViewById(R.id.gridView);
        adapter = new ImageAdapter(this);
        gvImage.setAdapter(adapter);
    }

    class ImageAdapter extends BaseAdapter{

        private Context mContext;

        int[] picture = {
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
        };

        public ImageAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int position) {
            return picture[position % 5];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv;

            if(convertView == null){
                iv = new ImageView(mContext);
                iv.setLayoutParams(new GridView.LayoutParams(80, 60));
                iv.setAdjustViewBounds(false);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }else{
                iv = (ImageView)convertView;
            }

            iv.setImageResource(picture[position%5]);

            return iv;
        }
    }
}
