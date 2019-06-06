package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.NetworkUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.weight.RoundImageView;

import java.util.List;

/**
 * Created by edison on 2019/1/13.
 */

public class StoryListAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public StoryListAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);

        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + bean.getUrlPic(), viewHolder.ivImg);
        viewHolder.tvTitle.setText(bean.getStoryName());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                    PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                        @Override
                        public void onClick() {
                            MainActivity.isWifi = true;
                            UIHelper.startPlayMusicAct(listBean, position);
                        }
                    });
                }else {
                    UIHelper.startPlayMusicAct(listBean, position);
                }
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_story_list, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RoundImageView ivImg;
        AppCompatTextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }


}
