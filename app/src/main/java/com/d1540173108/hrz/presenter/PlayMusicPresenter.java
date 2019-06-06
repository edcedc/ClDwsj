package com.d1540173108.hrz.presenter;

import com.d1540173108.hrz.MainActivity;
import com.d1540173108.hrz.bean.BaseListBean;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.HistoryBean;
import com.d1540173108.hrz.bean.SaveMusicListBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.utils.FileSaveUtils;
import com.d1540173108.hrz.view.impl.PlayMusicContract;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/14.
 */

public class PlayMusicPresenter extends PlayMusicContract.Presenter{
    @Override
    public void onSavePlayNum(String storyId, int type) {
        CloudApi.storySavePlayNum(storyId, type)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject jsonObject) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void onColletion(String storyId, int type, boolean isColletion) {
        if(isColletion){
            CloudApi.storyDelPlayNum(storyId)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                            if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                mView.onIsColletion(false);
                            }
                            showToast(baseResponseBeanResponse.body().message);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }else {
            CloudApi.storySavePlayNum(storyId, type)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            mView.showLoading();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JSONObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mView.addDisposable(d);
                        }

                        @Override
                        public void onNext(JSONObject jsonObject) {
                            if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                                mView.onIsColletion(true);
                            }
                            showToast("收藏成功");
                        }

                        @Override
                        public void onError(Throwable e) {
                            mView.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            mView.hideLoading();
                        }
                    });
        }

    }

    @Override
    public void onSetData(List<DataBean> listBean, int position) {
        if (listBean != null){
            LitePal.deleteAll(SaveMusicListBean.class);
            for (int i = 0;i < listBean.size();i++){
                DataBean bean = listBean.get(i);

                SaveMusicListBean music = new SaveMusicListBean();
                music.setStoryId(bean.getStoryId());
                music.setStoryName(bean.getStoryName());
                music.setMusicTips(bean.getMusicTips());
                music.setUrlMp3(bean.getUrlMp3());
                music.setDownMp3(bean.getDownMp3());
                music.setUrlPic(bean.getUrlPic());
                music.setTime(bean.getTime());
                music.save();

                if (i == position){
                    HistoryBean history = new HistoryBean();
                    history.setStoryId(bean.getStoryId());
                    history.setStoryName(bean.getStoryName());
                    history.setMusicTips(bean.getMusicTips());
                    history.setUrlMp3(bean.getUrlMp3());
                    history.setDownMp3(bean.getDownMp3());
                    history.setUrlPic(bean.getUrlPic());
                    history.setTime(bean.getTime());
                    history.save();
                }
            }
            List<SaveMusicListBean> all = LitePal.findAll(SaveMusicListBean.class);
        }
        mView.onData();
    }

    @Override
    public void onDow(List<DataBean> listBean) {
        if (listBean != null){
            showToast("下载中");
            DataBean bean = listBean.get(MainActivity.mPos);
            onSavePlayNum(bean.getStoryId(), 2);
            FileSaveUtils.saveVideo(act, CloudApi.IMAGE_SERVLET_URL + bean.getDownMp3(), bean.getStoryName());

//            DownloadBean download = new DownloadBean();
//            download.setStoryId(bean.getStoryId());
//            download.setStoryName(bean.getStoryName());
//            download.setMusicTips(bean.getMusicTips());
//            download.setUrlMp3(bean.getUrlMp3());
//            download.setDownMp3(bean.getDownMp3());
//            download.setUrlPic(bean.getUrlPic());
//            download.save();
        }else {
            showToast("下载失败");
        }
    }

    @Override
    public void onIsColletion(final String storyId) {
        CloudApi.storyGetStoryList(1, Constants.pageSize)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getRows();
                                if (list != null && list.size() != 0){
                                    for (DataBean bean : list){
                                        if (bean.getStoryId().equals(storyId)){
                                            mView.onIsColletion(true);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }



}
