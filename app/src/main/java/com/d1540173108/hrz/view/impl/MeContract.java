package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.weight.WithScrollGridView;

import org.json.JSONObject;

/**
 * Created by edison on 2019/1/10.
 */

public interface MeContract {

    interface View extends IBaseView {

        void setData(JSONObject data);

        void setHead(String message);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onInit(WithScrollGridView gridView, BaseFragment root);

        public abstract void onRequest();

        public abstract void onUpdateHead(String path);
    }

}
