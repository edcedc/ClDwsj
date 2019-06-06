package com.d1540173108.hrz.view.impl;


import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;

/**
 * Created by edison on 2018/11/17.
 */

public interface LoginContract {

    interface View extends IBaseView {

        void onCodeSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onLogin(String phone, String pwd);

        public abstract void onCode(String phone);

        public abstract void onUserAgreement();

        public abstract void onThirdLogin(String openid, String addr, String name, String head);
    }

}
