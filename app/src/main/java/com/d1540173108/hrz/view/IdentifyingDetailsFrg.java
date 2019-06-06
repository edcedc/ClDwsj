package com.d1540173108.hrz.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.IdentifyingAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseListContract;
import com.d1540173108.hrz.base.BaseListPresenter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FIdentifyingDescBinding;
import com.d1540173108.hrz.weight.LinearDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/13.
 *  识别细节
 */

public class IdentifyingDetailsFrg extends BaseFragment<BaseListPresenter, FIdentifyingDescBinding> implements BaseListContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private IdentifyingAdapter adapter;
    private String result;
    private String path;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        result = bundle.getString("result");
        path = bundle.getString("path");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_identifying_desc;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.error_correction), R.color.white, R.color.blue_6759AE);
        mB.btSubmit.setOnClickListener(this);
        if (adapter == null) {
            adapter = new IdentifyingAdapter(act, listBean, path);
        }
        setRecyclerViewType(mB.recyclerView);
//        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  20));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.setPureScrollModeOn();

        try {
            JSONObject data = new JSONObject(result);
            JSONArray attachments = data.optJSONArray("attachments");
            if (attachments != null && attachments.length() != 0){
                attachments.remove(0);
                for (int i = 0;i < attachments.length();i++){
                    JSONObject object = attachments.optJSONObject(i);
                    JSONObject content = object.optJSONObject("content");

                    DataBean bean = new DataBean();
                    bean.setImage(content.optString("ImageUrl"));
                    bean.setName(content.optString("Tag"));
                    bean.setProbability(content.optDouble("Probability"));
                    bean.setId(content.optString("TagId"));

                    listBean.add(bean);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit://纠错
                UIHelper.startErrorCorrectionFrg(this, path);
                break;
        }
    }
}
