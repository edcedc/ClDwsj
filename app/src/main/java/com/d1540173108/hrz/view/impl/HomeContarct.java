package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.weight.WithScrollGridView;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public interface HomeContarct {

    interface View extends IBaseView {

        void setData(List<DataBean> listBean);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onSleep(BaseFragment root, WithScrollGridView gridView);

        public abstract void onRequest();
    }

}
