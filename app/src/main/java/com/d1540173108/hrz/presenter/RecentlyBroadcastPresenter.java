package com.d1540173108.hrz.presenter;

import android.os.Handler;

import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.HistoryBean;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.view.impl.RecentlyBroadcastContract;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/10.
 */

public class RecentlyBroadcastPresenter extends RecentlyBroadcastContract.Presenter {
    @Override
    public void onRequest(int pageNumber) {
        List<DataBean> listBean = new ArrayList<>();
        List<HistoryBean> all = LitePal.findAll(HistoryBean.class);
        if (all.size() != 0) {
            for (HistoryBean bean : all) {
                DataBean dataBean = new DataBean();
                dataBean.setStoryId(bean.getStoryId());
                dataBean.setStoryName(bean.getStoryName());
                dataBean.setMusicTips(bean.getMusicTips());
                dataBean.setUrlMp3(bean.getUrlMp3());
                dataBean.setDownMp3(bean.getDownMp3());
                dataBean.setUrlPic(bean.getUrlPic());
                dataBean.setTime(bean.getTime());
                listBean.add(dataBean);
            }
            List list = removeDuplicate(listBean);
            mView.setData(list);
            mView.hideLoading();
        } else {
            mView.showLoadEmpty();
        }
    }


    public static List removeDuplicate(List<DataBean> brandList) {
        for (int i = 0; i < brandList.size() - 1; i++) {
            for (int j = brandList.size() - 1; j > i; j--) {
                if (brandList.get(j).getStoryId().equals(brandList.get(i).getStoryId())) {
                    brandList.remove(j);
                }
            }
        }
        return brandList;
    }

}
