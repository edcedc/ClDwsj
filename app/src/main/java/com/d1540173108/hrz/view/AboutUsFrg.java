package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ToastUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseListContract;
import com.d1540173108.hrz.base.BaseListPresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.FAboutBinding;

import java.util.List;

/**
 * Created by edison on 2019/1/10.
 *  关于神奇动物
 */

public class AboutUsFrg extends BaseFragment<BaseListPresenter, FAboutBinding> implements BaseListContract.View{
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_about;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.magical_animals));
//        mPresenter.onRequest(CloudApi.agreementGetUserAgreement, 1);
        mB.webView.setInitialScale(100);
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
    public boolean onBackPressedSupport() {
        mB.webView.goBack();// 返回前一个页面
        return super.onBackPressedSupport();
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {

    }

    @Override
    public void setData(Object data) {
        List<DataBean> listBean = (List<DataBean>) data;
        DataBean bean = listBean.get(1);
        mB.webView.loadDataWithBaseURL(null, bean.getContent(), "text/html", "utf-8", null);
    }
}
