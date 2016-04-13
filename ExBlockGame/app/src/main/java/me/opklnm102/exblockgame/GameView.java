package me.opklnm102.exblockgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.preference.PreferenceManager;
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

    private int mLife;  //Life 기록
    private long mGameStartTime; //시간을 기록

    //클리어 판정을 위해 블록을 관리
    private ArrayList<Block> mBlockArrayList;

    //다른 스레드로부터 처리를 받아들일 경우 사용하면 편하다.
    //하고 싶은 작업을 전달하면 Thread는 전달된 Handler를 차례로 처리
    private Handler mHandler;

    private final Bundle mSaveInstanceState;

    //값을 저장할 Key
    private static final String KEY_LIFE = "life";
    private static final String KEY_GAME_START_TIME = "game_start_time";
    private static final String KEY_BALL = "ball";
    private static final String KEY_BLOCK = "block";

    //값을 저장한다,
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_LIFE, mLife);
        outState.putLong(KEY_GAME_START_TIME, mGameStartTime);

        //Ball의 save()가 Bundle를 반환하므로 중첩하는 형태로 저장
        outState.putBundle(KEY_BALL, mBall.save(getWidth(), getHeight()));

        //블록은 100개를 다른 키로 저장해야 하므로 연속된 키로 저장
        for (int i = 0; i < BLOCK_COUNT; i++) {
            outState.putBundle(KEY_BLOCK + String.valueOf(i), mBlockArrayList.get(i).save());
        }
    }

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

    //부서지지 않은 블록의 개수를 센다
    private int getBlockCount() {
        int count = 0;
        for (Block block : mBlockArrayList) {
            if (block.isExist()) {
                count++;
            }
        }
        return count;
    }

    public void readyObjects(int width, int height) {
        mBlockWidth = width / 10;
        mBlockHeight = height / 20;

        //Block 생성
        mBlockArrayList = new ArrayList<>();
        for (int i = 0; i < BLOCK_COUNT; i++) {
            float blockTop = i / 10 * mBlockHeight;
            float blockLeft = i % 10 * mBlockWidth;
            float blockBottom = blockTop + mBlockHeight;
            float blockRight = blockLeft + mBlockWidth;
            mBlockArrayList.add(new Block(blockTop, blockLeft, blockBottom, blockRight));
        }

        //게임 그리기
        mDrawableItems = new ArrayList<>();
        mDrawableItems.addAll(mBlockArrayList);
        mPad = new Pad(height * 0.8f, height * 0.85f);
        mDrawableItems.add(mPad);
        mPadHalfWidth = width / 10;
        mBallRadius = width < height ? width / 40 : height / 40;
        mBall = new Ball(mBallRadius, width / 2, height / 2);
        mDrawableItems.add(mBall);
        mLife = 5;
        mGameStartTime = System.currentTimeMillis();

        //null이면 초기상태이므로 복원 처리를 안한다.
        if (mSaveInstanceState != null) {
            mLife = mSaveInstanceState.getInt(KEY_LIFE);
            mGameStartTime = mSaveInstanceState.getLong(KEY_GAME_START_TIME);
            mBall.restore(mSaveInstanceState.getBundle(KEY_BALL), width, height);
            for (int i = 0; i < BLOCK_COUNT; i++) {
                mBlockArrayList.get(i).restore(mSaveInstanceState.getBundle(KEY_BLOCK + String.valueOf(i)));
            }
        }

    }

    public GameView(final Context context, Bundle savedInstanceState) {
        super(context);
        //SurfaceTexture를 사용할 수 있는 상태인지 검출
        setSurfaceTextureListener(this);
        setOnTouchListener(this);
        mSaveInstanceState = savedInstanceState;

        //인수를 지정하지 않으면 생성된 Thread에서 실행
        mHandler = new Handler() {

            //UI Thread에서 실행되는 Handler
            @Override
            public void handleMessage(Message msg) {
                //실행할 처리
                Intent intent = new Intent(context, ClearActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtras(msg.getData());
                context.startActivity(intent);
            }
        };
    }

    public void start() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean isEnableSound = sharedPreferences.getBoolean("enable_sound", true);
        final boolean isEnableVibrator = sharedPreferences.getBoolean("enable_vibrate", true);

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.FILL);

                /**
                 ToneGenerator
                 원하는 효과음을 내는게 아니라 미리 준비된 효과음을 낸다.
                 푸시버튼식 전화기의 버튼음, 연결음 등에 사용하는 효과음을 쉽게 재생할 수 있는 라이브러리

                 복잡한 효과음을 생성하여 재생하고 싶을 때는 파형 데이터를 재생할 수 있는 AudioTrack을 사용
                 BGM등으로 음악 파일 등을 재생하고 싶을 경우에는 MediaPlayer 사용
                 */
                //(스트림의 종류, 음량)
                //스트림의 종류 - 단말의 음량설정에 관계, 게임 중 효과음은 음악처럼 다루므로 STREAM_MUSIC
                //음량 - 단말에 설정된 음량을 최대로 해서 거기서 백분율로 지정, 단말의 음량을 그대로 사용할 것이므로 MAX_VOLUME
                ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);

                //진동 permission 필요. 원하는 지점에서 vibrate() 호출
                Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

                int collisionTime = 0;  //효과음이 달라질 때까지 남은 시간
                int soundIndex = 0;  //현재 효과음
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
                            if (isEnableSound) {
                                toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 10);
                            }
                            if (isEnableVibrator) {
                                vibrator.vibrate(3);
                            }

                        }
                        if (ballTop < 0) {
                            mBall.setSpeedY(-mBall.getSpeedY());  //세로 방향 벽에 부딛혔으므로 세로 속도를 반전
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 10);
                            vibrator.vibrate(3);
                        }
                        //바닥에 떨어진 판정 추가
                        //공이 바닥에 떨어졌는지는 공의 윗면이 화면의 높이를 넘어갔는지로 판단
                        if (ballTop < getHeight()) {
                            if (mLife > 0) {
                                mLife--;
                                mBall.reset();
                            } else {
                                toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE);  //이 효과음은 길이가 정해져 있다.
                                vibrator.vibrate(3);
                                unlockCanvasAndPost(canvas);
                                Message message = Message.obtain();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean(ClearActivity.EXTRA_IS_CLEAR, false);
                                bundle.putInt(ClearActivity.EXTRA_BLOCK_COUNT, getBlockCount());
                                bundle.putLong(ClearActivity.EXTRA_TIME, System.currentTimeMillis() - mGameStartTime);
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                                return;
                            }
                        }

                        //블록과 공의 충돌 판정
                        Block leftBlock = getBlock(ballLeft, mBall.getY());
                        Block topBlock = getBlock(mBall.getX(), ballTop);
                        Block rightBlock = getBlock(ballRight, mBall.getY());
                        Block bottomBlock = getBlock(mBall.getY(), ballBottom);

                        //게임 클리어는 블록과 공이 충돌한 순간에만 일어나므로 공과 블록의 충돌판정에 클리어 처리를 추가
                        //충돌이 한군데라도 발생하면 true, 아니면 false
                        boolean isCollision = false;  //한 건 이상 충돌이 발생했는지를 기록하는 플래그


                        //충돌한 블록이 존재하면 충돌 처리를 한다.
                        if (leftBlock != null) {
                            mBall.setSpeedX(-mBall.getSpeedX());
                            leftBlock.collision();
                            isCollision = true;
                        }
                        if (topBlock != null) {
                            mBall.setSpeedY(-mBall.getSpeedY());
                            topBlock.collision();
                            isCollision = true;
                        }
                        if (rightBlock != null) {
                            mBall.setSpeedX(-mBall.getSpeedX());
                            rightBlock.collision();
                            isCollision = true;
                        }
                        if (bottomBlock != null) {
                            mBall.setSpeedY(-mBall.getSpeedY());
                            bottomBlock.collision();
                            isCollision = true;
                        }

                        //연속으로 블록을 격파할 때 효과음 달라지게
                        if (isCollision) {
                            //블록에 부딫힌 경우
                            if (collisionTime > 0) {
                                //일정 시간 내에 부딪힌 경우 효과음을 바꾼다
                                if (soundIndex < 15) {
                                    soundIndex++;
                                }
                            } else {
                                //일정 기간 내에 부딫히지 않은 경우 효과음을 되돌린다.
                                soundIndex = 1;
                            }
                            collisionTime = 10;
                            toneGenerator.startTone(soundIndex, 10);
                            vibrator.vibrate(3);
                        } else if (collisionTime > 0) {
                            //블록에 부딪히지 않은 경우 남은 시간을 줄인다.
                            collisionTime--;
                        }

                        //패드와 공의 충돌 판정 -> 공의 밑면이 패드를 넘는 순간
                        //패드의 윗면 좌표와 공의 속도 획득
                        float padTop = mPad.getTop();
                        float ballSpeedY = mBall.getSpeedY();
                        //공의 밑면이 패드를 넘는순간, 직전에 패드 윗면보다 위에 있었는지, 패드에 닿았는지(패드왼, 공오. 패드오, 공왼 비교)
                        if (ballBottom > padTop && ballBottom - ballSpeedY < padTop && padLeft < ballRight && padRight > ballLeft) {

                            //미리 준비된 효과음 재생
                            //(재생할 효과음, 재생 시간(1/1000초 단위))
                            // TONE_DTMF_0 - 전화기의 0번을 눌렀을 때 나는 소리
                            // 10 - 0.01초 재생
                            toneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 10);
                            vibrator.vibrate(3);

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

                        if (isCollision && getBlockCount() == 0) {
                            Message message = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(ClearActivity.EXTRA_IS_CLEAR, true);
                            bundle.putInt(ClearActivity.EXTRA_BLOCK_COUNT, 0);
                            bundle.putLong(ClearActivity.EXTRA_TIME, System.currentTimeMillis() - mGameStartTime);
                            message.setData(bundle);
                            mHandler.sendMessage(message);
                        }
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
                toneGenerator.release();  //toneGenerator를 릴리스
            }  //run 끝
        });  //Runnable 끝
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
