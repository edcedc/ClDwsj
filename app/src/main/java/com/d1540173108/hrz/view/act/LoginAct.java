package com.d1540173108.hrz.view.act;

import android.content.Intent;
import android.os.Bundle;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.view.LoginFrg;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by edison on 2019/1/9.
 */

public class LoginAct extends BaseActivity {

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
        if (findFragment(LoginFrg.class) == null) {
            loadRootFragment(R.id.fl_container, LoginFrg.newInstance());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
