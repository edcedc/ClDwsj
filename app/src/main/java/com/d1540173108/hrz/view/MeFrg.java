package com.d1540173108.hrz.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.event.LoginInEvent;
import com.d1540173108.hrz.event.LoginSuccessInEvent;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FMeBinding;
import com.d1540173108.hrz.event.CameraInEvent;
import com.d1540173108.hrz.presenter.MePresenter;
import com.d1540173108.hrz.view.bottomFrg.CameraBottomFrg;
import com.d1540173108.hrz.view.impl.MeContract;
import com.d1540173108.hrz.weight.PictureSelectorTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 *  发现
 */

public class MeFrg extends BaseFragment<MePresenter, FMeBinding> implements MeContract.View, View.OnClickListener{

    public static MeFrg newInstance() {
        Bundle args = new Bundle();
        MeFrg fragment = new MeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private CameraBottomFrg cameraBottomFrg;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private boolean isRequest = true;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_me;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        mB.refreshLayout.setPureScrollModeOn();
        mPresenter.onInit(mB.gridView, this);
        mB.ivHead.setOnClickListener(this);
        mB.ly.setOnClickListener(this);

        if (cameraBottomFrg == null){
            cameraBottomFrg = new CameraBottomFrg();
        }
        cameraBottomFrg.setCameraListener(new CameraBottomFrg.onCameraListener() {
            @Override
            public void camera() {
                PictureSelectorTool.PictureSelectorImage(act, CameraInEvent.HEAD_CAMEAR, true);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }

            @Override
            public void photo() {
                PictureSelectorTool.photo(act, CameraInEvent.HEAD_PHOTO, true);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }
        });
        EventBus.getDefault().register(this);
        mPresenter.onRequest();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(CameraInEvent event) {
        if (event.getRequest() == CameraInEvent.HEAD_CAMEAR || event.getRequest() == CameraInEvent.HEAD_PHOTO){
            if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            localMediaList.clear();
            localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
            String path = localMediaList.get(0).getCompressPath();
            Glide.with(act).load(path).into(mB.ivHead);
            mPresenter.onUpdateHead(path);
        }
    }

    @Subscribe
    public void onLoginInEvent(LoginInEvent event){
        mPresenter.onRequest();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
        if (User.getInstance().isLogin() && isRequest){
            mPresenter.onRequest();
            isRequest = false;
        }
        setData(User.getInstance().getUserInfoObj());
    }

    @Override
    public void onClick(View view) {
        if (!User.getInstance().isLogin()){
            UIHelper.startLoginAct();
            return;
        }
        switch (view.getId()){
            case R.id.iv_head:
                cameraBottomFrg.show(getChildFragmentManager(), "dialog");
                break;
            case R.id.ly:
                UIHelper.startMyInfoFrg(this);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setData(JSONObject data) {
        if (data == null)return;
        mB.tvName.setText(data.optString("userNickName"));
        if (data.optString("sex").equals("女")){
            mB.tvName.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, act.getResources().getDrawable(R.mipmap.y2, null), null);
        }else {
            mB.tvName.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, act.getResources().getDrawable(R.mipmap.boy_1, null), null);
        }
        String birthDate = data.optString("birthDate");
        if (!birthDate.equals("null")){
            String[] split = birthDate.split(" ");
            mB.tvTime.setText(split[0]);
        }
        String url = data.optString("url");
        if (url.startsWith("http")){
            GlideLoadingUtils.load(act, url, mB.ivHead);
            EventBus.getDefault().post(new LoginSuccessInEvent(data.optString("userNickName"), url));
        }else {
            if (data.optString("userNickName").equals("null")){
                EventBus.getDefault().post(new LoginSuccessInEvent("小神奇", CloudApi.HEAD_SERVLET_URL + data.optString("url") + "&" + (float) (Math.random() * 100)));
            }else {
                EventBus.getDefault().post(new LoginSuccessInEvent(data.optString("userNickName"), CloudApi.HEAD_SERVLET_URL + data.optString("url") + "&" + (float) (Math.random() * 100)));
            }
            GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + url + "&" + (float) (Math.random() * 100), mB.ivHead);
        }
    }


    @Override
    public void setHead(String message) {
        GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + message + "&" + (float) (Math.random() * 100), mB.ivHead);
    }
}
