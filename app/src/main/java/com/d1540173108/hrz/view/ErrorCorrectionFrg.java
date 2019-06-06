package com.d1540173108.hrz.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.databinding.FErrorCorrectionBinding;
import com.d1540173108.hrz.presenter.ErrorCorrectionPresenter;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.view.impl.ErrorCorrectionContract;

/**
 * Created by edison on 2019/1/13.
 *  纠错
 */

public class ErrorCorrectionFrg extends BaseFragment<ErrorCorrectionPresenter, FErrorCorrectionBinding> implements ErrorCorrectionContract.View{

    private String path;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        path = bundle.getString("path");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_error_correction;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.error_correction2), R.color.white, R.color.blue_6759AE);
        mB.refreshLayout.setPureScrollModeOn();
//        if (bitPath != null){
//            mB.ivImg.setImageBitmap((Bitmap) bitPath);
//        }else {
            GlideLoadingUtils.load(act, path, mB.ivImg);
//        }
        mB.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onConfirm(mB.etText.getText().toString(), path);
            }
        });
    }
}
