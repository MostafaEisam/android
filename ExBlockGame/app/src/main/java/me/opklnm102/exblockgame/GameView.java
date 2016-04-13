package me.opklnm102.exblockgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.telecom.PhoneAccount;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by opklnm102 on 2016-04-12.
 * 게임 화면을 그리기 위한 TextureView
 */
public class GameView extends TextureView implements TextureView.SurfaceTextureListener, View.OnTouchListener {

    private Thread mThread;
    volatile private boolean mIsRunnable;  //스레드내에서 갱신됨. 안하면 초기값을 계속 읽는다.
    volatile private float mTouchedX;
    volatile private float mTouchedY;

    private ArrayList<DrawableItem> mDrawableItems;

    private Pad mPad;
    private float mPadHalfWidth;

    private Ball mBall;
    private float mBallRadius;

    private float mBlockWidth;
    private float mBlockHeight;

    static final int BLOCK_COUNT = 100;

    private int mLife;

    //특정좌표에 있는 블록을 가져온다.
    private Block getBlock(float x, float y) {
        int index = (int) (x / mBlockWidth) + (int) (y / mBlockHeight) * 10;
        if (0 <= index && index < BLOCK_COUNT) {
            Block block = (Block) mDrawableItems.get(index);
            if (block.isExist()) {
                return block;
            }
        }
        return null;
    }

    public void readyObjects(int width, int height) {
        mBlockWidth = width / 10;
        mBlockHeight = height / 20;

        mDrawableItems = new ArrayList<>();
        for (int i = 0; i < BLOCK_COUNT; i++) {
            float blockTop = i / 10 * mBlockHeight;
            float blockLeft = i % 10 * mBlockWidth;
            float blockBottom = blockTop + mBlockHeight;
            float blockRight = blockLeft + mBlockWidth;
            mDrawableItems.add(new Block(blockTop, blockLeft, blockBottom, blockRight));
        }
        mPad = new Pad(height * 0.8f, height * 0.85f);
        mDrawableItems.add(mPad);
        mPadHalfWidth = width / 10;
        mBallRadius = width < height ? width / 40 : height / 40;
        mBall = new Ball(mBallRadius, width / 2, height / 2);
        mDrawableItems.add(mBall);
        mLife = 5;
    }

    public GameView(Context context) {
        super(context);
        //SurfaceTexture를 사용할 수 있는 상태인지 검출
        setSurfaceTextureListener(this);
        setOnTouchListener(this);
    }

    public void start() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);

                while (true) {
                    long startTime = System.currentTimeMillis();
                    //앱 실행중 반복 호출
                    synchronized (GameView.this) {
                        if (!mIsRunnable) {
                            break;  //루프 종료
                        }
                        Canvas canvas = lockCanvas();  //Canvas is잠김? null : Canvas
                        if (canvas == null) {
                            continue;  //캔버스가 null이면 다시 루프
                        }
                        if (mBall == null) {
                            continue;
                        }
                        canvas.drawColor(Color.BLACK);  //화면을 칠한다.
                        float padLeft = mTouchedX - mPadHalfWidth;
                        float padRight = mTouchedX + mPadHalfWidth;
                        mPad.setLeftRight(padLeft, padRight);
                        mBall.move();
                        float ballTop = mBall.getY() - mBallRadius;
                        float ballLeft = mBall.getX() - mBallRadius;
                        float ballBottom = mBall.getY() + mBallRadius;
                        float ballRight = mBall.getX() + mBallRadius;

                        //벽충돌 판정
                        if (ballLeft < 0 && mBall.getSpeedX() < 0 || ballRight >= getWidth() && mBall.getSpeedX() > 0) {
                            mBall.setSpeedX(-mBall.getSpeedX());  //가로방향 벽에 부딪혔으므로 가로속도를 반전
                        }
                        if (ballTop < 0) {
                            mBall.setSpeedY(-mBall.getSpeedY());  //세로 방향 벽에 부딛혔으므로 세로 속도를 반전
                        }
                        //바닥에 떨어진 판정 추가
                        //공이 바닥에 떨어졌는지는 공의 윗면이 화면의 높이를 넘어갔는지로 판단
                        if(ballTop < getHeight()){
                            if(mLife > 0){
                                mLife--;
                                mBall.reset();
                            }

                        }

                        //블록과 공의 충돌 판정
                        Block leftBlock = getBlock(ballLeft, mBall.getY());
                        Block topBlock = getBlock(mBall.getX(), ballTop);
                        Block rightBlock = getBlock(ballRight, mBall.getY());
                        Block bottomBlock = getBlock(mBall.getY(), ballBottom);

                        //충돌한 블록이 존재하면 충돌 처리를 한다.
                        if (leftBlock != null) {
                            mBall.setSpeedX(-mBall.getSpeedX());
                            leftBlock.collision();
                        }
                        if (topBlock != null) {
                            mBall.setSpeedY(-mBall.getSpeedY());
                            topBlock.collision();
                        }
                        if (rightBlock != null) {
                            mBall.setSpeedX(-mBall.getSpeedX());
                            rightBlock.collision();
                        }
                        if (bottomBlock != null) {
                            mBall.setSpeedY(-mBall.getSpeedY());
                            bottomBlock.collision();
                        }

                        //패드와 공의 충돌 판정 -> 공의 밑면이 패드를 넘는 순간
                        //패드의 윗면 좌표와 공의 속도 획득
                        float padTop = mPad.getTop();
                        float ballSpeedY = mBall.getSpeedY();
                        //공의 밑면이 패드를 넘는순간, 직전에 패드 윗면보다 위에 있었는지, 패드에 닿았는지(패드왼, 공오. 패드오, 공왼 비교)
                        if (ballBottom > padTop && ballBottom - ballSpeedY < padTop && padLeft < ballRight && padRight > ballLeft) {
                            //패드에 부딫힐때마다 5%씩 증가
                            if (ballSpeedY < mBlockHeight / 3) {
                                ballSpeedY *= -1.05f;
                            } else {
                                ballSpeedY = -ballSpeedY;
                            }

                            //가로 방향 속도 설정
                            float ballSpeedX = mBall.getSpeedX() + (mBall.getX() - mTouchedX) / 10;
                            if (ballSpeedX > mBlockWidth / 5) {
                                ballSpeedX = mBlockWidth / 5;
                            }
                            mBall.setSpeedY(ballSpeedY);
                            mBall.setSpeedX(ballSpeedX);
                        }


                        //인터페이스를 상속받아 아이템그리는걸 통합합
                        for (DrawableItem item : mDrawableItems) {
                            item.draw(canvas, paint);
                        }
                        unlockCanvasAndPost(canvas);  //lock을 해제하고 화면에 그린다.
                        long sleepTime = 16 - System.currentTimeMillis() + startTime;
                        if (sleepTime > 0) {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        mIsRunnable = true;
        mThread.start();
    }

    public void stop() {
        mIsRunnable = false;
    }

    /**
     * 화면은 Canvas를 사용하여 그린다.
     * SurfaceTextureView에 그릴때는 Canvas를 사용
     * 이미지는 화면에 직접 그리지 않고 일단 Canvas에 그린다음, 모든 이미지를 다 그린 시점에 화면에 표시
     * <p/>
     * 일반 위젯은 OS가 관리하는 시점에만 화면에 그려지지만
     * SurfaceTextureView는 프로그래머가 임의의 시점에 그릴 수 있다.
     * SurfaceTextureView는 Canvas에 그리거나 화면에 반영하는 처리가 언제든 가능하므로,
     * 그대로 사용하면 어떤 프로세스가 그리는 도중 다른 프로세스에서도 동시에 그릴 수 있다.
     * -> 오류발생 가능성이 크므로 화면에 그릴때는 하나의 처리만 실행하도록 해야한다.
     * -> lockCanvas()를 이용
     */

    //SurfaceTextureView를 사용할 수 있을때 호출
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        readyObjects(width, height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        readyObjects(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        synchronized (this) {
            return true;  //시스템이 리소스 폐기
        }
//        return false;  //프로그래머가 리소스 폐기
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    //사용자의 터치에 반응
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mTouchedX = event.getX();
        mTouchedY = event.getY();
        return true;
    }
}
