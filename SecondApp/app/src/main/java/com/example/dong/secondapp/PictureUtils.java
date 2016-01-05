package com.example.dong.secondapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.widget.ImageView;

import java.util.Map;

/**
 * Created by Dong on 2015-07-08.
 */
public class PictureUtils {
    /*
    현재의 윈도우 크기에 맞추기 위해 작은 크기로 조정된
    로컬 파일로부터 BitmapDrawable객체를 얻는다.
     */
    //장치의 디폴트 화면 크기로 이미지를 조정한다. 이미지 로딩전 크기를 조절할 코드
    @SuppressWarnings("deprecation")  //deprecation된 경고 무기
    public static BitmapDrawable getScaledDrawable(Activity a, String path){
        Display display = a.getWindowManager().getDefaultDisplay();

        //deprecation된 메서드
        /*ImageView에 완벽하게 맞추기 위해서는 이미지 크기를 조정하는 것이 제일 좋은 방법
        그러나 이미지가 보여질 뷰의 크기는 우리가 필요할 때 사용할 수 없는 경우가 있다.
        ex)onCreateView(..)메서드 내부에서는 ImageView의 크기를 얻을 수 없다.
        따라서 장치의 디폴트 화면 크기(언제든 사용가능)에 맞게 이미지를 조정하는 것이 안전한 방법
        궁극적으로 이미지가 나타날 뷰는 디폴트 화면 크기보다 더 작을 수는 있어도 더 클 수는 없기 때문이다.
         */
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        //디스크 이미지의 크기를 읽는다.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if(srcHeight < destHeight || srcWidth < destWidth){
            if(srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight / destHeight);
            }else{
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(a.getResources(), bitmap);
    }

    //이미지가 더이상 필요 없을 때 클린업할 코드
    public static void cleanImageView(ImageView imageView){
        if(!(imageView.getDrawable() instanceof BitmapDrawable))
            return;

        //메모리를 절약하기 위해 뷰의 이미지를 클린업한다.
        BitmapDrawable b = (BitmapDrawable)imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
        /*
        안드로이드 문서에는 Bitmap.recycle()를 호출할 필요가 없다고 나와있다. 그러나 필요
        Bitmap.recycle()은 우리 비트맵이 사용하던 네이티브 스토리지를 해제시킨다.(비트맵 객체에서 가장 중요한 것)
        (네이티브 스토리지에는 안드로이드 버전에 따라 더 작거나 또는 더 큰 데이터를 저장할 수 있다. 허니콤 이전에는
        모든 자바 Bitmap데이터를 저장했다.)

        비트맵이 사용하던 메모리를 명시적으로 recycle()을 호출하여 해제하지 않더라도 그 메모리는 여전히 클린업될 것이다.
        그러나 바로 되는것이 아니고 파이널라이저(finalizer)의 향후 어떤 시점에서 클린업 될 것이다.
        비트맵 자체가 garbage collection되는 시점에 클린업되는 것이 아니다. 따라서 파이널라이저가 호출되어 클린업되기 전에
        장치의 메모리 부족이 생길 수 있다.

        안드로이드에서 파이널라이저는 언제 실행되는지 애매모호하다. 따라서 예기치 않게 메모리 부족이 생길 수 있는
        이런 종류의 문제는 찾기 어려우며 재현하기도 어렵다. 그러므로 이미지가 크다면 recycle()을 호출하여 이런 메모리
        문제를 예방하는 것이 좋다.
         */
    }
}
