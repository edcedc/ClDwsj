package com.d1540173108.hrz.view.act;

import android.content.Intent;
import android.os.Bundle;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.view.AnimalEncyclopediaFrg;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by edison on 2019/1/11.
 *  动物世界
 */

public class AnimalEncyclopediaAct extends BaseActivity{
    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected void initView() {
        setSofia(true);
        if (findFragment(AnimalEncyclopediaFrg.class) == null) {
            loadRootFragment(R.id.fl_container, AnimalEncyclopediaFrg.newInstance());
        }
    }


}
