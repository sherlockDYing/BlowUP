package com.doris.blowup;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.doris.blowup.sensor.AudioManagerSensor;
import com.doris.blowup.sensor.MySensor;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

//
// Created by dingying on 2020/1/8.
//
public class DandelionActivity extends Activity {

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
        findView();
//        if (recordThread == null || recordThread.getRecordStatus()) {
//            recordThread = new RecordThread(handler, 1);
//            recordThread.start();
//        }
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels;
        h = dm.heightPixels;
        blowSensor = AudioManagerSensor.getInstance(handler);
        blowSensor.start();
    }

    BlowHandler handler = new BlowHandler();

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

    private void findView(){
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
//            AnimationSet set = (AnimationSet) AnimationUtils.loadAnimation(
//                    DandelionActivity.this, R.anim.baiyue);
//            baiyunImage.setVisibility(View.VISIBLE);
//            baiyunImage.startAnimation(set);
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

    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO,}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
