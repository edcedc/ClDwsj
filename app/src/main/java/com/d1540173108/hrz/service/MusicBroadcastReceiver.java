package com.d1540173108.hrz.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.R;

/**
 * Created by edison on 2019/3/7.
 */

public class MusicBroadcastReceiver  extends BroadcastReceiver {

    MusicBroadcastReceiver mbr = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean playing = intent.getBooleanExtra("playing",false);
        LogUtils.e(playing);
    }

}
