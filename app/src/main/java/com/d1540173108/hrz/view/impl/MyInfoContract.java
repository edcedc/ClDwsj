package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;

/**
 * Created by edison on 2019/1/10.
 */

public interface MyInfoContract {
    interface View extends IBaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void updateSex(int sex);

        public abstract void updateBirthDate(String s);
    }
}
