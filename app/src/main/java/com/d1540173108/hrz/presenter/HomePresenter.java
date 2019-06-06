package com.d1540173108.hrz.presenter;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.LabelAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.BaseListBean;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.view.impl.HomeContarct;
import com.d1540173108.hrz.weight.WithScrollGridView;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/13.
 */

public class HomePresenter extends HomeContarct.Presenter{

    @Override
    public void onSleep(final BaseFragment root, WithScrollGridView gridView) {
        String[] laberStr = {"小知识", act.getString(R.string.story), act.getString(R.string.picture_book), act.getString(R.string.animation)};
        int[] laberImg = {R.mipmap.y49, R.mipmap.icon_gus, R.mipmap.icon_huiben, R.mipmap.icon_donghua};

        final List<DataBean> listStr = new ArrayList<>();
        for (int i = 0; i < laberStr.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(laberStr[i]);
            bean.setType(1);
            bean.setImg(laberImg[i]);
            listStr.add(bean);
        }
        LabelAdapter labelAdapter = new LabelAdapter(act, listStr);
        gridView.setAdapter(labelAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DataBean bean = listStr.get(i);
                String name = bean.getName();
                switch (i){
                    case 0:
                        UIHelper.startHtmlAct(act.getString(R.string.little_knowledge), "http://chimelong.peanuts.cc/index1.html");
                        break;
                    case 1:
                        UIHelper.startStoryListFrg(root, name, i);
                        break;
                    default:
//                        UIHelper.startStoryListFrg(root, name, i);
                        UIHelper.startCeShiAct();
                        break;
                }
            }
        });

    }

    @Override
    public void onRequest() {
        CloudApi.list(1, CloudApi.storyGetStoryList)
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
                                }
                            }
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
