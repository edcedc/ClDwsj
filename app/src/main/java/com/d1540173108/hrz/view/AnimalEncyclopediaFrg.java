package com.d1540173108.hrz.view;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.AnimalEncyclopediaAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FAnimalEncyclopediaBinding;
import com.d1540173108.hrz.presenter.AnimalEncyclopediaPresenter;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.view.impl.AnimalEncyclopediaContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/11.
 *  动物世界
 */

public class AnimalEncyclopediaFrg extends BaseFragment<AnimalEncyclopediaPresenter, FAnimalEncyclopediaBinding> implements AnimalEncyclopediaContract.View, View.OnClickListener{

    private String id;

    public static AnimalEncyclopediaFrg newInstance() {
        Bundle args = new Bundle();
        AnimalEncyclopediaFrg fragment = new AnimalEncyclopediaFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private AnimalEncyclopediaAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_animal_encyclopedia;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.animal_encyclopedia), R.color.white, R.color.blue_6759AE);
        mB.tvSearch.setOnClickListener(this);
        mB.lyAdinmlDesc.setOnClickListener(this);
        if (adapter == null){
            adapter = new AnimalEncyclopediaAdapter(act, listBean);
        }
        mB.gridView.setAdapter(adapter);

        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.onRequest(pagerNumber = 1);
            }
        });
        mB.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    mPresenter.onSearch(mB.etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
   }

    @Override
    public void onError(Throwable e) {
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

//        DataBean bean = list.get(0);
//        id = bean.getId();
//        mPresenter.onInfo(bean.getId());
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search:
                mPresenter.onSearch(mB.etSearch.getText().toString());
//                mB.tvNoData.setVisibility(View.VISIBLE);
//                mB.lySearch.setVisibility(View.GONE);
                break;
            case R.id.ly_adinml_desc:
                UIHelper.startAnimalDescAct(id);
                break;
        }
    }

    @Override
    public void setInto(JSONObject jsonObject) {
        mB.lyAdinmlDesc.setVisibility(View.VISIBLE);
        id = jsonObject.optString("tagId");
        mB.tvName.setText(jsonObject.optString("tag"));
        mB.tvContent.setText(jsonObject.optString("intro"));
        GlideLoadingUtils.load(act, jsonObject.optString("imageUrl"), mB.ivImg);
    }

    @Override
    public void setSend(JSONArray jsonArray) {

    }
}
