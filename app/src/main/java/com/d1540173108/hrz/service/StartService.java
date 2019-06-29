package com.d1540173108.hrz.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.event.PhoneListenInEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edison on 2019/3/7.
 */

public class StartService extends Service {
    private AudioManager mAm;
    private MyOnAudioFocusChangeListener mListener;

    public class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        @Override
        public void onAudioFocusChange(int focusChange) {
            LogUtils.e(focusChange);
            if (focusChange == -1) {
                EventBus.getDefault().post(new PhoneListenInEvent(true));
            }
        }
    }

    public boolean requestFocus() {
        if(mListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAm.requestAudioFocus(mListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN);
        }
        return false;
    }

    public boolean abandonFocus() {
        if(mListener != null) {
            return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                    mAm.abandonAudioFocus(mListener);
        }
        return false;
    }

    @Override
    public void onCreate() {
        mAm = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mListener = new MyOnAudioFocusChangeListener();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        // Request audio focus for playback
        int result = mAm.requestAudioFocus(mListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        boolean vIsActive = mAm.isMusicActive();
//        LogUtils.e(vIsActive);

//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//            LogUtils.e("requestAudioFocus successfully.");
//        } else {
//            LogUtils.e("requestAudioFocus failed.");
//        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mAm.abandonAudioFocus(mListener);
    }

}
