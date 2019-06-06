package com.d1540173108.hrz.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.DownloadBean;
import com.d1540173108.hrz.bean.SaveMusicListBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.event.AppointMusicInEvent;
import com.d1540173108.hrz.event.LoginSuccessInEvent;
import com.d1540173108.hrz.event.PhoneListenInEvent;
import com.d1540173108.hrz.event.PlayMusicEndInEvent;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.StoryAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FHomeBinding;
import com.d1540173108.hrz.presenter.HomePresenter;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.view.bottomFrg.MusicListBottomFrg;
import com.d1540173108.hrz.view.impl.HomeContarct;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by edison on 2019/1/10.
 *  首页
 */

public class HomeFrg extends BaseFragment<HomePresenter, FHomeBinding> implements HomeContarct.View, View.OnClickListener{

    public static HomeFrg newInstance() {
        Bundle args = new Bundle();
        HomeFrg fragment = new HomeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private StoryAdapter adapter;
    private MusicListBottomFrg musicListBottomFrg;

    private int ar_red = 0;//0AR 1红包



    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_home;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        setSofia(true);
        mB.ivAchievement.setOnClickListener(this);
        mB.ivMsg.setOnClickListener(this);
        mB.ivVisit.setOnClickListener(this);
        mB.ivImg.setOnClickListener(this);
        mB.fyMagicalAnimals.setOnClickListener(this);
        mB.fyRedEnvelopes.setOnClickListener(this);
        mB.ivRed.setOnClickListener(this);
        mB.ivPlay.setOnClickListener(this);
        mB.ivPlayList.setOnClickListener(this);
        mB.lyPlay.setOnClickListener(this);
        mB.ivArImg.setOnClickListener(this);
        mB.tvMore.setOnClickListener(this);

        if (adapter == null){
            adapter = new StoryAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(mB
                .recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setNestedScrollingEnabled(false);
        mB.recyclerView.setAdapter(adapter);

        showLoadDataing();
//        mB.refreshLayout.startRefresh();
        mPresenter.onSleep(HomeFrg.this, mB.gridView);
        mPresenter.onRequest();
        mB.refreshLayout.setPureScrollModeOn();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {

                mPresenter.onSleep(HomeFrg.this, mB.gridView);
                mPresenter.onRequest();
            }
        });

        EventBus.getDefault().register(this);
        //后台说固定写死
        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL +  "/zoor/story/showPicBannerAR?id=1" + "&" + (float) (Math.random() * 100), mB.ivArImg);


    }

    @Subscribe
    public void onMainThreadInEvent(PhoneListenInEvent event){
        if (MainActivity.mMyBinder != null){
            if (event.isListen){
                mB.ivPlay.setBackgroundResource(R.mipmap.icon_bofang);
                MainActivity.mMyBinder.pauseMusic();
            }
        }
    }

    @Subscribe
    public void onMainLoginSuccessInEvent(LoginSuccessInEvent event){
        mB.tvName.setText(event.name);
        GlideLoadingUtils.load(act, event.head, mB.ivAchievement);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (MainActivity.mMyBinder != null && MainActivity.mMyBinder.isPlaying() && MainActivity.mPos != -1 && !MainActivity.isDow){
            SaveMusicListBean bean = MainActivity.mMyBinder.getCurrentBean();
            GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + bean.getUrlPic(), mB.ivPlayImg);
            mB.tvPlayName.setText(bean.getStoryName());
            mB.lyPlay.setVisibility(View.VISIBLE);
            mB.ivPlay.setBackgroundResource(R.mipmap.y36);
        }else if (MainActivity.mMyBinder != null && MainActivity.mMyBinder.isPlaying() && MainActivity.isDow){
            DownloadBean bean = LitePal.findAll(DownloadBean.class).get(MainActivity.mPos);
            mB.tvPlayName.setText(bean.getStoryName());
        }else {
            mB.ivPlay.setBackgroundResource(R.mipmap.icon_bofang);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainAppointMusicInEvent(AppointMusicInEvent event) {
        SaveMusicListBean bean = LitePal.findAll(SaveMusicListBean.class).get(event.position);
        MainActivity.mPos = event.position;
        MainActivity.mMyBinder.appointMusic(bean.getStoryId());
        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + bean.getUrlPic(), mB.ivPlayImg);
        mB.tvPlayName.setText(bean.getStoryName());
        mB.lyPlay.setVisibility(View.VISIBLE);
        mB.ivPlay.setBackgroundResource(R.mipmap.y36);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainPlayMusicEndInEvent(PlayMusicEndInEvent event) {
         mB.lyPlay.setVisibility(View.GONE);
    }

    @Override
    public void setData(List<DataBean> list) {
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_msg://消息中心
                UIHelper.startMsgFrg(this);
                break;
            case R.id.iv_visit://扫一扫
//                UIHelper.startZxingAct(act);
                UIHelper.startArAct(act, 1);
                break;
            case R.id.iv_img://小知识
                UIHelper.startHtmlAct("小知识", "http://chimelong.peanuts.cc/index1.html");
                break;
            case R.id.fy_magical_animals://神奇动物
                ar_red = 0;
                mB.lyArRed.setBackgroundResource(R.mipmap.qiehuan1);
                GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL +  "/zoor/story/showPicBannerAR?id=1" + "&" + (float) (Math.random() * 100), mB.ivArImg);
                break;
            case R.id.fy_red_envelopes://领取红包
                ar_red = 1;
                mB.lyArRed.setBackgroundResource(R.mipmap.qiehuan2);
                GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL +  "/zoor/story/showPicBannerHB?id=1" + "&" + (float) (Math.random() * 100), mB.ivArImg);
                break;
            case R.id.iv_ar_img:
                if (ar_red == 0) {
//                    UIHelper.startHtmlAct("我是AR");
                    UIHelper.startArAct(act, 2);
                }else {
//                    UIHelper.startHtmlAct("领取红包", null);
                    UIHelper.startArAct(act, 2);
                }
                break;
            case R.id.iv_red:
//                UIHelper.startHtmlAct("领取红包", null);
                UIHelper.startArAct(act, 2);
                break;
            case R.id.iv_play:
                if (MainActivity.mMyBinder != null && MainActivity.mMyBinder.isPlaying()){
                    MainActivity.mMyBinder.pauseMusic();
                    mB.ivPlay.setBackgroundResource(R.mipmap.icon_bofang);
                }else {
                    if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                        PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                            @Override
                            public void onClick() {
                                MainActivity.isWifi = true;
                                MainActivity.isOnePlay = true;
                                MainActivity.mMyBinder.playMusic();
                                mB.ivPlay.setBackgroundResource(R.mipmap.y36);
                            }
                        });
                    }else {
                        MainActivity.isOnePlay = true;
                        MainActivity.mMyBinder.playMusic();
                        mB.ivPlay.setBackgroundResource(R.mipmap.y36);
                    }
                }
                break;
            case R.id.iv_play_list:
//                if (musicListBottomFrg == null){
//                    musicListBottomFrg = new MusicListBottomFrg();
//                }
//                musicListBottomFrg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.ly_play:
                if (!MainActivity.isDow){
                    UIHelper.startPlayMusicAct(null, MainActivity.mPos);
                }
                break;
            case R.id.iv_achievement:
                if (!User.getInstance().isLogin()){
                    UIHelper.startLoginAct();
                    return;
                }
                UIHelper.startMyInfoFrg(this);
                break;
            case R.id.tv_more:
                UIHelper.startStoryListFrg(this, "故事", 0);
                break;
        }
    }

}
