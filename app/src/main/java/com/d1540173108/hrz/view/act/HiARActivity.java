package com.d1540173108.hrz.view.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.bean.BaseListBean;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.mar.MyApplication;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.utils.RandomUntil;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.d1540173108.hrz.view.HomeFrg;
import com.hiar.chimelong.BaseHiARActivity;
import com.hiar.chimelong.EnumSharePlatform;
import com.hiar.chimelong.WebActivity;
import com.lzy.okgo.model.Response;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class HiARActivity extends BaseHiARActivity {

    private static final String TAG = "HiARActivity";

    /**
     * 打开HiARActivity
     * @param context
     * @param uId           用户ID
     * @param sceneID       初始化场景ID 1是二维码，2是AR
     * @param lotteryUrl    抽奖地址
     */
    public static void open(Activity context, String uId, String sceneID, String lotteryUrl) {
        MainActivity.isArAct = true;
         Intent intent = new Intent(context, HiARActivity.class)
                .putExtra(HiARActivity.KEY_UID, uId)
                .putExtra(HiARActivity.KEY_SCENEID, sceneID)
                .putExtra(HiARActivity.KEY_LOTTERYURL, lotteryUrl);
        context.startActivityForResult(intent, 1);
    }

    /**
     * 首页返回键按钮事件回调
     */
    @Override
    public void onClickReturnHomeButton() {
//        LogUtils.e( "onClickReturnHomeButton");
        finish();
     }

    /**
     * 扫描到二维码回调
     * 一定要调用super.onQrCode(qrCode)
     * 一定要跳到新的Activity处理
     *
     * @param qrCode 识别到的二维码字符串
     */
    @Override
    public void onQrCode(String qrCode) {
        super.onQrCode(qrCode);
        LogUtils.e( "onQrCode:" + qrCode);
        if (qrCode.startsWith("chimelongAR:")){//走核销
            String[] split = qrCode.split("chimelongAR:");
            long mills = TimeUtils.getNowMills()/ 1000;//时间秒戳
            int v = RandomUntil.getRandomNum(String.valueOf(mills));//时间戳长度随机数
            String substring = String.valueOf(mills).substring(0, v);//时间戳截取0 - 刚刚获取的随机数
            //时间戳 + 上面那个 + 时间戳 + 上面那个
            String m = String.valueOf(mills) + substring + String.valueOf(mills) + substring;
            //小写md5两次
            String md5 = EncryptUtils.encryptMD5ToString(EncryptUtils.encryptMD5ToString(m.getBytes()).toLowerCase()).toLowerCase();
            LogUtils.e(mills, v, substring);
            String url = "https://ar.chimelong.com/lottery/myprize?photoID=" + split[1] + "&memberID=" +
                    ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId() + "&t=" + mills + "&n=" + v + "&s=" + md5;

            LogUtils.e(url);
//            WebActivity.open(this,url);
            UIHelper.startHtmlAct("领取奖品", url);
        }else {
//            WebActivity.open(this,"非正确核销二维码");
//            showToast("非正确核销二维码");
//            finish();
//            onResume();
//            onClickReturnHomeButton();

            UIHelper.startHtmlAct("二维码", qrCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    /**
     * 分享回调
     * 现在只回调见EnumSharePlatform.CUSTOM 自定义分享平台
     * @param sharePlatform 分享平台的枚举 见EnumSharePlatform
     * @param shareImagePath 分享的图片本地路径
     */
    @Override
    public void onShare(int sharePlatform, final String shareImagePath) {
        LogUtils.e( "onShare:" + sharePlatform + ",shareImagePath:" + shareImagePath);
        switch (sharePlatform){
            case EnumSharePlatform.QQ://QQ
                shareImageLocal(SHARE_MEDIA.QQ, shareImagePath);
                break;
            case EnumSharePlatform.QZONE://QQ空间
                shareImageLocal(SHARE_MEDIA.QZONE, shareImagePath);
                break;
            case EnumSharePlatform.WECHAT://微信
                shareImageLocal(SHARE_MEDIA.WEIXIN, shareImagePath);
                break;
            case EnumSharePlatform.WECHATMOMENTS://朋友圈
                shareImageLocal(SHARE_MEDIA.WEIXIN_CIRCLE, shareImagePath);
                break;
            case EnumSharePlatform.SINAWEIBO://微博
                shareImageLocal(SHARE_MEDIA.SINA, shareImagePath);
                break;
        }
    }

    /**
     * 听故事回调
     * @param TargetId id
     */
    @Override
    public void onCLickListenStory(final String TargetId) {
        LogUtils.e( "onCLickListenStory:" + TargetId);
//        showToast("听故事："+ TargetId);
        CloudApi.list(1, CloudApi.storyGetStoryList)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showToast("正在跳转");
                     }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                final List<DataBean> list = data.getRows();
                                if (list != null && list.size() != 0){
                                    for (int i = 0;i < list.size();i++){
                                        DataBean bean = list.get(i);
                                        if (bean.getStoryId().equals(TargetId)){
                                            final int finalI = i;
                                            if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                                                PopupWindowTool.showDialog(HiARActivity.this, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                                                    @Override
                                                    public void onClick() {
                                                        MainActivity.isWifi = true;
                                                        UIHelper.startPlayMusicAct(list, finalI);
                                                        MainActivity.mPos = finalI;                                                    }
                                                });
                                            }else {
                                                UIHelper.startPlayMusicAct(list, finalI);
                                                MainActivity.mPos = finalI;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 打开WebViewActivity回调
     * @param webType 1：红包规则
     */
    @Override
    public void onOpenWebView(int webType) {
//        showToast("onOpenWebView："+ webType);
        LogUtils.e(webType);
        switch (webType){
            case 1://红包规则
                WebActivity.open(this,"https://ar.chimelong.com/info/rules");
                break;
        }
    }

    private void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(HiARActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void shareImageLocal(SHARE_MEDIA media, String shareImagePath){
        UMImage imagelocal = new UMImage(this, new File(shareImagePath));
        imagelocal.setThumb(new UMImage(this, new File(shareImagePath)));
        new ShareAction(HiARActivity.this).withMedia(imagelocal )
                .setPlatform(media)
                .withText(getString(R.string.share_title))
                .setCallback(shareListener).share();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("成功了");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtils.e("失败", t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast("取消了");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (MainActivity.mMyBinder != null){
//            MainActivity.mMyBinder.pauseMusic();
//        }

    }

}
