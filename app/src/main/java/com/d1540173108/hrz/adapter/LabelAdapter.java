package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseListViewAdapter;
import com.d1540173108.hrz.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2018/11/17.
 */

public class LabelAdapter extends BaseListViewAdapter<DataBean> {
    public LabelAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_label, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.tvName.setText(bean.getName());
//        viewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null,
//               act.getResources().getDrawable(bean.getImg(), null), null, null);
        viewHolder.ivImg.setBackgroundResource(bean.getImg());
        return convertView;
    }


    class ViewHolder {

        AppCompatTextView tvName;
        ImageView ivImg;

        public ViewHolder(View convertView) {
            tvName = convertView.findViewById(R.id.tv_name);
            ivImg = convertView.findViewById(R.id.iv_img);
        }
    }

}
