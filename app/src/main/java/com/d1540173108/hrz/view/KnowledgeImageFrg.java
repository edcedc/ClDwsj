package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.FKnowledgeImageBinding;
import com.d1540173108.hrz.utils.GlideLoadingUtils;

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

    @Override
    protected void initView(View view) {
        setTitle(title);
        GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + "/uploadify/showImage?attachId=" + id, mB.ivImg);
    }
}
