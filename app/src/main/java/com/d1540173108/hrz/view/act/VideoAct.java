package com.d1540173108.hrz.view.act;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.utils.ShareTool;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.databinding.AVideoBinding;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * Created by edison on 2019/1/13.
 *  视频
 */

public class VideoAct extends BaseActivity<BasePresenter, AVideoBinding>{

    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private GSYVideoOptionBuilder gsyVideoOption;
    private String url;

    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.a_video;
    }

    @Override
    protected void initParms(Bundle bundle) {
        url = bundle.getString("url");
    }

    @Override
    protected void initView() {
        setSofia(true);
        mB.fyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(act, mB.videoPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        ImageView imageView = new ImageView(act);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        GlideLoadingUtils.load(act, url, imageView);
        gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setLooping(false)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl(url)
                .setCacheWithPlay(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        LogUtils.e("onPrepared", objects[0], objects[1]);
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        LogUtils.e("onEnterFullscreen", objects[0], objects[1]);
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        LogUtils.e("onAutoComplete", url);

                    }

                    @Override
                    public void onClickStartError(String url, Object... objects) {
                        super.onClickStartError(url, objects);
                        LogUtils.e("onClickStartError", url);
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        LogUtils.e("onQuitFullscreen", objects[0], objects[1]);
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                })
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
//                        LogUtils.e(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(mB.videoPlayer);
        mB.videoPlayer.startPlayLogic();
        mB.videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                mB.videoPlayer.startWindowFullscreen(act, true, true);
            }
        });

        mB.fyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.finish();
            }
        });
    }

    @Override
    public void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        ShareTool.getInstance().release(act);
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null){
            orientationUtils.releaseListener();
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (mB.videoPlayer.getFullWindowPlayer() != null) {
            return mB.videoPlayer.getFullWindowPlayer();
        }
        return mB.videoPlayer;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            mB.videoPlayer.onConfigurationChanged(act, newConfig, orientationUtils, true, true);
        }
    }
}
