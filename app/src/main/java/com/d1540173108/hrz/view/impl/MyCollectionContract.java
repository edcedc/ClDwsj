package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseListView;
import com.d1540173108.hrz.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/10.
 */

public interface MyCollectionContract {

    interface View extends IBaseListView {

        void Del(List<DataBean> temp);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(int pageNumber);

        public abstract void onDel(List<DataBean> ids);
    }

}
