package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/14.
 */

public interface PlayMusicContract {

    interface View extends IBaseView {

        void onData();

        void onIsColletion(boolean b);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onSavePlayNum(String storyId, int type);

        public abstract void onColletion(String storyId, int type, boolean isColletion);

        public abstract void onSetData(List<DataBean> listBean, int position);

        public abstract void onDow(List<DataBean> listBean);

        public abstract void onIsColletion(String storyId);
    }

}
