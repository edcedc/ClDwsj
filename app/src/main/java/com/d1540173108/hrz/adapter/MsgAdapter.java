package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public class MsgAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public MsgAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        viewHolder.tvTitle.setText(bean.getTitle());
        viewHolder.tvContent.setVisibility(View.INVISIBLE);
        viewHolder.tvTime.setText(bean.getCreateTime());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startHtmlAct(bean.getTitle(), "http://chimelong.powert.top/h5/#/login?id=" + bean.getId());
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_msg, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView tvTitle;
        AppCompatTextView tvContent;
        AppCompatTextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
