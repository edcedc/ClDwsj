package com.d1540173108.hrz.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseListViewAdapter;
import com.d1540173108.hrz.bean.DataBean;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public class AnimalDescAdapter extends BaseListViewAdapter<DataBean>{
    public AnimalDescAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }
    private boolean isLook = false;

    public void setLook(boolean look) {
        isLook = look;
    }

    public boolean isLook() {
        return isLook;
    }

    @Override
    public int getCount() {
        if (isLook){
            return listBean.size();
        }else {
            return listBean.size() >= 3 ? 3 : listBean.size();
        }
    }
    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_animal_desc, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvContent.setText(bean.getContent());
        return convertView;
    }

    class ViewHolder{

        AppCompatTextView tvTitle;
        AppCompatTextView tvContent;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View convertView) {
            tvTitle = convertView.findViewById(R.id.tv_title);
            tvContent = convertView.findViewById(R.id.tv_content);
        }
    }

}
