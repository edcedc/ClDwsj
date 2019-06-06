package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;

import org.json.JSONObject;

/**
 * Created by edison on 2019/2/21.
 */

public interface IdentifyingContract {

    interface View extends IBaseView {

        void setData(String result);

        void setGetError();
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onSend(String path);
    }

}
