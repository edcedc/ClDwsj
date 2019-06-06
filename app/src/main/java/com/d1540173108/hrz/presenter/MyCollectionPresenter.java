package com.d1540173108.hrz.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.bean.BaseListBean;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.view.impl.MyCollectionContract;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/10.
 */

public class MyCollectionPresenter extends MyCollectionContract.Presenter{

    @Override
    public void onRequest(int pageNumber) {
        CloudApi.storyGetStoryList(pageNumber, Constants.pageSize)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getRows();
                                if (list != null && list.size() != 0){
                                    mView.setData(list);
                                    mView.setRefreshLayoutMode(data.getRecords());
                                    mView.hideLoading();
                                }else {
                                    mView.showLoadEmpty();
                                }
                            }
                        }else {
                            mView.showLoadEmpty();
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
    public void onDel(final List<DataBean> listBean) {
        StringBuilder sb = new StringBuilder();
        final List<DataBean> temp = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < listBean.size(); i++) {
            DataBean bean = listBean.get(i);
            if (bean.isSelect()){
                sb.append(bean.getStoryId()).append(",");
                temp.add(bean);
                listStr.add(bean.getStoryId());
            }
        }
        if (StringUtils.isEmpty(sb.toString()))return;
        String substring = sb.substring(0, sb.length() - 1);

        CloudApi.storyDelPlayNum(substring)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            mView.Del(temp);
                        }
                        showToast(baseResponseBeanResponse.body().message);
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
