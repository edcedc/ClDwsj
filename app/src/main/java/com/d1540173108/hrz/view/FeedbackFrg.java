package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.FFeedbackBinding;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/10.
 *  提交反馈
 */

public class FeedbackFrg extends BaseFragment<BasePresenter, FFeedbackBinding>{
    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_feedback;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.feedback));

        mB.btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.onBackPressed();
            }
        });
        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = mB.etText.getText().toString();
                if (StringUtils.isEmpty(text)){

                    return;
                }

                CloudApi.feedbackSaveFeedbackMsg(text, text)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);

                            }

                            @Override
                            public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    mB.ly.setVisibility(View.GONE);
                                    mB.lyLayout.setVisibility(View.VISIBLE);
                                }
//                                showToast(baseResponseBeanResponse.body().message);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                hideLoading();

                            }
                        });
            }
        });
    }
}
