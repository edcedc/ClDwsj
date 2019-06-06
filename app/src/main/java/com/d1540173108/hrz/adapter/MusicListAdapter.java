package com.d1540173108.hrz.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseRecyclerviewAdapter;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.Music;
import com.d1540173108.hrz.bean.SaveMusicListBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.event.AppointMusicInEvent;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.weight.RoundImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by edison on 2019/1/10.
 */

public class MusicListAdapter extends BaseRecyclerviewAdapter<SaveMusicListBean>{
    public MusicListAdapter(Context act, List<SaveMusicListBean> listBean) {
        super(act, listBean);
    }

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
        final SaveMusicListBean bean = listBean.get(position);
        viewHolder.tvName.setText(bean.getStoryName());
        GlideLoadingUtils.load(act, CloudApi.IMAGE_SERVLET_URL + bean.getUrlPic(), viewHolder.ivImg);
        viewHolder.tvTime.setText(bean.getTime());

        viewHolder.cbSubmit.setVisibility(isEdit() == true ? View.VISIBLE : View.GONE);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isMobileData() && !MainActivity.isWifi){
                    PopupWindowTool.showDialog(act, PopupWindowTool.wifi, new PopupWindowTool.DialogListener() {
                        @Override
                        public void onClick() {
                            MainActivity.isWifi = true;
                            EventBus.getDefault().post(new AppointMusicInEvent(position));
                        }
                    });
                }else {
                    EventBus.getDefault().post(new AppointMusicInEvent(position));
                }
                LogUtils.e("AppointMusicInEvent");
            }
        });

        viewHolder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_music_list, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbSubmit;
        RoundImageView ivImg;
        AppCompatTextView tvName;
        AppCompatTextView tvTime;
        ImageView ivDel;

        public ViewHolder(View itemView) {
            super(itemView);
            cbSubmit = itemView.findViewById(R.id.cb_submit);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivDel = itemView.findViewById(R.id.iv_del);
        }
    }

}
