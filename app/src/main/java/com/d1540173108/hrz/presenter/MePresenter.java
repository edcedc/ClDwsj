package com.d1540173108.hrz.presenter;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.CleanUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.adapter.LabelAdapter;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.bean.DownloadBean;
import com.d1540173108.hrz.bean.HistoryBean;
import com.d1540173108.hrz.callback.Code;
import com.d1540173108.hrz.controller.CloudApi;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.view.impl.MeContract;
import com.d1540173108.hrz.weight.WithScrollGridView;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by edison on 2019/1/10.
 */

public class MePresenter extends MeContract.Presenter{

    @Override
    public void onInit(WithScrollGridView gridView, final BaseFragment root) {
        String[] laberStr = {act.getString(R.string.stories_collection), act.getString(R.string.recently_broadcast),
                act.getString(R.string.my_download), act.getString(R.string.magical_animals), act.getString(R.string.clear_cache), act.getString(R.string.feedback)};
        int[] laberImg = {R.mipmap.y7, R.mipmap.y8, R.mipmap.y9, R.mipmap.y12, R.mipmap.y10, R.mipmap.y11};
        List<DataBean> listStr = new ArrayList<>();
        for (int i = 0; i < laberStr.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(laberStr[i]);
            bean.setImg(laberImg[i]);
            listStr.add(bean);
        }
        LabelAdapter labelAdapter = new LabelAdapter(act, listStr);
        gridView.setAdapter(labelAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0://我收藏的故事
                        if (!User.getInstance().isLogin()){
                            UIHelper.startLoginAct();
                            return;
                        }
                        UIHelper.startMyCollectionFrg(root);
                        break;
                    case 1://最近播放
                        UIHelper.startRecentlyBroadcastFrg(root);
                        break;
                    case 2://我的下载
                        UIHelper.startMyDownloadFrg(root);
                        break;
                    case 3://关于神奇动物
                        UIHelper.startAboutUsFrg(root);
                        break;
                    case 4://清除缓存
                        PopupWindowTool.showDialog(act, PopupWindowTool.clear_cache, new PopupWindowTool.DialogListener() {
                            @Override
                            public void onClick() {
                                mView.showLoading();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        CleanUtils.cleanCustomDir(Constants.mainPath);
                                        LitePal.deleteAll(DownloadBean.class);
                                        LitePal.deleteAll(HistoryBean.class);
                                        mView.hideLoading();
                                        showToast("清除缓存成功");
                                    }
                                }, 1000);
                            }
                        });
                        break;
                    case 5:
                        UIHelper.startFeedbackFrg(root, 1);
                        break;
                }
            }
        });
    }

    @Override
    public void onRequest() {
        if (!User.getInstance().isLogin())return;
        CloudApi.userGetUserInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        mView.showLoading();
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
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data != null){
                                JSONObject user = data.optJSONObject("user");
                                try {
                                    user.put("url", data.optString("url"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                User.getInstance().setUserInfoObj(user);
                                mView.setData(user);
                            }
                        }
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
    public void onUpdateHead(String path) {
        CloudApi.userUploadAvatar(path)
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
                            JSONObject userInfoObj = User.getInstance().getUserInfoObj();
                            try {
                                userInfoObj.put("url", jsonObject.optString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mView.setHead(jsonObject.optString("message"));
                        }
//                        showToast(jsonObject.optString("message"));
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
