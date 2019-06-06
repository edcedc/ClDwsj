package com.d1540173108.hrz.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.view.impl.AnimalEncyclopediaContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/11.
 */

public class AnimalEncyclopediaPresenter extends AnimalEncyclopediaContract.Presenter{
    @Override
    public void onRequest(int pageNumber) {
        CloudApi.animalstarList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONArray>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONArray jsonArray) {
                        if (jsonArray != null){
                            List<DataBean> list = new ArrayList<>();
                            for (int i = 0;i < jsonArray.length();i++){
                                JSONObject object = jsonArray.optJSONObject(i);
                                DataBean bean = new DataBean();
                                bean.setName(object.optString("tag"));
                                bean.setImage(object.optString("imageUrl"));
                                bean.setId(object.optString("tagId"));
                                bean.setProbability(object.optDouble("probability"));
                                list.add(bean);
                            }
                            mView.setData(list);
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

    @Override
    public void onSearch(String search) {
        if (StringUtils.isEmpty(search)){
            showToast(act.getString(R.string.no_search));
            return;
        }
        CloudApi.send(CloudApi.WEIRUAN_URL + "v1/chatbot/animalAI/" + search + "/send")
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONArray>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONArray jsonArray) {
                        if (jsonArray != null && jsonArray.length() != 0){
                            List<DataBean> list = new ArrayList<>();
                            for (int i = 0;i < jsonArray.length();i++){
                                JSONObject object = jsonArray.optJSONObject(i);
                                DataBean bean = new DataBean();
                                bean.setName(object.optString("tag"));
                                bean.setImage(object.optString("imageUrl"));
                                bean.setId(object.optString("tagId"));
                                bean.setProbability(object.optDouble("probability"));
                                list.add(bean);

                                mView.setInto(object);
                                break;
                            }

//                            mView.setData(list);
                        }else {
                            showToast("无此动物");
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

    @Override
    public void onInfo(String id) {
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
                            mView.setInto(jsonObject);
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
}
