package com.d1540173108.hrz.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.MyPagerAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.Common;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.Music;
import com.d1540173108.hrz.bean.SaveMusicListBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FPlayMusicBinding;
import com.d1540173108.hrz.event.AppointMusicInEvent;
import com.d1540173108.hrz.event.LrcViewInEvent;
import com.d1540173108.hrz.event.MediaServiceInEvent;
import com.d1540173108.hrz.event.MediaSuccessInEvent;
import com.d1540173108.hrz.event.PhoneListenInEvent;
import com.d1540173108.hrz.presenter.PlayMusicPresenter;
import com.d1540173108.hrz.service.MediaService;
import com.d1540173108.hrz.utils.FileSaveUtils;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.utils.ShareTool;
import com.d1540173108.hrz.view.bottomFrg.MusicListBottomFrg;
import com.d1540173108.hrz.view.impl.PlayMusicContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/14.
 * 播放音乐
 */

public class PlayMusicFrg extends BaseFragment<PlayMusicPresenter, FPlayMusicBinding> implements PlayMusicContract.View, View.OnClickListener {

    private String storyId;
    private PlayMusicImgFrg musicImgFrg;
    private PlayMusicLyricFrg lyricFrg;
    private ShareAction shareAction;

    public static PlayMusicFrg newInstance() {
        Bundle args = new Bundle();
        PlayMusicFrg fragment = new PlayMusicFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");
    private List<DataBean> listBean;
    private int position;
    private MusicListBottomFrg musicListBottomFrg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        listBean = new Gson().fromJson(bundle.getString("list"), type);
        position = bundle.getInt("position");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_play_music;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        setSofia(true);
        MainActivity.isDow = false;
        mB.fyCollection.setOnClickListener(this);
        mB.ivShare.setOnClickListener(this);
        mB.fyPlayType.setOnClickListener(this);
        mB.ivShang.setOnClickListener(this);
        mB.ivXia.setOnClickListener(this);
        mB.ivList.setOnClickListener(this);
        mB.fyPlay.setOnClickListener(this);
        mB.fyClose.setOnClickListener(this);
        mB.ivDownload.setOnClickListener(this);
        mPresenter.onSetData(listBean, position);
        EventBus.getDefault().register(this);

        int screenWidth = ScreenUtils.getScreenWidth();
        ViewGroup.LayoutParams params = mB.viewPager.getLayoutParams();
        params.height = screenWidth;
        params.width = screenWidth;
        mB.viewPager.setLayoutParams(params);

        mB.musicSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                if (fromUser) {
                    MainActivity.mMyBinder.seekToPositon(seekBar.getProgress());
//                    mMediaService.mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                EventBus.getDefault().post(new LrcViewInEvent(seekBar.getProgress()));
            }
        });
        mHandler.post(mRunnable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainMediaInEvent(MediaSuccessInEvent event) {
        LogUtils.e("MediaSuccessInEvent");
        mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
        if (!MainActivity.mMyBinder.isPlaying()) {
            MainActivity.mMyBinder.playMusic();
            mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
        }
        mB.tvEndTime.setText(time.format(MainActivity.mMyBinder.getProgress()));
        setMusicData(MainActivity.mPos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainAppointMusicInEvent(AppointMusicInEvent event) {
        SaveMusicListBean bean = LitePal.findAll(SaveMusicListBean.class).get(event.position);
        MainActivity.mMyBinder.appointMusic(bean.getStoryId());
        mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
        mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
        MainActivity.mPos = event.position;
        setMusicData(MainActivity.mPos);
        mB.tvEndTime.setText(time.format(MainActivity.mMyBinder.getProgress()));
    }

    @Subscribe
    public void onMainThreadInEvent(PhoneListenInEvent event){
        if (MainActivity.mMyBinder != null){
            if (event.isListen){
                mB.ivPlay.setBackgroundResource(R.mipmap.y31);
                MainActivity.mMyBinder.pauseMusic();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //我们的handler发送是定时1000s发送的，如果不关闭，MediaPlayer release掉了还在获取getCurrentPosition就会爆IllegalStateException错误
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(mRunnable);
        ShareTool.getInstance().release(act);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fy_close:
                act.finish();
                break;
            case R.id.fy_collection://收藏
                mPresenter.onColletion(storyId, 1, isColletion);
                break;
            case R.id.fy_play:
                if (MainActivity.mMyBinder.isPlaying()) {
                    MainActivity.mMyBinder.pauseMusic();
                    mB.ivPlay.setBackgroundResource(R.mipmap.y31);
                } else {
                    if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                        PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                            @Override
                            public void onClick() {
                                MainActivity.isWifi = true;
                                MainActivity.mMyBinder.playMusic();
                                MainActivity.isOnePlay = true;
                                mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
                                mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
                            }
                        });
                    }else {
                        MainActivity.mMyBinder.playMusic();
                        MainActivity.isOnePlay = true;
                        mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
                        mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
                    }
                }
                break;
            case R.id.iv_shang:
                if (MainActivity.mPos == 0) {
                    showToast("已经是第一首");
                    return;
                }
                MainActivity.mMyBinder.pauseMusic();
                MainActivity.mMyBinder.preciousMusic();
                setMusicData(MainActivity.mPos);
                break;
            case R.id.iv_xia:
                MainActivity.mMyBinder.pauseMusic();
                MainActivity.mMyBinder.nextMusic();
                mB.ivPlay.setBackgroundResource(R.mipmap.y31);
                setMusicData(MainActivity.mPos);
                break;
            case R.id.iv_list:
                if (musicListBottomFrg == null) {
                    musicListBottomFrg = new MusicListBottomFrg();
                }
                Type type = new TypeToken<ArrayList<DataBean>>() {
                }.getType();
                String json = new Gson().toJson(listBean, type);
                Bundle bundle = new Bundle();
                bundle.putString("list", json);
                musicListBottomFrg.setArguments(bundle);
                musicListBottomFrg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.iv_download:
                if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                    PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                        @Override
                        public void onClick() {
                            MainActivity.isWifi = true;
                            mPresenter.onDow(listBean);
                        }
                    });
                }else {
                    mPresenter.onDow(listBean);
                }
                break;
            case R.id.iv_share:
                if (!User.getInstance().isLogin()) {
                    UIHelper.startLoginAct();
                    return;
                }
                shareAction.open();
                break;
            case R.id.fy_play_type:
                if (MainActivity.mMyBinder.getPlayMode() == 0) {
                    MainActivity.mMyBinder.setPlayMode(1);
                    mB.ivPlayType.setBackgroundResource(R.mipmap.icon_xh);
                    showToast("单曲循环");
                } else if (MainActivity.mMyBinder.getPlayMode() == 1) {
                    MainActivity.mMyBinder.setPlayMode(2);
                    mB.ivPlayType.setBackgroundResource(R.mipmap.icon_sjbf);
                    showToast("随机播放");
                } else {
                    MainActivity.mMyBinder.setPlayMode(0);
                    mB.ivPlayType.setBackgroundResource(R.mipmap.icon_sx);
                    showToast("列表循环");
                }
                break;
        }
    }

    /**
     * 更新ui的runnable
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
//            LogUtils.e(MainActivity.mMyBinder.isPlaying(), time.format(MainActivity.mMyBinder.getPlayPosition()));
            if (MainActivity.mMyBinder.isPlaying()) {
                mB.musicSeekbar.setProgress(MainActivity.mMyBinder.getPlayPosition());
                mB.tvStartTime.setText(time.format(MainActivity.mMyBinder.getPlayPosition()));
                EventBus.getDefault().post(new LrcViewInEvent(MainActivity.mMyBinder.getPlayPosition()));
//                mHandler.sendEmptyMessage(1);
            } else {
//                mHandler.sendEmptyMessage(0);
            }
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showLoading();
                    break;
                case 1:
                    hideLoading();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onData() {
        List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
        SaveMusicListBean bean = all.get(position);
        mB.tvTitle.setText(bean.getStoryName());
        storyId = bean.getStoryId();
        mPresenter.onIsColletion(storyId);
        mPresenter.onSavePlayNum(storyId, 0);
//        shareAction = ShareTool.getInstance().shareAction(act, "http://chimelong.peanuts.cc/?ch=25");
        shareAction = ShareTool.getInstance().shareAction(act, "http://chimelong.peanuts.cc/music_h5/index.html?type=" + storyId);

        if (MainActivity.mMyBinder.isPlaying()) {
            if (MainActivity.mPos == position) {//记录播放索引值

            } else {
                MainActivity.mMyBinder.appointMusic(storyId);
            }
            mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
            mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
        } else {
            MainActivity.isOnePlay = true;
            MainActivity.mMyBinder.resetMusic(storyId);
            mB.musicSeekbar.setMax(MainActivity.mMyBinder.getProgress());
            mB.ivPlay.setBackgroundResource(R.mipmap.icon_tz);
        }
        MainActivity.mPos = position;
        mB.tvEndTime.setText(time.format(MainActivity.mMyBinder.getProgress()));
        LogUtils.e(time.format(MainActivity.mMyBinder.getProgress()), MainActivity.mMyBinder.getProgress());
        musicImgFrg = new PlayMusicImgFrg();
        lyricFrg = new PlayMusicLyricFrg();
        Bundle bundle = new Bundle();
        bundle.putString("image", bean.getUrlPic());
        bundle.putString("musicTips", bean.getStoryId());
        musicImgFrg.setArguments(bundle);
        lyricFrg.setArguments(bundle);
        mFragments.add(musicImgFrg);
        mFragments.add(lyricFrg);
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments));
        mB.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mB.ivDot1.setBackgroundResource(R.mipmap.icon_fy);
                    mB.ivDot2.setBackgroundResource(R.mipmap.icon_fy1);
                } else {
                    mB.ivDot1.setBackgroundResource(R.mipmap.icon_fy1);
                    mB.ivDot2.setBackgroundResource(R.mipmap.icon_fy);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mB.viewPager.setCurrentItem(0);
        mB.viewPager.setOffscreenPageLimit(2);

        LogUtils.e(MainActivity.mPos);
    }

    private boolean isColletion = false;

    @Override
    public void onIsColletion(boolean b) {
        this.isColletion = b;
        mB.cbCollection.setBackgroundResource(b == true ? R.mipmap.icon_sc1 : R.mipmap.icon_sc0);
    }

    private void setMusicData(int pos) {
        LogUtils.e(time.format(MainActivity.mMyBinder.getProgress()));
        mB.tvEndTime.setText(time.format(MainActivity.mMyBinder.getProgress()));
//        time.format(MainActivity.mMyBinder.getProgress());
        storyId = LitePal.findAll(SaveMusicListBean.class).get(pos).getStoryId();
//        shareAction = ShareTool.getInstance().shareAction(act, "http://chimelong.peanuts.cc/?ch=25");
        shareAction = ShareTool.getInstance().shareAction(act, "http://chimelong.peanuts.cc/music_h5/index.html?type=" + storyId);
        musicImgFrg.setImage(LitePal.findAll(SaveMusicListBean.class).get(pos).getUrlPic());
        lyricFrg.setMusicTips(LitePal.findAll(SaveMusicListBean.class).get(pos).getStoryId());
        mB.tvTitle.setText(LitePal.findAll(SaveMusicListBean.class).get(pos).getStoryName());
    }

}