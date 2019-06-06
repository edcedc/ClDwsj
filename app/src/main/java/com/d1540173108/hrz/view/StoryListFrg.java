package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;

import com.d1540173108.hrz.controller.CloudApi;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.StoryListAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseListContract;
import com.d1540173108.hrz.base.BaseListPresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.FStoryBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/13.
 *  故事列表
 */

public class StoryListFrg extends BaseFragment<BaseListPresenter, FStoryBinding> implements BaseListContract.View {

    private List<DataBean> listBean = new ArrayList<>();
    private StoryListAdapter adapter;
    private String title;
    private int position;
    private String url;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        title = bundle.getString("title");
        position = bundle.getInt("position");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_story;
    }

    @Override
    protected void initView(View view) {
        setTitle(title);
        if (adapter == null) {
            adapter = new StoryListAdapter(act, listBean);
        }
        setRecyclerViewGridType(mB.recyclerView, 2, 25, R.color.white);
        mB.recyclerView.setAdapter(adapter);

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.storyGetStoryList, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(CloudApi.storyGetStoryList, pagerNumber += 1);
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
