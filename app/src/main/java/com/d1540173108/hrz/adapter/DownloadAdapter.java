package com.d1540173108.hrz.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.BuildConfig;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.weight.RoundImageView;

import java.io.File;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * Created by edison on 2019/1/10.
 */

public class DownloadAdapter extends BaseRecyclerviewAdapter<DataBean>{


    public DownloadAdapter(Context act, List<DataBean> listBean, int type) {
        super(act, listBean);
        this.type = type;
    }

    private int type;

    private boolean isEdit = false;

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.cbSubmit.setVisibility(isEdit == true ? View.VISIBLE : View.GONE);
        String urlPic = bean.getUrlPic();
        if (StringUtils.isEmpty(urlPic)){
            viewHolder.ivImg.setVisibility(View.GONE);
        }else {
            viewHolder.ivImg.setVisibility(View.VISIBLE);
            GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + urlPic, viewHolder.ivImg);
        }
        viewHolder.tvTime.setText(bean.getTime());
        viewHolder.tvName.setText(bean.getStoryName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1){
                    MainActivity.mPos = position;
                    MainActivity.mMyBinder.playLocal(bean.getUrlMp3());
                }else {
                    if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                        PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                            @Override
                            public void onClick() {
                                MainActivity.isWifi = true;
                                UIHelper.startPlayMusicAct(listBean, position);
                            }
                        });
                    }
                }
            }
        });
        viewHolder.cbSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setSelect(viewHolder.cbSubmit.isChecked());
            }
        });
    }

    private onClickListener listener;
    public void setOnClickListener(onClickListener listener){
        this.listener = listener;
    }
    public interface onClickListener{
        void onClick(int position, boolean isChecked);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_download, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbSubmit;
        RoundImageView ivImg;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            cbSubmit = itemView.findViewById(R.id.cb_submit);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
