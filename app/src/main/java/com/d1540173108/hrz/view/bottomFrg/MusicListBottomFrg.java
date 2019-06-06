package com.d1540173108.hrz.view.bottomFrg;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.MusicListAdapter;
import com.d1540173108.hrz.base.BaseBottomSheetFrag;
import com.d1540173108.hrz.bean.Common;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.Music;
import com.d1540173108.hrz.bean.SaveMusicListBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/16.
 *  播放列表
 */

public class MusicListBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener{

    private ImageView ivImg;
    private AppCompatTextView tvType;
    private AppCompatTextView tvSize;
    private RecyclerView recyclerView;

    @Override
    public void onClick(View view) {

    }

    @Override
    public int bindLayout() {
        return R.layout.p_music_list;
    }

    @Override
    public void initView(View view) {
        ivImg = view.findViewById(R.id.iv_img);
        tvType = view.findViewById(R.id.tv_type);
        tvSize = view.findViewById(R.id.tv_size);
        recyclerView = view.findViewById(R.id.recyclerView);
        view.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        MusicListAdapter adapter = new MusicListAdapter(act, LitePal.findAll(SaveMusicListBean.class));
        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        tvSize.setText("当前有" + LitePal.findAll(SaveMusicListBean.class).size() + "个故事");

        if (MainActivity.mMyBinder.getPlayMode() == 1){
            ivImg.setBackgroundResource(R.mipmap.icon_xh);
            tvType.setText("单曲播放");
        }else if (MainActivity.mMyBinder.getPlayMode() == 2){
            ivImg.setBackgroundResource(R.mipmap.icon_sjbf);
            tvType.setText("随机播放");
        }else {
            ivImg.setBackgroundResource(R.mipmap.icon_sx);
            tvType.setText("列表循环");
        }
    }


}
