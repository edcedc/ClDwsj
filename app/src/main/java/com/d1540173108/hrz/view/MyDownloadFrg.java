package com.d1540173108.hrz.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.bean.DownloadBean;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.DownloadAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.databinding.BRecyclerBinding;
import com.d1540173108.hrz.presenter.MyDownloadPresenter;
import com.d1540173108.hrz.view.impl.MyDownloadContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 * 我的下载
 */

public class MyDownloadFrg extends BaseFragment<MyDownloadPresenter, BRecyclerBinding> implements MyDownloadContract.View {

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
        setTitle(getString(R.string.my_download), getString(R.string.edit));
        topRight = view.findViewById(R.id.top_right);
        if (adapter == null) {
            adapter = new DownloadAdapter(act, listBean, 1);
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
        String s = topRight.getText().toString();
        if (s.equals(getString(R.string.edit))) {
            setTitle(getString(R.string.my_download), getString(R.string.clear));
            adapter.setEdit(true);
        } else {
            List<DataBean> temp = new ArrayList<>();
            for (int i = 0; i < listBean.size(); i++) {
                DataBean bean = listBean.get(i);
                if (bean.isSelect()){
                    temp.add(bean);
                    MainActivity.mMyBinder.pauseMusic();
                    FileUtils.deleteFile(bean.getUrlMp3());
                }
            }
            listBean.removeAll(temp);
            setTitle(getString(R.string.my_download), getString(R.string.edit));
            adapter.setEdit(false);
        }
        adapter.notifyDataSetChanged();
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
