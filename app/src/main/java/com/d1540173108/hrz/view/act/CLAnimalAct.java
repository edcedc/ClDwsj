package com.d1540173108.hrz.view.act;

import android.content.Intent;
import android.os.Bundle;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.event.CameraInEvent;
import com.d1540173108.hrz.view.CLAnimalFrg;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by edison on 2019/1/14.
 *  长隆动物
 */

public class CLAnimalAct extends BaseActivity {

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
        if (findFragment(CLAnimalFrg.class) == null) {
            loadRootFragment(R.id.fl_container, CLAnimalFrg.newInstance());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(new CameraInEvent(requestCode, data));
        }
    }

}
