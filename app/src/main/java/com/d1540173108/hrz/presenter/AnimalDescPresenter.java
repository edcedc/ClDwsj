package com.d1540173108.hrz.presenter;

import android.os.Handler;

import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.view.impl.AnimalDescContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/13.
 */

public class AnimalDescPresenter extends AnimalDescContract.Presenter{
    @Override
    public void onRequest(String id) {
        CloudApi.info(CloudApi.WEIRUAN_URL + "v1/chatbot/animallibrary/" + id + "/info")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {
                        if (jsonObject != null){
                            mView.setData(jsonObject);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
