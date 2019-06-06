package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.bean.DataBean;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public interface AnimalDescContract {

    interface View extends IBaseView {

        void setData(Object object);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(String id);
    }

}
