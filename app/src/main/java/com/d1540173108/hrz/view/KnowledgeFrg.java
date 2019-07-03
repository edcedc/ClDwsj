package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.KnowledgeAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseListContract;
import com.d1540173108.hrz.base.BaseListPresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.databinding.BRecyclerBinding;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wb  yyc
 * User: 501807647@qq.com
 * Date: 2019/6/26
 * Time: 14:36
 */
public class KnowledgeFrg extends BaseFragment<BaseListPresenter, BRecyclerBinding> implements BaseListContract.View {

    public static KnowledgeFrg newInstance() {
        Bundle args = new Bundle();
        KnowledgeFrg fragment = new KnowledgeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    private List<DataBean> listBean = new ArrayList<>();
    private KnowledgeAdapter adapter;

    @Override
    protected void initParms(Bundle bundle) {
    }

    @Override
    protected int bindLayout() {
        return R.layout.b_recycler;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.knowledge));

        if (adapter == null){
            adapter = new KnowledgeAdapter(act, this, listBean, true);
        }
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(mB .recyclerView.getContext(), RecyclerView.VERTICAL, false));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setNestedScrollingEnabled(false);
        mB.recyclerView.setAdapter(adapter);

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.knowledgeGetKnowledgeList, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(CloudApi.knowledgeGetKnowledgeList, pagerNumber += 1);
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
