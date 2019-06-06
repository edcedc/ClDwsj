package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.FUpdateNicknameBinding;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/10.
 *  修改昵称
 */

public class UpdateNicknameFrg extends BaseFragment<BasePresenter, FUpdateNicknameBinding> implements IBaseView{
    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_nickname;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.set_nick), getString(R.string.submit2));
        JSONObject userInfoObj = User.getInstance().getUserInfoObj();
        mB.etText.setText(userInfoObj.optString("userNickName"));
        mB.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mB.etText.setText("");
            }
        });
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        final String name = mB.etText.getText().toString();
        if (StringUtils.isEmpty(name)){
            showToast(getString(R.string.error_nickname));
            return;
        }
        if (name.equals("")){
            act.onBackPressed();
            return;
        }
        CloudApi.userSaveUserPhone(name, null, null)
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
                            try {
                                JSONObject userInfoObj = User.getInstance().getUserInfoObj();
                                userInfoObj.put("userNickName", name);
                                userInfoObj.put("userName", name);
                                act.onBackPressed();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        showToast(baseResponseBeanResponse.body().message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        UpdateNicknameFrg.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });

    }
}
