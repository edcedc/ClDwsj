package com.d1540173108.hrz.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.FKnowledgeImageBinding;

/**
 * Created by wb  yyc
 * User: 501807647@qq.com
 * Date: 2019/6/26
 * Time: 14:52
 */
public class KnowledgeImageFrg extends BaseFragment<BasePresenter, FKnowledgeImageBinding> {

    private String title;
    private String id;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        title = bundle.getString("title");
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_knowledge_image;
    }

    private final int MAX_WIDTH = 1000;//图片的最大宽度
    private final int MAX_HEIGHT = 500;//图片的最大高度

    @Override
    protected void initView(View view) {
        setTitle(title);
//        GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + "/uploadify/showImage?attachId=" + id, mB.ivImg);
        final String s = CloudApi.HEAD_SERVLET_URL + "/uploadify/showImage?attachId=" + id;
        LogUtils.e(s);

        Glide.with(this)
                .asBitmap()
                .load(s)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mB.ivImg.setImageBitmap(resource);
                    }
                });


    }
}
