package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.weight.RoundImageView;
import com.d1540173108.hrz.weight.ShangshabanRotateTextView;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public class IdentifyingAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public IdentifyingAdapter(Context act, List<DataBean> listBean, String path) {
        super(act, listBean);
        this.path = path;
    }

    private String path;

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, bean.getImage(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());

        int probability = (int) bean.getProbability();
        if (probability < 40){
            viewHolder.tvContent.setTextColor(act.getColor(R.color.reb_FE4D49));
        }else if (probability > 80){
            viewHolder.tvContent.setTextColor(act.getColor(R.color.blue_765BE7));
        }else {//小于90 大于30
            viewHolder.tvContent.setTextColor(act.getColor(R.color.blue_FE84607));
        }
        viewHolder.tvContent.setText(bean.getProbability() + "%");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startAnimalDescAct(bean.getId());
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_identifying, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundImageView ivImg;
        AppCompatTextView tvName;
        AppCompatTextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

}
