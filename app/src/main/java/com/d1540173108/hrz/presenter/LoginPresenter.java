package com.d1540173108.hrz.presenter;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.event.LoginInEvent;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.d1540173108.hrz.utils.cache.SharedAccount;
import com.d1540173108.hrz.view.impl.LoginContract;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2018/11/17.
 */

public class LoginPresenter extends LoginContract.Presenter{
    @Override
    public void onLogin(final String phone, final String code) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)){
            showToast(act.getString(R.string.error_phone1));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_phone));
            return;
        }
        CloudApi.authLogin(phone, code)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = jsonObject.optJSONObject("data");
                            User.getInstance().setUserInfoObj(data);
                            User.getInstance().setLogin(true);
                            ShareSessionIdCache.getInstance(act).save(data.optString("userId"));
                            SharedAccount.getInstance(act).save(phone, null);
                            MobclickAgent.onProfileSignIn(data.optString("userId"));
                            EventBus.getDefault().post(new LoginInEvent());
//                            UIHelper.startMainAct();
//                            ActivityUtils.finishAllActivities();
                            act.finish();
                        }else {
                            showToast(jsonObject.optString("message"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });

    }

    @Override
    public void onCode(String phone) {
        if (StringUtils.isEmpty(phone)){
            showToast(act.getString(R.string.error_phone1));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_phone));
            return;
        }
        CloudApi.authGetSms(phone)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                        showToast(baseResponseBeanResponse.body().message);
                        mView.onCodeSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void onUserAgreement() {

    }

    @Override
    public void onThirdLogin(String openid, final String addr, final String name, String head) {
        CloudApi.userOpenIdChangeUserId(openid, addr, name, head)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data != null){
                                String userName = data.optString("userName");
                                String url = data.optString("url");
                                try {
                                    if (StringUtils.isEmpty(userName) || userName.equals("null")){
                                        data.put("userName", name);
                                    }
                                    if (StringUtils.isEmpty(url) || url.equals("null")){
                                        data.put("url", url);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                MobclickAgent.onProfileSignIn(data.optString("userId"));
                                //当用户使用第三方账号（如新浪微博）登录时，可以这样统计：
                                switch (addr){
                                    case "微博":
                                        MobclickAgent.onProfileSignIn("WB", data.optString("userId"));
                                        break;
                                    case "微信":
                                        MobclickAgent.onProfileSignIn("WX", data.optString("userId"));
                                        break;
                                    case "QQ":
                                        MobclickAgent.onProfileSignIn("QQ", data.optString("userId"));
                                        break;
                                }
                                User.getInstance().setLogin(true);
                                User.getInstance().setUserInfoObj(data);
                                ShareSessionIdCache.getInstance(act).save(data.optString("userId"));
                                MobclickAgent.onProfileSignIn(data.optString("userId"));
                                EventBus.getDefault().post(new LoginInEvent());
//                                UIHelper.startMainAct();
//                                ActivityUtils.finishAllActivities();
                                act.finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
