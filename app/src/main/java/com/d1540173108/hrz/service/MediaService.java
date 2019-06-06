package com.d1540173108.hrz.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.bean.SaveMusicListBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.event.MediaSuccessInEvent;
import com.d1540173108.hrz.event.PlayMusicEndInEvent;
import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import java.io.IOException;
import java.util.List;

/**
 * 111
 * Created by NIWA on 2017/3/17.
 */

public class MediaService extends Service {

    private static final String TAG = "MediaService";
    private MyBinder mBinder = new MyBinder();

    private boolean isPrepared = false;//判断是否缓冲好
    public static int singlePlayType = -1;

    //初始化MediaPlayer
    public MediaPlayer mMediaPlayer = new MediaPlayer();
    private int playMode = 0;

    public MediaService() {
        iniMediaPlayerFile(MainActivity.mPos);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                LogUtils.e("onPrepared");
//                mBinder.playMusic();
                isPrepared = true;
//                LogUtils.e(mediaPlayer.getCurrentPosition(), TimeUtils.millis2String(mediaPlayer.getCurrentPosition() * 1000, new SimpleDateFormat("mm:ss")));
                EventBus.getDefault().post(new MediaSuccessInEvent());
            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.e("setOnCompletionListener");
                EventBus.getDefault().post(new PlayMusicEndInEvent());
                if (!mMediaPlayer.isPlaying() && singlePlayType == -1) {
                    mBinder.setPlayMode();
                }
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                LogUtils.e(mediaPlayer, i, i1);
                return true;
            }
        });
        return mBinder;
    }

    public class MyBinder extends Binder {

//        /**
//         *  获取MediaService.this（方便在ServiceConnection中）
//         *
//         * *//*
//        public MediaService getInstance() {
//            return MediaService.this;
//        }*/

        /**
         * 是否缓存好
         */
        public boolean isPrepared() {
            return isPrepared;
        }

        /**
         * 播放音乐
         */
        public void playMusic() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.start();
            }
        }

        //获取播放状态
        public boolean isPlaying() {
            if (mMediaPlayer == null) return false;
            return mMediaPlayer.isPlaying();
        }

        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.pause();
            }
        }

        /**
         * reset
         */
        public void resetMusic(String storyId) {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.reset();

                List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
                for (int i = 0; i < all.size(); i++) {
                    SaveMusicListBean bean = all.get(i);
                    if (storyId.equals(bean.getStoryId())) {
                        iniMediaPlayerFile(i);
                        break;
                    }
                }

            }
        }

        public void resetMusic2() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
//                mMediaPlayer.reset();
                iniMediaPlayerFile(MainActivity.mPos);
            }
        }

        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        /**
         * 指定播放
         */
        public void appointMusic(String storyId) {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
                for (int i = 0; i < all.size(); i++) {
                    SaveMusicListBean bean = all.get(i);
                    if (storyId.equals(bean.getStoryId())) {
                        iniMediaPlayerFile(i);
//                        mMediaPlayer.start();
                        break;
                    }
                }
            }
        }

        /**
         *  播放本地
         */
        public void playLocal(String url) {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                try {
                    LogUtils.e(url);
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    LogUtils.e(TAG, "设置资源，准备阶段出错");
                    e.printStackTrace();
                }
            }
        }
        /**
         *  播放本地带缓存成功回调
         */
        public void playLocal2(String url) {
            if (mMediaPlayer != null && !StringUtils.isEmpty(url)) {
                mMediaPlayer.reset();
                try {
                    LogUtils.e(url);
                    mMediaPlayer.setDataSource(url);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    LogUtils.e(TAG, "设置资源，准备阶段出错");
                    e.printStackTrace();
                }
            }
        }

        /**
         * 下一首
         */
        public void nextMusic() {
            if (mMediaPlayer != null && LitePal.findAll(SaveMusicListBean.class).size() != 0) {
                //切换歌曲reset()很重要很重要很重要，没有会报IllegalStateException
                if (MainActivity.mPos == LitePal.findAll(SaveMusicListBean.class).size() - 1) {
                    ToastUtils.showShort("已经是最后一首");
                    return;
                }
                MainActivity.mPos += 1;
                mMediaPlayer.reset();
                iniMediaPlayerFile(MainActivity.mPos);
//                playMusic();
            }
        }

        /**
         * 上一首
         */
        public void preciousMusic() {
            if (mMediaPlayer != null && LitePal.findAll(SaveMusicListBean.class).size() != 0) {
                if (MainActivity.mPos == 0){
                    ToastUtils.showShort("已经是第一首");
                    return;
                }
                MainActivity.mPos -= 1;
                mMediaPlayer.reset();
                iniMediaPlayerFile(MainActivity.mPos);
//                playMusic();
            }
        }

        public MediaPlayer.OnPreparedListener setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
            return onPreparedListener;
        }

        /**
         * 获取歌曲长度
         **/
        public int getProgress() {
            return mMediaPlayer.getDuration();
        }

        /**
         * 获取播放位置
         */
        public int getPlayPosition() {
            return mMediaPlayer.getCurrentPosition();
        }

        /**
         * 获取当前播放bean
         */
        public SaveMusicListBean getCurrentBean() {
            List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
            return all.get(MainActivity.mPos);
        }

        /**
         * 播放指定位置
         */
        public void seekToPositon(int msec) {
            mMediaPlayer.seekTo(msec);
        }

        private void setPlayMode() {
            if (playMode == 0)//全部循环
            {
                if (MainActivity.mPos == LitePal.findAll(SaveMusicListBean.class).size() - 1)//默认循环播放
                {
                    MainActivity.mPos = 0;// 第一首
                    mMediaPlayer.reset();
                    iniMediaPlayerFile(MainActivity.mPos);

                } else {
                    MainActivity.mPos++;
                    mMediaPlayer.reset();
                    iniMediaPlayerFile(MainActivity.mPos);
                }
            } else if (playMode == 1)//单曲循环
            {
                //MainActivity.mPos不需要更改
                mMediaPlayer.reset();
                iniMediaPlayerFile(MainActivity.mPos);
            } else if (playMode == 2)//随机
            {
                MainActivity.mPos = (int) (Math.random() * LitePal.findAll(SaveMusicListBean.class).size());//随机播放
                mMediaPlayer.reset();
                iniMediaPlayerFile(MainActivity.mPos);

            }
        }

        public void setPlayMode(int playMode1) {
            playMode = playMode1;
        }

        public int getPlayMode() {
            return playMode;
        }
    }

    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    private void iniMediaPlayerFile(int dex) {
        List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
        if (all.size() == 0) return;
        LogUtils.e(LitePal.findAll(SaveMusicListBean.class).get(dex).getUrlMp3());
        isPrepared = false;
        try {
            mMediaPlayer.setDataSource(CloudApi.IMAGE_SERVLET_URL + ":8080" + all.get(dex).getUrlMp3());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            LogUtils.e(TAG, "设置资源，准备阶段出错");
            e.printStackTrace();
        }
    }

}
