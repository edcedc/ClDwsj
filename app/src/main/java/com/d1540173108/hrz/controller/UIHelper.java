package com.d1540173108.hrz.controller;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.event.PhoneListenInEvent;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.d1540173108.hrz.view.KnowledgeFrg;
import com.d1540173108.hrz.view.KnowledgeImageFrg;
import com.d1540173108.hrz.view.act.CeShiAct;
import com.d1540173108.hrz.view.act.HiARActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.view.AboutUsFrg;
import com.d1540173108.hrz.view.ErrorCorrectionFrg;
import com.d1540173108.hrz.view.FeedbackFrg;
import com.d1540173108.hrz.view.IdentifyingDetailsFrg;
import com.d1540173108.hrz.view.IdentifyingFrg;
import com.d1540173108.hrz.view.IdentifyingResultFrg;
import com.d1540173108.hrz.view.MainFrg;
import com.d1540173108.hrz.view.MsgDescFrg;
import com.d1540173108.hrz.view.MsgFrg;
import com.d1540173108.hrz.view.MyCollectionFrg;
import com.d1540173108.hrz.view.MyDownloadFrg;
import com.d1540173108.hrz.view.MyInfoFrg;
import com.d1540173108.hrz.view.ShareFrg;
import com.d1540173108.hrz.view.act.PlayMusicAct;
import com.d1540173108.hrz.view.RecentlyBroadcastFrg;
import com.d1540173108.hrz.view.StoryListFrg;
import com.d1540173108.hrz.view.UpdateNicknameFrg;
import com.d1540173108.hrz.view.act.AnimalDescAct;
import com.d1540173108.hrz.view.act.AnimalEncyclopediaAct;
import com.d1540173108.hrz.view.act.CLAnimalAct;
import com.d1540173108.hrz.view.act.HtmlAct;
import com.d1540173108.hrz.view.act.LoginAct;
import com.d1540173108.hrz.view.act.VideoAct;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/22.
 */

public final class UIHelper {

    private UIHelper() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void startMainAct() {
        ActivityUtils.startActivity(MainActivity.class);
    }

    public static void startLoginAct() {
        ActivityUtils.startActivity(LoginAct.class);
    }

    /**
     * 二维码扫描
     */
    public static void startZxingAct(final Activity act) {
//        ActivityUtils.startActivity(WeChatCaptureActivity.class);
//        AndPermission.with(act)
//                .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
//                .onGranted(new Action() {
//                    @Override
//                    public void onAction(Object data) {
//
//                        Intent intent = new Intent(act, CaptureActivity.class);
//                              /*  ZxingConfig是配置类
//                                 *可以设置是否显示底部布局，闪光灯，相册，
//                                 * 是否播放提示音  震动
//                                 * 设置扫描框颜色等
//                                 * 也可以不传这个参数
//                                 * */
//                        ZxingConfig config = new ZxingConfig();
//                        // config.setPlayBeep(false);//是否播放扫描声音 默认为true
//                        //  config.setShake(false);//是否震动  默认为true
//                        // config.setDecodeBarCode(false);//是否扫描条形码 默认为true
////                                config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色 默认为白色
////                                config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色 默认无色
////                                config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色 默认白色
//                        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
//                        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//                        act.startActivityForResult(intent, MainActivity.REQUEST_CODE_SCAN);
//                    }
//                })
//                .onDenied(new Action() {
//                    @Override
//                    public void onAction(Object data) {
//                        Uri packageURI = Uri.parse("package:" + getPackageName());
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        act.startActivity(intent);
//                        Toast.makeText(act, "没有权限无法扫描呦", Toast.LENGTH_LONG).show();
//                    }
//
//                }).start();
    }

    /**
     * 动物百科
     */
    public static void startAnimalEncyclopediaAct() {
        ActivityUtils.startActivity(AnimalEncyclopediaAct.class);
    }

    /**
     * 动物详情
     * @param id
     */
    public static void startAnimalDescAct(String id) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        ActivityUtils.startActivity(bundle, AnimalDescAct.class);
    }
    public static void startAnimalDescAct(String id, String path) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("path", path);
        ActivityUtils.startActivity(bundle, AnimalDescAct.class);
    }

    /**
     * Html
     */
    public static void startHtmlAct(String title, String url, int type) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        bundle.putInt("type", type);
        ActivityUtils.startActivity(bundle, HtmlAct.class);
    }
    public static void startHtmlAct(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        ActivityUtils.startActivity(bundle, HtmlAct.class);
    }

    /**
     * 播放视频
     * @param image
     */
    public static void startVideoAct(String image) {
        Bundle bundle = new Bundle();
        bundle.putString("url",image);
        ActivityUtils.startActivity(bundle, VideoAct.class);
    }


    /**
     * 提交反馈
     */
    public static void startFeedbackFrg(BaseFragment root, int type) {
        FeedbackFrg frg = new FeedbackFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        if (type == 1) {
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        } else {
            root.start(frg);
        }
    }

    /**
     * 我的信息
     */
    public static void startMyInfoFrg(BaseFragment root) {
        MyInfoFrg frg = new MyInfoFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 修改昵称
     *
     * @param root
     */
    public static void startUpdateNicknameFrg(BaseFragment root) {
        UpdateNicknameFrg frg = new UpdateNicknameFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 我的下载
     *
     * @param root
     */
    public static void startMyDownloadFrg(BaseFragment root) {
        MyDownloadFrg frg = new MyDownloadFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 我收藏的故事
     * @param root
     */
    public static void startMyCollectionFrg(BaseFragment root) {
        MyCollectionFrg frg = new MyCollectionFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 最近播放
     */
    public static void startRecentlyBroadcastFrg(BaseFragment root) {
        RecentlyBroadcastFrg frg = new RecentlyBroadcastFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 关于神奇动物
     * @param root
     */
    public static void startAboutUsFrg(BaseFragment root) {
        AboutUsFrg frg = new AboutUsFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 消息中心
     */
    public static void startMsgFrg(BaseFragment root) {
        MsgFrg frg = new MsgFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 故事列表
     * @param root
     * @param name
     */
    public static void startStoryListFrg(BaseFragment root, String name, int position) {
        StoryListFrg frg = new StoryListFrg();
        Bundle bundle = new Bundle();
        bundle.putString("title", name);
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     * 识别细节
     */
    public static void startIdentifyingDetailsFrg(BaseFragment root, String result, String path) {
        IdentifyingDetailsFrg frg = new IdentifyingDetailsFrg();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 识别结果
     */
    public static void startIdentifyingResultFrg(BaseFragment root, String result, String path) {
        IdentifyingResultFrg frg = new IdentifyingResultFrg();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 纠错
     */
    public static void startErrorCorrectionFrg(BaseFragment root, String path) {
        ErrorCorrectionFrg frg = new ErrorCorrectionFrg();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 识别
     */
    public static void startIdentifyingFrg(BaseFragment root, String path) {
        IdentifyingFrg frg = new IdentifyingFrg();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 长隆动物
     */
    public static void startCLAnimalAct() {
        ActivityUtils.startActivity(CLAnimalAct.class);
    }

    /**
     * 播放器页面
     */
    public static void startPlayMusicAct(List<DataBean> listBean, int position) {
        if (position == -1) return;
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        String json = new Gson().toJson(listBean, type);
        Bundle bundle = new Bundle();
        bundle.putString("list", json);
        bundle.putInt("position", position);
        ActivityUtils.startActivity(bundle, PlayMusicAct.class);
    }

    /**
     * 消息详情
     * @param root
     * @param bean
     */
    public static void startMsgDescFrg(BaseFragment root, DataBean bean) {
        MsgDescFrg frg = new MsgDescFrg();
        Bundle bundle = new Bundle();
        bundle.putString("bean", new Gson().toJson(bean));
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * 分享
     * @param root
     * @param path
     */
    public static void startShareFrg(BaseFragment root, JSONObject data, String path) {
        ShareFrg frg = new ShareFrg();
        Bundle bundle = new Bundle();
        bundle.putString("data", data.toString());
        bundle.putString("path", path);
        frg.setArguments(bundle);
        root.start(frg);
    }

    /**
     * ar
     * @param act
     */
    public static void startArAct(Activity act, int sceneID) {
        if (User.getInstance().isLogin()){
            EventBus.getDefault().post(new PhoneListenInEvent(true));
            HiARActivity.open(act, ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId(),sceneID + "","https://ar.chimelong.com/lottery/draw");
        }else {
            UIHelper.startLoginAct();
        }
    }

    public static void startCeShiAct() {
        ActivityUtils.startActivity(CeShiAct.class);
    }

    /**
     *  1.1.1小知识列表
     * @param root
     */
    public static void startKnowledgeFrg(BaseFragment root) {
        KnowledgeFrg frg = new KnowledgeFrg();
        Bundle bundle = new Bundle();
        frg.setArguments(bundle);
        ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
    }

    /**
     *  1.1.1小知识列表 图片
     * @param root
     */
    public static void startKnowledgeImageFrg(BaseFragment root, String title, String id, int type) {
        KnowledgeImageFrg frg = new KnowledgeImageFrg();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("id", id);
        frg.setArguments(bundle);
        if (type == 0){
            ((MainFrg) root.getParentFragment()).startBrotherFragment(frg);
        }else {
            root.start(frg);
        }
    }
}