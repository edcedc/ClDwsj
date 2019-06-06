package com.d1540173108.hrz.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FClAnimalBinding;
import com.d1540173108.hrz.event.CameraInEvent;
import com.d1540173108.hrz.weight.PictureSelectorTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/14.
 *  长隆动物
 */

public class CLAnimalFrg extends BaseFragment<BasePresenter, FClAnimalBinding> implements View.OnClickListener{

    public static CLAnimalFrg newInstance() {
        Bundle args = new Bundle();
        CLAnimalFrg fragment = new CLAnimalFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<LocalMedia> localMediaList = new ArrayList<>();

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_cl_animal;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.refreshLayout.setPureScrollModeOn();
        mB.lyDistinguish.setOnClickListener(this);
        mB.ivAnimalEncyclopedia.setOnClickListener(this);
        mB.ivGallery.setOnClickListener(this);
        mB.ivSet.setOnClickListener(this);
        mB.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.finish();
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(CameraInEvent event) {
        if (event.getRequest() == CameraInEvent.HEAD_DISTINGUISH_CAMEAR || event.getRequest() == CameraInEvent.HEAD_DISTINGUISH_PHOTO){
            localMediaList.clear();
            localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
            final String path = localMediaList.get(0).getCutPath();
            UIHelper.startIdentifyingFrg(CLAnimalFrg.this, path);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_gallery://图库
                PictureSelectorTool.PictureSelectorImage(act, CameraInEvent.HEAD_DISTINGUISH_CAMEAR, true, false);
                break;
            case R.id.iv_animal_encyclopedia://动物百科
                UIHelper.startAnimalEncyclopediaAct();
                break;
            case R.id.iv_set://设置

                break;
            case R.id.ly_distinguish://开始识别
                PictureSelectorTool.photo(act, CameraInEvent.HEAD_DISTINGUISH_PHOTO, true, false);
                break;
        }
    }
}
