package com.d1540173108.hrz.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.sofia.Sofia;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.utils.TUtil;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public abstract class BaseActivity<P extends BasePresenter, VB extends ViewDataBinding> extends SupportActivity {

    protected Activity act;
    protected VB mB;
    public P mPresenter;

    private Bitmap bitPath;

    public Bitmap getBitPath() {
        return bitPath;
    }

    public void setBitPath(Bitmap bitPath) {
        this.bitPath = bitPath;
    }

    /**
     * 重写getResources()方法，让APP的字体不受系统设置字体大小影响
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mB = DataBindingUtil.setContentView(this, this.bindLayout());
        ;
        act = this;
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.act = this;
            this.initPresenter();
        }
        // 初始化参数
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        initParms(bundle);

        initView();
    }

    protected void setSofia(boolean isFullScreen) {
//        if (!isFullScreen) {
//            Sofia.with(act)
//                    .invasionStatusBar()
//                    .statusBarBackgroundAlpha(0)
//                    .statusBarLightFont()
//                    .statusBarBackground(ContextCompat.getColor(act, R.color.white))
//            ;
//        } else {
//            Sofia.with(this)
//                    .invasionStatusBar()
//                    .statusBarLightFont()
//                    .statusBarBackground(Color.TRANSPARENT)
//                    .navigationBarBackground(Color.TRANSPARENT);
//        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    protected abstract int bindLayout();

    protected abstract void initParms(Bundle bundle);

    protected abstract void initView();

    protected void setTitle(String title) {
        title(title, null, -1, true);
    }
    protected void setTitle(String title, String right) {
        title(title,  right, -1, true);
    }
    protected void setTitle(String title, boolean isBack) {
        title(title,  null, -1, isBack);
    }
    protected void setTitle(String title, String right, boolean isBack) {
        title(title, right, -1, isBack);
    }

    private void title(String title, String rightText, int img, boolean isBack) {
        setSofia(false);
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView topTitle = findViewById(R.id.top_title);
        TextView topRight = findViewById(R.id.top_right);
        FrameLayout topRightFy = findViewById(R.id.top_right_fy);
        //需要调用该函数才能设置toolbar的信息
        mAppCompatActivity.setSupportActionBar(toolbar);
        mAppCompatActivity.getSupportActionBar().setTitle("");
        if (isBack){
            toolbar.setNavigationIcon(R.mipmap.close);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    act.onBackPressed();
                }
            });
        }else {
            toolbar.setNavigationIcon(null);
        }
        topTitle.setText(title);
        if (!StringUtils.isEmpty(rightText)){
            topRightFy.setVisibility(View.VISIBLE);
            topRight.setText(rightText);
        }else if (img != -1){
            topRightFy.setVisibility(View.VISIBLE);
            topRight.setBackgroundResource(img);
        }else {

        }
        topRightFy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnRightClickListener();
            }
        });
    }

    protected void setOnRightClickListener() {

    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    protected boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    private ProgressDialog dialog;

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
    }

    public void onError(Throwable e) {
        mHandler.sendEmptyMessage(handler_hide);
        LogUtils.e(e.getMessage());
        showToast(e.getMessage());
    }

    private final int handler_load = 0;
    private final int handler_hide = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case handler_load:
                    if (dialog != null && dialog.isShowing()) return;
                    dialog = new ProgressDialog(act);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("请求网络中...");
                    dialog.show();
                    break;
                case handler_hide:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
            }
        }
    };

    protected void showToast(final String str){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.showLong(str);
                ToastUtils.showShort(str);
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }

    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

}
