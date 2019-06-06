package com.d1540173108.hrz.view.bottomFrg;

import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.FindAdapter;
import com.d1540173108.hrz.base.BaseBottomSheetFrag;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.weight.WithScrollGridView;
import com.d1540173108.hrz.weight.buttonBar.BottomBar;
import com.d1540173108.hrz.weight.buttonBar.BottomBarTab;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2019/1/11.
 *  发现
 */

public class FindBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener{

    private WithScrollGridView gridView;
    private BottomBar bottomBar;

    private FindAdapter adapter;
    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private SupportFragment[] mFragments = new SupportFragment[3];

    @Override
    public void onClick(View view) {

    }

    @Override
    public int bindLayout() {
        return R.layout.f_find;
    }

    @Override
    public void initView(View view) {
        gridView = view.findViewById(R.id.gridView);
        bottomBar = view.findViewById(R.id.bottomBar);
        String[] laberStr = {act.getString(R.string.chang_long), act.getString(R.string.zking),
                act.getString(R.string.animal_encyclopedia)};
        int[] laberImg = {R.mipmap.icon_cldw, R.mipmap.icon_sys, R.mipmap.icon_dwbk};
        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0; i < laberStr.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(laberStr[i]);
            bean.setImg(laberImg[i]);
            listBean.add(bean);
        }
        if (adapter == null){
            adapter = new FindAdapter(act, listBean);
        }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }
        });

        bottomBar
                .addItem(new BottomBarTab(act, R.mipmap.y3, "首页"))
                .addItem(new BottomBarTab(act, R.mipmap.icon_fx,"发现"))
                .addItem(new BottomBarTab(act, R.mipmap.icon_wd,"我的"));
        bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                LogUtils.e("onTabSelected");
            }

            @Override
            public void onTabUnselected(int position) {
                LogUtils.e("onTabUnselected");
            }

            @Override
            public void onTabReselected(int position) {
                LogUtils.e("onTabReselected");
            }
        });
        bottomBar.setCurrentItem(0);
    }
}
