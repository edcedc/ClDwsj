package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseListViewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.weight.RoundImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/11.
 */

public class AnimalEncyclopediaAdapter extends BaseListViewAdapter<DataBean>{

    public AnimalEncyclopediaAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_animal, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, bean.getImage(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startAnimalDescAct(bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder{

        RoundImageView ivImg;
        AppCompatTextView tvName;

        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvName = convertView.findViewById(R.id.tv_name);
        }
    }

}
