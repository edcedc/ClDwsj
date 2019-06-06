package com.d1540173108.hrz.base;


import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/8.
 */

public interface IBaseView {

    void showLoading();

    void hideLoading();

    void onError(Throwable e);

    void addDisposable(Disposable d);

    void showLoadDataing();

    void showLoadEmpty();

}
