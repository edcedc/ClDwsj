/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.d1540173108.hrz.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yanzhenjie.sofia.Sofia;
import com.yanzhenjie.sofia.StatusView;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.event.PayInEvent;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.utils.TUtil;
import com.d1540173108.hrz.utils.pay.PayResult;
import com.d1540173108.hrz.weight.GridDividerItemDecoration;
import com.d1540173108.hrz.weight.LoadDataLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.Map;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation_swipeback.SwipeBackFragment;

/**
 * 若把初始化内容放到initData实现,就是采用Lazy方式加载的Fragment
 * 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
 * -
 * -注1: 如果是与ViewPager一起使用，调用的是setUserVisibleHint。
 * ------可以调用mViewPager.setOffscreenPageLimit(size),若设置了该属性 则viewpager会缓存指定数量的Fragment
 * -注2: 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
 * -注3: 针对初始就show的Fragment 为了触发onHiddenChanged事件 达到lazy效果 需要先hide再show
 */

/**
 * ================================================
 * 作    者：yc）
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public abstract class BaseFragment<P extends BasePresenter, VB extends ViewDataBinding> extends SwipeBackFragment {

    private boolean isVisible = true;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    protected Activity act;
    private View view;

    protected VB mB;
    public P mPresenter;
    private LoadDataLayout swipeLoadDataLayout;


    protected int pagerNumber = 1;//网络请求默认第一页
    protected int mTotalPage;//网络请求当前几页
    protected int TOTAL_COUNTER;//网络请求一共有几页

    protected boolean isTopFrg = false;//记录是否onResum导航栏



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        this.inflater = inflater;
        isFirstLoad = true;

        View rootView = getLayoutInflater().inflate(this.bindLayout(), null, false);
        mB = DataBindingUtil.bind(rootView);
        isPrepared = true;
        // 初始化参数
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.view = rootView;
        mPresenter = TUtil.getT(this, 0);
        if (mPresenter != null) {
            mPresenter.act = this.getActivity();
            this.initPresenter();
        }
        api = WXAPIFactory.createWXAPI(act, Constants.WX_APPID);
        initParms(bundle);
        initView(rootView);

        swipeLoadDataLayout = view.findViewById(R.id.swipeLoadDataLayout);
        return attachToSwipeBack(rootView);
    }

    //简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
    public abstract void initPresenter();

    protected void setSofia(boolean isFullScreen) {
        sofia(isFullScreen, R.color.white);
    }
    protected void setSofia(boolean isFullScreen, int color) {
        sofia(isFullScreen, color);
    }
    private void sofia(boolean isFullScreen, int color){
//        if (!isFullScreen) {
//            Sofia.with(act)
//                    .statusBarLightFont()
//                    .invasionStatusBar()
//                    .statusBarBackgroundAlpha(0)
//                    .statusBarDarkFont()
//                    .statusBarBackground(ContextCompat.getColor(act, color))
//            ;
//        } else {
//            Sofia.with(act)
//                    .invasionStatusBar()
////                    .invasionNavigationBar()
//                    .statusBarDarkFont()
//                    .statusBarBackgroundAlpha(0)
//            ;
//        }
    }

    protected abstract void initParms(Bundle bundle);

    protected abstract int bindLayout();

    protected abstract void initView(View view);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate");
        act = getActivity();
    }

    protected void showToast(final String str) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.showLong(str);
                ToastUtils.showShort(str);
                LogUtils.e(str);
            }
        });
    }


    private ProgressDialog dialog;

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
        mHandler.sendEmptyMessage(handler_success);
    }

    public void onError(Throwable e) {
        if (null != e) {
            mHandler.sendEmptyMessage(handler_hide);
            mHandler.sendEmptyMessage(handler_success);
            LogUtils.e(e.getMessage());
            showToast(e.getMessage());
        }else{
            LogUtils.e("请求之外Throwable,断点");
        }
    }

    private CompositeDisposable compositeDisposable;

    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    private void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    private final int handler_load = 0;
    private final int handler_hide = 1;
    private final int handler_empty = 2;
    private final int handler_error = 3;
    private final int handler_no_network = 4;
    private final int handler_loadData = 5;
    private final int handler_success = 6;

    private final int SDK_PAY_FLAG = 7;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
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
                    hideLoad2ing();
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case handler_empty:
                    if (swipeLoadDataLayout != null && pagerNumber == 1) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.EMPTY);
                    }
                    break;
                case handler_loadData:
                    if (swipeLoadDataLayout != null) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.LOADING);
                    }
                    break;
                case handler_success:
                    if (swipeLoadDataLayout != null) {
                        swipeLoadDataLayout.setStatus(LoadDataLayout.SUCCESS);
                    }
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToast("支付成功");
                        EventBus.getDefault().post(new PayInEvent());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToast("支付失败");
                    }
                    break;
            }
        }
    };

    private void hideLoad2ing() {
        mHandler.sendEmptyMessage(handler_success);
    }

    public void showLoadDataing() {
        mHandler.sendEmptyMessage(handler_loadData);
    }

    public void showLoadEmpty() {
        mHandler.sendEmptyMessage(handler_empty);
    }

    protected void setRefreshLayout(TwinklingRefreshLayout refreshLayout, RefreshListenerAdapter listener) {
//        ProgressLayout headerView = new ProgressLayout(act);
//        refreshLayout.setHeaderView(headerView);
//        refreshLayout.setOverScrollRefreshShow(false);
        ProgressLayout header = new ProgressLayout(act);
        refreshLayout.setHeaderView(header);
        refreshLayout.setFloatRefresh(true);
        refreshLayout.setOverScrollRefreshShow(false);
        refreshLayout.setHeaderHeight(140);
        refreshLayout.setMaxHeadHeight(240);
        refreshLayout.setOverScrollHeight(200);
        refreshLayout.setOnRefreshListener(listener);
    }

    /**
     *  是否有更多
     * @param listSize
     * @param totalRow
     * @param refreshLayout
     */
    protected void setRefreshLayoutMode(int listSize, int totalRow, TwinklingRefreshLayout refreshLayout) {
        if (listSize == totalRow) {
            refreshLayout.setEnableLoadmore(false);
        } else {
            refreshLayout.setEnableLoadmore(true);
        }
    }

    protected void setRefreshLayout(final int pagerNumber, final TwinklingRefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pagerNumber == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
            }
        }, 300);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setRecyclerViewType(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void setRecyclerViewGridType(RecyclerView recyclerView, int spanCount, int height, int color){
        recyclerView.setLayoutManager(new GridLayoutManager(act, spanCount));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
//        recyclerView.addItemDecoration(new GridDividerItemDecoration(height, ContextCompat.getColor(act,color)));
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.topMargin = 20;
//        params.leftMargin = 20;
//        params.rightMargin = 20;
//        recyclerView.setLayoutParams(params);
//        recyclerView.setPadding(15, 15, 15, 0);
    }

    protected void setOnRightClickListener() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.d("onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.d("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        hideLoading();
        dispose();
        super.onDestroy();
        LogUtils.d("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.d("onDetach");
    }


    protected void setTitle(String title) {
        title(title, null, -1, true, -1, -1);
    }
    protected void setTitle(String title, int img) {
        title(title, null, img, true, -1, -1);
    }
    protected void setTitle(String title, int textColor, int backgroundColor) {
        title(title, null, -1, true, textColor, backgroundColor);
    }
    protected void setTitle(String title, int img, int textColor, int backgroundColor) {
        title(title, null, img, true, textColor, backgroundColor);
    }
    protected void setTitle(String title, String right) {
        title(title,  right, -1, true, -1, -1);
    }
    protected void setTitle(String title, boolean isBack) {
        title(title,  null, -1, isBack, -1, -1);
    }
    protected void setTitle(String title, String right, boolean isBack) {
        title(title, right, -1, isBack, -1, -1);
    }

    private void title(String title, String rightText, int img, boolean isBack, int textColor, int backgroundColor) {
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        TextView topTitle = view.findViewById(R.id.top_title);
        TextView topRight = view.findViewById(R.id.top_right);
        FrameLayout topRightFy = view.findViewById(R.id.top_right_fy);
        ImageView topIvRight = view.findViewById(R.id.top_iv_right);
        StatusView statusView = view.findViewById(R.id.status_view);
        View topView = view.findViewById(R.id.top_view);
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
            topView.setVisibility(View.VISIBLE);
        }else {
            topView.setVisibility(View.GONE);
            toolbar.setNavigationIcon(null);
        }
        topTitle.setText(title);
        if (!StringUtils.isEmpty(rightText)){
            topRightFy.setVisibility(View.VISIBLE);
            topIvRight.setVisibility(View.GONE);
            topRight.setText(rightText);
        }else if (img != -1){
            topRightFy.setVisibility(View.VISIBLE);
            topRight.setVisibility(View.GONE);
            topIvRight.setVisibility(View.VISIBLE);
            topIvRight.setBackgroundResource(img);
        }else {

        }
        //设置标题栏颜色
        if (backgroundColor != - 1){
            toolbar.setBackgroundColor(act.getColor(backgroundColor));
            setSofia(false, backgroundColor);
        }else {
            setSofia(false);
        }
        //设置title颜色
        if (textColor != -1){
            toolbar.setNavigationIcon(R.mipmap.icon_fh);
            topTitle.setTextColor(act.getColor(textColor));
        }
        topRightFy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnRightClickListener();
            }
        });
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



    //支付宝支付
    public void pay(final String info){
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(act);
                Map<String, String> result = alipay.payV2(info,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    protected IWXAPI api;
    protected void wxPay(final JSONObject data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (api.getWXAppSupportAPI() >= com.tencent.mm.opensdk.constants.Build.PAY_SUPPORTED_SDK_INT) {
                    PayReq req = new PayReq();
                    req.appId = data.optString("appid");
                    req.partnerId = data.optString("partnerid");
                    req.prepayId = data.optString("prepayid");
                    req.nonceStr = data.optString("noncestr");
                    req.timeStamp = data.optString("timestamp");
                    req.packageValue = data.optString("package");
                    req.sign = data.optString("sign");
                    req.extData = "app data"; // optional
                    api.sendReq(req);
                }else {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast(getResources().getString(R.string.not_wx_pay));
                        }
                    });
                }
            }
        }).start();
    }
}
