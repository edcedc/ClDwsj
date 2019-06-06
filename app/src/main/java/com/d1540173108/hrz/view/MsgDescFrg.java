package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.FMsgDescBinding;

/**
 * Created by edison on 2019/1/16.
 *  消息详情
 */

public class MsgDescFrg extends BaseFragment<BasePresenter, FMsgDescBinding>{

    private DataBean bean;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_msg_desc;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.msg_desc));
        mB.refreshLayout.setPureScrollModeOn();
        mB.tvTitle.setText(bean.getTitle());
        mB.tvTime.setText(bean.getCreateTime());
        mB.tvContent.setText(bean.getContent());
    }
}
