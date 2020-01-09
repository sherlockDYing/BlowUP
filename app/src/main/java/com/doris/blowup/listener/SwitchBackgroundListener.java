package com.doris.blowup.listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.doris.blowup.R;

//
// Created by dingying on 2020/1/9.
//
public class SwitchBackgroundListener implements GestureDetector.OnGestureListener {

    private ViewGroup background;

    private int flag = 3;

    private int stage = 0;

    private int[] bg = new int[5];

    private final static  int FOR_D = 0;
    private final static int FOR_W = 1;
    public SwitchBackgroundListener(ViewGroup background,int stage){
        Log.i("background","bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        this.background = background;
        if(stage == FOR_D){
           bg[0] = R.mipmap.bg1;
           bg[1] = R.mipmap.bg2;
           bg[2] = R.mipmap.bg3;
           bg[3] = R.mipmap.bg4;
           bg[4] = R.mipmap.bg5;
        }
        if(stage == FOR_W){
            bg[0] = R.mipmap.bg01;
            bg[1] = R.mipmap.bg02;
            bg[2] = R.mipmap.bg03;
            bg[3] = R.mipmap.bg04;
            bg[4] = R.mipmap.bg05;
        }
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("background","infling!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        // 处理左右滑动
        if (e1.getX() - e2.getX() > 100) { // 向左滑动
            if (flag == 3) {
                background.setBackgroundResource(bg[3]);
                flag = 4;
                return true;
            }
            if (flag == 4) {
                background.setBackgroundResource(bg[4]);
                flag = 5;
                return true;
            }
            if (flag == 1) {
                background.setBackgroundResource(bg[1]);
                flag = 2;
                return true;
            }
            if (flag == 2) {
                background.setBackgroundResource(bg[2]);
                flag = 3;
                return true;
            }
        } else if (e1.getX() - e2.getX() < -100) { // 向右滑动
            if (flag == 3) {
                background.setBackgroundResource(bg[1]);
                flag = 2;
                return true;
            }
            if (flag == 2) {
                background.setBackgroundResource(bg[0]);
                flag = 1;
                return true;
            }
            if (flag == 5) {
                background.setBackgroundResource(bg[3]);
                flag = 4;
                return true;
            }
            if (flag == 4) {
                background.setBackgroundResource(bg[2]);
                flag = 3;
                return true;
            }
        }
        return false;
    }

}
