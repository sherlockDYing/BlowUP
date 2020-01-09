package com.doris.blowup.sensor;

//
// Created by dingying on 2020/1/8.
//
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;

import com.doris.blowup.WindmillActivity;


public class AudioManagerSensor extends MySensor {
    private static AudioManagerSensor mAudioSensor = null;
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;//是否正在录音
    private Handler mHandler;
    private AudioManagerSensor(Handler handler) {
        super();
        this.mHandler = handler;
        isGetVoiceRun = false;
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
    }

    public static AudioManagerSensor getInstance(Handler handler) {
        if (mAudioSensor == null) {
            synchronized (AudioManagerSensor.class) {
                if (mAudioSensor == null) {
                    mAudioSensor = new AudioManagerSensor(handler);
                }
            }
        }
        return mAudioSensor;
    }

    @Override
    public void start() {
        if (isGetVoiceRun) {
            return;
        }
        if (mAudioRecord == null) {
            throw new NullPointerException("mAudioRecord初始化失败！");
        }
        isGetVoiceRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAudioRecord.startRecording();
                short[] buffer = new short[BUFFER_SIZE];
                while (isGetVoiceRun) {
                    int record = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                    long v = 0;
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    double mean = v / (double) record;
                    double volume = 10 * Math.log10(mean);
                    if (volume > 65) {
                        mHandler.sendEmptyMessage(WindmillActivity.BLOW_START);
                    }

                    synchronized (mLock) {
                        try {
                            mLock.wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (mAudioRecord != null) {
                    mAudioRecord.stop();

                }
            }
        }).start();
    }

    @Override
    public void shutDown() {
        isGetVoiceRun = false;
    }

    @Override
    public void destory() {
        if (mAudioRecord != null) {
            mAudioRecord.release();
            mAudioRecord = null;
        }
        mAudioSensor = null;
    }
}
