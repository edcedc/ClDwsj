package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FLoginBinding;
import com.d1540173108.hrz.presenter.LoginPresenter;
import com.d1540173108.hrz.utils.CountDownTimerUtils;
import com.d1540173108.hrz.utils.IsInstallWeChatOrAliPay;
import com.d1540173108.hrz.utils.cache.ShareIsLoginCache;
import com.d1540173108.hrz.utils.cache.SharedAccount;
import com.d1540173108.hrz.view.impl.LoginContract;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * Created by edison on 2019/1/9.
 *  登录
 */

public class LoginFrg extends BaseFragment<LoginPresenter, FLoginBinding> implements LoginContract.View, View.OnClickListener{

    public static LoginFrg newInstance() {
        Bundle args = new Bundle();
        LoginFrg fragment = new LoginFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_login;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.login));
        ShareIsLoginCache.getInstance(act).save(true);
        mB.btCode.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.tvWx.setOnClickListener(this);
        mB.tvWb.setOnClickListener(this);
        mB.tvQq.setOnClickListener(this);
        mB.ly.setOnClickListener(this);
        mB.refreshLayout.setPureScrollModeOn();

        String mobile = SharedAccount.getInstance(act).getMobile();
        if (!StringUtils.isEmpty(mobile)){
            mB.etPhone.setText(mobile);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_code:
                mPresenter.onCode(mB.etPhone.getText().toString());
                break;
            case R.id.bt_submit:
                mPresenter.onLogin(mB.etPhone.getText().toString(), mB.etCode.getText().toString());
                break;
            case R.id.ly_:
                UIHelper.startFeedbackFrg(this, 0);
                break;
            case R.id.tv_wx:
                if (!IsInstallWeChatOrAliPay.isWeixinAvilible(act)){
                    showToast("未安装微信客户端");
                    return;
                }
                addr = "微信";
                UMShareAPI.get(act).getPlatformInfo(act, SHARE_MEDIA.WEIXIN, listener);
                break;
            case R.id.tv_wb:
                if (!IsInstallWeChatOrAliPay.isSinaInstalled(act)){
                    showToast("未安装微博客户端");
                    return;
                }
                addr = "微博";
                UMShareAPI.get(act).getPlatformInfo(act, SHARE_MEDIA.SINA, listener);
                break;
            case R.id.tv_qq:
                if (!IsInstallWeChatOrAliPay.isQQClientAvailable(act)){
                    showToast("未QQ微博客户端");
                    return;
                }
                addr = "QQ";
                UMShareAPI.get(act).getPlatformInfo(act, SHARE_MEDIA.QQ, listener);
                break;
        }
    }

    private String addr = "";

    private UMAuthListener listener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (map != null && map.size() != 0){
                String openid = "";
                String name = "";
                String head = "";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    LogUtils.e(entry.getKey(), entry.getValue());
                    if (entry.getKey().equals("uid")){
                        openid = entry.getValue();
                    }
                    if (entry.getKey().equals("name")){
                        name = entry.getValue();
                    }
                    //微博头像
                    if (entry.getKey().equals("avatar_hd")){
                        head = entry.getValue();
                    }
                    //QQ
                    if (entry.getKey().equals("profile_image_url")){
                        head = entry.getValue();
                    }
                    //微信
                    if (entry.getKey().equals("profile_image_url")){
                        head = entry.getValue();
                    }

                    /*if (entry.getKey().equals("openid")){
                        openid = entry.getValue();
                        addr = "微信";
                    }
                    if (entry.getKey().equals("iconurl")){
                        head = entry.getValue();
                    }


                     if (entry.getKey().equals("profile_image_url")){
                        head = entry.getValue();
                    }
                    if (entry.getKey().equals("id")){
                        openid = entry.getValue();
                        addr = "微博";
                    }
                    if (entry.getKey().equals("cover_image_phone")){
                        head = entry.getValue();
                    }

                    if (entry.getKey().equals("name")){
                        name = entry.getValue();
                    }*/
                }
                mPresenter.onThirdLogin(openid, addr, name, head);
            }else {
                showToast("错误");
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable t) {
            LogUtils.e("失败：" + t.getMessage());
            showToast(t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            LogUtils.e("取消了");
        }
    };

    @Override
    public void onCodeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.btCode).start();
    }
}
