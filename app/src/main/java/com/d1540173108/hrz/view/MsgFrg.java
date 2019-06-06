package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.d1540173108.hrz.controller.CloudApi;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.MsgAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseListContract;
import com.d1540173108.hrz.base.BaseListPresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.BRecyclerBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/13.
 *  消息中心
 */

public class MsgFrg extends BaseFragment<BaseListPresenter, BRecyclerBinding> implements BaseListContract.View {

    private List<DataBean> listBean = new ArrayList<>();
    private MsgAdapter adapter;

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
        setTitle(getString(R.string.msg));
        if (adapter == null) {
            adapter = new MsgAdapter(act, this, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.messageGetMsgList, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(CloudApi.messageGetMsgList, pagerNumber += 1);
            }
        });
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
