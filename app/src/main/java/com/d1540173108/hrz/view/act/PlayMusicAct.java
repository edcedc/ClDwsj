package com.d1540173108.hrz.view.act;

import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.view.PlayMusicFrg;

/**
 * Created by edison on 2019/1/14.
 *  播放器页面
 */

public class PlayMusicAct extends BaseActivity {

    private String list;
    private int position;

    @Override
    public void initPresenter() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initParms(Bundle bundle) {
        list = bundle.getString("list");
        position = bundle.getInt("position");
    }

    @Override
    protected void initView() {
        setSofia(true);
        if (findFragment(PlayMusicFrg.class) == null) {
            PlayMusicFrg frg = PlayMusicFrg.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("list", list);
            bundle.putInt("position", position);
            frg.setArguments(bundle);
            loadRootFragment(R.id.fl_container, frg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.isArAct && MainActivity.mMyBinder != null){
            MainActivity.mMyBinder.pauseMusic();
        }
    }
}
