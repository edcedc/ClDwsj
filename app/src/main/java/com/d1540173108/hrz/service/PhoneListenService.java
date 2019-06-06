package com.d1540173108.hrz.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.event.PhoneListenInEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edison on 2019/1/27.
 */

public class PhoneListenService extends Service {

    private static final String tag = "PhoneListenService";

    // 电话管理者对象
    private TelephonyManager mTelephonyManager;
    // 电话状态监听者
    private MyPhoneStateListener myPhoneStateListener;
    // 动态监听去电的广播接收器
    private InnerOutCallReceiver mInnerOutCallReceiver;

    @Override
    public void onCreate() {
        LogUtils.e("启动了手机服务");
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        // 动态注册广播接收器监听去电信息
        mInnerOutCallReceiver = new InnerOutCallReceiver();
        // 手机拨打电话时会发送：android.intent.action.NEW_OUTGOING_CALL的广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mInnerOutCallReceiver, intentFilter);
        super.onCreate();
    }

    /**
     * 动态注册广播接收器监听去电信息
     */
    class InnerOutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 获取播出的去电号码
            String outPhone = getResultData();
            LogUtils.e(tag, "outPhone:" + outPhone);
        }
    }

    /**
     * 自定义内部类对来电的电话状态进行监听
     */
    class MyPhoneStateListener extends PhoneStateListener {
        // 重写电话状态改变时触发的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtils.e(tag, "响铃:" + incomingNumber);
                    EventBus.getDefault().post(new PhoneListenInEvent(true));
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    LogUtils.e(tag, "接听");
                    EventBus.getDefault().post(new PhoneListenInEvent(true));
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    EventBus.getDefault().post(new PhoneListenInEvent(false));
                    LogUtils.e(tag, "挂断");
                    break;
                    default:
                        LogUtils.e(tag, "不知道什么状态");
                    break;
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // 取消来电的电话状态监听服务
        if (mTelephonyManager != null && myPhoneStateListener != null) {
            mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        // 取消去电的广播监听
        if (mInnerOutCallReceiver != null) {
            unregisterReceiver(mInnerOutCallReceiver);
        }
        super.onDestroy();
    }
}
