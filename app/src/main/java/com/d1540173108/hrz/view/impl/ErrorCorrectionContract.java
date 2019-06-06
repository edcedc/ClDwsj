package com.d1540173108.hrz.view.impl;

import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;

/**
 * Created by edison on 2019/1/14.
 */

public interface ErrorCorrectionContract {

    interface View extends IBaseView {

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onConfirm(String text, String path);
    }

}
