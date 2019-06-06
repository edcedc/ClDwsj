package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.d1540173108.hrz.bean.HistoryBean;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.DownloadAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.BRecyclerBinding;
import com.d1540173108.hrz.presenter.RecentlyBroadcastPresenter;
import com.d1540173108.hrz.view.impl.RecentlyBroadcastContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 *  最近播放
 */

public class RecentlyBroadcastFrg extends BaseFragment<RecentlyBroadcastPresenter, BRecyclerBinding> implements RecentlyBroadcastContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private DownloadAdapter adapter;
    private TextView topRight;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.b_recycler;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.recently_broadcast), getString(R.string.clear));
        topRight = view.findViewById(R.id.top_right);
        if (adapter == null) {
            adapter = new DownloadAdapter(act, listBean, 3);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }
        });
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        LitePal.deleteAll(HistoryBean.class);
        listBean.clear();
        adapter.notifyDataSetChanged();
        showLoadEmpty();
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
