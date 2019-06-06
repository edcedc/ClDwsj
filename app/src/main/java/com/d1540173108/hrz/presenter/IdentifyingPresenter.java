package com.d1540173108.hrz.presenter;

import android.os.CountDownTimer;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.view.impl.IdentifyingContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/2/21.
 */

public class IdentifyingPresenter extends IdentifyingContract.Presenter{

    private long nowMills;

    @Override
    public void onSend(String path) {
        if (path == null)return;
        nowMills = TimeUtils.getNowMills();
        CloudApi.chatbotSend(path, nowMills)
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
                        if (jsonObject.optInt("code") == 200){
                            getRecoResult(nowMills);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void getRecoResult(final long nowMills){
        CloudApi.getRecoResult(nowMills)
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
                            String result = jsonObject.optString("result");
                            if (result.equals("null")){
                                downTimer.start();
                            }else {
                                JSONObject data = jsonObject.optJSONObject("result");
                                JSONArray attachments = data.optJSONArray("attachments");
                                if (attachments != null && attachments.length() != 0) {
                                    for (int i = 0;i < attachments.length();i++) {
                                        JSONObject object = attachments.optJSONObject(i);
                                        String content1 = object.optString("content");
                                        if (content1.contains("Result:0")){
                                            mView.setGetError();
                                            return;
                                        }else {
                                            mView.setData(result);
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        getRecoResult(nowMills);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private CountDownTimer downTimer = new CountDownTimer(6000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            getRecoResult(nowMills);
        }
    };

}
