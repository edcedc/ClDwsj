package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.FindAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FMainBinding;
import com.d1540173108.hrz.event.FindInEvent;
import com.d1540173108.hrz.weight.buttonBar.BottomBar;
import com.d1540173108.hrz.weight.buttonBar.BottomBarTab;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by edison on 2019/1/10.
 */

public class MainFrg extends BaseFragment<BasePresenter, FMainBinding> implements IBaseView {

    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private SupportFragment[] mFragments = new SupportFragment[3];

    private FindAdapter adapter;


    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_main;
    }

    @Override
    protected void initView(View view) {
        mB.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.y3, "首页"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.y1,"发现"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.icon_wd,"我的"));
        mB.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (position == SECOND){
                    EventBus.getDefault().post(new FindInEvent());
//                    new FindBottomFrg().show(getChildFragmentManager(), "dialog");
                }else {
                    mB.lyBottom.setVisibility(View.GONE);
                    showHideFragment(mFragments[position], mFragments[prePosition]);
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
                if (position == SECOND){
                    EventBus.getDefault().post(new FindInEvent());
                }
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
        mB.bottomBar.setCurrentItem(0);

        EventBus.getDefault().register(this);




        String[] laberStr = {act.getString(R.string.zking),act.getString(R.string.chang_long),
                act.getString(R.string.animal_encyclopedia)};
        int[] laberImg = {R.mipmap.icon_sys, R.mipmap.icon_cldw, R.mipmap.icon_dwbk};
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
        mB.gridView.setAdapter(adapter);
        mB.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mB.lyBottom.setVisibility(View.GONE);
                switch (i){
                    case 0:
                        UIHelper.startArAct(act, 1);
                        break;
                    case 1:
                        UIHelper.startCLAnimalAct();
                        break;
                    case 2:
                        UIHelper.startAnimalEncyclopediaAct();
                        break;
                }
            }
        });

        mB.lyBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mB.lyBottom.setVisibility(View.GONE);
            }
        });

    }

    @Subscribe
    public void onMianThreadInEvent(FindInEvent event){
        if (mB.lyBottom.getVisibility() == View.GONE){
            mB.lyBottom.setVisibility(View.VISIBLE);
        }else {
            mB.lyBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(HomeFrg.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFrg.newInstance();
            mFragments[THIRD] = MeFrg.newInstance();

            loadMultipleRootFragment(R.id.fl_container,
                    FIRST,
                    mFragments[FIRST],
                    mFragments[THIRD]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[THIRD] = findChildFragment(MeFrg.class);
        }
        setSwipeBackEnable(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        setSofia(false);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

    @Override
    public boolean onBackPressedSupport() {
        exitBy2Click();// 调用双击退出函数
//        return super.onBackPressedSupport();
        return true;
    }

    private Boolean isExit = false;

    private void exitBy2Click() {
        Handler tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            showToast("再按一次退出程序");
            tExit = new Handler();
            tExit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            return;
        } else {
//            Cockroach.uninstall();
            MobclickAgent.onProfileSignOff();
            act.finish();
            System.exit(0);
        }
    }
}
