package com.d1540173108.hrz.view.act;

import android.os.Bundle;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;

/**
 * Created by edison on 2019/1/27.
 */

public class CeShiAct extends BaseActivity{
    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.a_ceshi;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void initView() {
        setTitle("频道升级中");
    }
}
