package com.d1540173108.hrz.view.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.view.AnimalDescFrg;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by edison on 2019/1/11.
 *  动物详情
 */

public class AnimalDescAct extends BaseActivity {

    private String id;
    private String path;

    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        path = bundle.getString("path");
    }

    @Override
    protected void initView() {
        setSofia(true);
        AnimalDescFrg frg = AnimalDescFrg.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("path", path);
        frg.setArguments(bundle);
        if (findFragment(AnimalDescFrg.class) == null) {
            loadRootFragment(R.id.fl_container, frg);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

}
