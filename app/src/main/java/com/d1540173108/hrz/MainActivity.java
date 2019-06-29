package com.d1540173108.hrz;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.bean.Common;
import com.d1540173108.hrz.bean.Music;
import com.d1540173108.hrz.event.CameraInEvent;
import com.d1540173108.hrz.event.MediaServiceInEvent;
import com.d1540173108.hrz.event.MediaSuccessInEvent;
import com.d1540173108.hrz.event.PhoneListenInEvent;
import com.d1540173108.hrz.service.GtIntentService;
import com.d1540173108.hrz.service.GtPushService;
import com.d1540173108.hrz.service.MediaService;
import com.d1540173108.hrz.service.MusicBroadcastReceiver;
import com.d1540173108.hrz.service.PhoneListenService;
import com.d1540173108.hrz.service.StartService;
import com.d1540173108.hrz.utils.RandomUntil;
import com.d1540173108.hrz.utils.cache.ShareIsLoginCache;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.d1540173108.hrz.view.MainFrg;
import com.igexin.sdk.PushManager;
import com.luck.picture.lib.tools.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends BaseActivity {


    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    public static boolean isArAct = false;//记录是否AR页面出来

    @Override
    protected void initView() {
        setSofia(false);
        ShareIsLoginCache.getInstance(act).save(true);
        if (findFragment(MainFrg.class) == null) {
            loadRootFragment(R.id.fl_container, MainFrg.newInstance());
        }

        MediaServiceIntent = new Intent(this, MediaService.class);
        bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        ServiceUtils.startService(PhoneListenService.class);

        startService(new Intent(MainActivity.this,StartService.class));
//        stopService(new Intent(MainActivity.this,StartService.class));

    }

    @Override
    public void onResume() {
        super.onResume();
        initGT();
    }

    private void initGT() {
        // 为第三方自定义推送服务
        PushManager.getInstance().initialize(act, GtPushService.class);
//         为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(act, GtIntentService.class);
        PushManager.getInstance().turnOnPush(act);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnection != null){
            unbindService(mServiceConnection);
        }
        ServiceUtils.stopService(PhoneListenService.class);
    }

    public static int mPos = 0;
    public static MediaService.MyBinder mMyBinder;
    public static boolean isDow = false;
    Intent MediaServiceIntent;
    public static boolean isOnePlay = false;//记录整个项目是否播放过一次
    public static boolean isWifi = false;//记录一次是否流量下进行

    public static int REQUEST_CODE_SCAN = 111;



    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            EventBus.getDefault().post(new MediaServiceInEvent(name, service));
            LogUtils.e( "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isArAct = false;
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
//                String content = data.getStringExtra(Constant.CODED_CONTENT);
//                showToast("扫描结果为：" + content);
            }
        }
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(new CameraInEvent(requestCode, data));
        }
    }



}
