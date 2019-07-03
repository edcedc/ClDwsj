package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.constraint.Group;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.weight.RoundImageView;
import com.flyco.roundview.RoundTextView;

import java.util.List;

/**
 * Created by wb  yyc
 * User: 501807647@qq.com
 * Date: 2019/6/26
 * Time: 12:56
 */
public class KnowledgeAdapter extends BaseRecyclerviewAdapter<DataBean> {

    boolean isaKnowledge = false;

    public KnowledgeAdapter(Context act, BaseFragment root, List<DataBean> listBean, boolean isaKnowledge) {
        super(act, root, listBean);
        this.isaKnowledge = isaKnowledge;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        if (!isaKnowledge){
            GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + "/uploadify/showImage?attachId=" + bean.getKnowledgeImg(), viewHolder.iv_img);
        }else {
            String bannerTitle = bean.getKnowledgeTitle();
            if (!StringUtils.isEmpty(bannerTitle)){
                viewHolder.gp_view.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(bannerTitle);
            }else {
                viewHolder.gp_view.setVisibility(View.GONE);
            }

            GlideLoadingUtils.load(act, CloudApi.HEAD_SERVLET_URL + "/uploadify/showImage?attachId=" + bean.getBannerImg(), viewHolder.iv_img);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isaKnowledge){
                    UIHelper.startKnowledgeFrg(root);
                }else {
                    if (bean.getUseMethod() == 1){
                        UIHelper.startKnowledgeImageFrg(root, bean.getKnowledgeTitle(), bean.getKnowledgeImg());
                    }else {
                        UIHelper.startHtmlAct(bean.getKnowledgeTitle(), bean.getKnowledgeUrl());
                    }

                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_knowledge, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RoundImageView iv_img;
        Group gp_view;
        AppCompatTextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_img);
            gp_view = itemView.findViewById(R.id.gp_view);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

}
