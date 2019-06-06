package com.d1540173108.hrz.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.view.impl.ErrorCorrectionContract;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/14.
 */

public class ErrorCorrectionPresenter extends ErrorCorrectionContract.Presenter{
    @Override
    public void onConfirm(String text, String path) {
        if (StringUtils.isEmpty(text)){
            showToast(act.getString(R.string.enter_animal_names));
            return;
        }
        CloudApi.chatbotSend(path, text)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
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
                        if (jsonObject.optInt("code") == 200){
                            showToast("上传成功");
                            act.onBackPressed();
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
