package com.d1540173108.hrz.view.act;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.weight.HtmlFormat;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.databinding.AHtmlBinding;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  统一H5
 */

public class HtmlAct extends BaseActivity<BasePresenter, AHtmlBinding> {

    private String url;
    private String id;
    private int type = 0;
    private String title;
    private String content;

    public static final int queryAPPAgreement = 0;//2: 用户协议，3: 会员权益说明 4: 推广规则

    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.a_html;
    }

    @Override
    protected void initParms(Bundle bundle) {
        url = bundle.getString("url");
        id = bundle.getString("id");
        type = bundle.getInt("type");
        title = bundle.getString("title");
        content = bundle.getString("content");
    }

    @Override
    protected void initView() {
        setSofia(true);
        setTitle(title);
        if (type == 0){
            if (!StringUtils.isEmpty(url)){
                mB.webView.loadUrl(url);
            }else {
                mB.webView.loadUrl("https://www.baidu.com/");
            }
        }else {
            mB.webView.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        }


        mB.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView var1, int var2, String var3, String var4) {
                mB.progressBar.setVisibility(View.GONE);
                ToastUtils.showShort("网页加载失败");
            }
        });
        //进度条
        mB.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mB.progressBar.setVisibility(View.GONE);
                    return;
                }
                mB.progressBar.setVisibility(View.VISIBLE);
                mB.progressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mB.webView.canGoBack()) {
            mB.webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void getHtmlUrl(){
        /*CloudApi.commonQueryAPPAgreement(type)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                String remark = data.getContext();
                                if (StringUtils.isEmpty(remark)){
                                    remark = data.getContent();
                                }
                                mB.webView.loadDataWithBaseURL(null, remark, "text/html", "utf-8", null);
                            }
                        }else {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        HtmlAct.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                });*/
    }

    @Override
    protected void onDestroy() {
        mB.webView.removeAllViews();
        mB.webView.destroy();
        super.onDestroy();
    }
}
