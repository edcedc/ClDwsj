package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.d1540173108.hrz.base.BaseListViewAdapter;
import com.d1540173108.hrz.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public class ListLabelAdapter extends BaseListViewAdapter<DataBean>{
    public ListLabelAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
