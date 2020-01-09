package com.doris.blowup;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.doris.blowup.listener.SwitchBackgroundListener;
import com.doris.blowup.sensor.AudioManagerSensor;
import com.doris.blowup.sensor.MySensor;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

//
// Created by dingying on 2020/1/8.
//
public class DandelionActivity extends Activity implements View.OnTouchListener {

    private final static  int FOR_D = 0;


    private GestureDetector mGesture;

    private SwitchBackgroundListener switchBackgroundListener;

    private Button btnAgain;

    private RelativeLayout background;

    private ImageView after;

    private ImageView before;

    private ImageView chui1, chui2, chui3, chui4, chui5, chui6, chui7, chui8,
            chui9, chui10;

    private Animation animDisappear;
    private Animation animAppear;

    private RelativeLayout relayout;

    private int w;

    private int h;

    private static int VARIABLE = 200;

    private MySensor blowSensor;

    public static final int BLOW_START = 0;
    public static final int BLOWING = 1;
    public static final int BLOW_END = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dandelion);
        initView();
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels;
        h = dm.heightPixels;

        blowSensor = AudioManagerSensor.getInstance(handler);
        blowSensor.start();
    }

    BlowHandler handler = new BlowHandler();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return this.mGesture.onTouchEvent(event);
    }

    class BlowHandler extends Handler {
        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(BLOW_END), delayMillis);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BLOW_START:
                    Log.i("dandelion","dandelion blow start");
                    blowSensor.shutDown();
                    updateOnBlow();
                    break;
                case BLOW_END:
                    Log.i("dandelion","dandelion blow end");
                    sleep(100);
                    break;
                default:
                    break;
            }
        }
    };

    private void initView(){
        background = findViewById(R.id.background);
        btnAgain = findViewById(R.id.again_btn);
        after = (ImageView) findViewById(R.id.chui_res);
        before = (ImageView) findViewById(R.id.chui_pugongying);
        relayout = (RelativeLayout) findViewById(R.id.chui_yihou);
        chui1 = (ImageView) findViewById(R.id.chui1);
        chui2 = (ImageView) findViewById(R.id.chui2);
        chui3 = (ImageView) findViewById(R.id.chui3);
        chui4 = (ImageView) findViewById(R.id.chui4);
        chui5 = (ImageView) findViewById(R.id.chui5);
        chui6 = (ImageView) findViewById(R.id.chui6);
        chui7 = (ImageView) findViewById(R.id.chui7);
        chui8 = (ImageView) findViewById(R.id.chui8);
        chui9 = (ImageView) findViewById(R.id.chui9);
        chui10 = (ImageView) findViewById(R.id.chui10);

        animDisappear = AnimationUtils.loadAnimation(this, R.anim.disappear);
        animAppear = AnimationUtils.loadAnimation(this,R.anim.appear);

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                before.startAnimation(animAppear);
                after.startAnimation(animAppear);
                blowSensor.start();
                Log.i("AGAIN","invisible");
                btnAgain.setVisibility(View.INVISIBLE);
            }
        });

        switchBackgroundListener = new SwitchBackgroundListener(background,FOR_D);
        mGesture = new GestureDetector(switchBackgroundListener);
        background.setOnTouchListener(this);
        background.setLongClickable(true);
    }


    public void updateOnBlow(){
        before.startAnimation(animDisappear);
        animDisappear.setAnimationListener(afterBlowAnimListener);
    }

    private AnimationListener afterBlowAnimListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
            relayout.startAnimation(animAppear);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }



        @Override
        public void onAnimationEnd(Animation animation) {
            //吹后蒲公英显现，结束--播放蒲公英飞舞动画---
            AnimationSet ani = createAnimation();
            ani.setAnimationListener(afterFlyAniListener);
            chui1.startAnimation(ani);
            chui2.startAnimation(createAnimation());
            chui3.startAnimation(createAnimation());
            chui4.startAnimation(createAnimation());
            chui5.startAnimation(createAnimation());
            chui6.startAnimation(createAnimation());
            chui7.startAnimation(createAnimation());
            chui8.startAnimation(createAnimation());
            chui9.startAnimation(createAnimation());
            chui10.startAnimation(createAnimation());
            //
        }
    };


    private AnimationListener afterFlyAniListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            after.startAnimation(AnimationUtils.loadAnimation(
                    DandelionActivity.this, R.anim.disappear));
            btnAgain.setVisibility(View.VISIBLE);
        }
    };

    private AnimationSet createAnimation() {
        //蒲公英向上飞的随机组合动画
        AnimationSet set = new AnimationSet(false);
        int c = createR();
        TranslateAnimation translateAnimationX = new TranslateAnimation(0, c,
                0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, h * -1);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(createTime());
        set.setFillAfter(true);
        set.setFillEnabled(true);
        return set;
    }


    private int createR() {
        int temp = (int) Math.round(Math.random() * VARIABLE + 100);
        return temp;
    }

    private int createTime() {
        return (int) Math.round(Math.random() * 3000 + 1000);
    }



    @Override
    protected void onStart() {
        super.onStart();
        blowSensor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        blowSensor.shutDown();

    }

    @Override
    protected void onDestroy() {
        blowSensor.destory();
        super.onDestroy();

    }
}
