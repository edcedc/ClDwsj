package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.FImgBinding;
import com.d1540173108.hrz.utils.GlideLoadingUtils;

/**
 * Created by edison on 2019/1/15.
 *  图片
 */

public class PlayMusicImgFrg extends BaseFragment<BasePresenter, FImgBinding> {

    private String image;

    public void setImage(String image) {
        LogUtils.e(CloudApi.IMAGE_SERVLET_URL + image);
        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + image, mB.ivImg);
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        image = bundle.getString("image");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_img;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + image, mB.ivImg);
    }
}
