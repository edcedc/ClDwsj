package com.d1540173108.hrz.controller;

import android.graphics.Bitmap;
import android.util.Base64;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.Utils;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.BaseListBean;
import com.d1540173108.hrz.bean.BaseResponseBean;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.callback.JsonConvert;
import com.d1540173108.hrz.callback.NewsCallback;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.utils.cache.ShareSessionIdCache;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okrx2.adapter.ObservableResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CloudApi {


    private static final String url =
            "chimelong.powert.top";
//            "192.168.101.31:9528";

    public static final String SERVLET_URL = "http://" +
            url + "/zoor/";

    public static final String IMAGE_SERVLET_URL = "http://" + url;

    public static final String HEAD_SERVLET_URL = "http://" + url + "/zoor";

    //客户地址
    public static final String CHIMELONG_URL = "http://animal.chimelong.com/";
    //客户微软地址
    public static final String WEIRUAN_URL = "http://chimelongabcapi.chinanorth.cloudapp.chinacloudapi.cn/";

    public static final String TEST_URL = ""; //测试

    private static final String TAG = "CloudApi";

    private CloudApi() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 获得token
     */
    public static Observable<JSONObject> gettoken() {
        return OkGo.<JSONObject>get(CHIMELONG_URL + "gettoken")
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取识别结果
     */
    public static Observable<JSONObject> getRecoResult(long conversationId) {
        return OkGo.<JSONObject>post(CHIMELONG_URL + "getRecoResult")
                .params("conversationId", conversationId)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 动物百科信息
     */
    public static Observable<JSONObject> info(String url) {
        return OkGo.<JSONObject>get(url)
                .params("token", User.getInstance().getToken())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 7.动物AI查询
     */
    public static Observable<JSONArray> send(String url) {
        return OkGo.<JSONArray>get(url)
                .params("token", User.getInstance().getToken())
                .converter(new JsonConvert<JSONArray>() {
                })
                .adapt(new ObservableBody<JSONArray>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.发送图片消息
     */
    public static Observable<JSONObject> chatbotSend(String path, long coverId) {
        return OkGo.<JSONObject>post("http://upload.jsb.peanuts.cc/?s=/wechat/Api/upload_img")
                .isMultipart(true)
                .params("TOKEN", User.getInstance().getToken())
                .params("image", new File(path))
                .params("_coverId", coverId)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 纠错
     */
    public static Observable<JSONObject> chatbotSend(String path, String newName) {
        return OkGo.<JSONObject>post("http://upload.jsb.peanuts.cc/?s=/wechat/Api/correction")
                .isMultipart(true)
                .params("TOKEN", User.getInstance().getToken())
                .params("image", new File(path))
                .params("newName", newName)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 8.动物明星
     */
    public static Observable<JSONArray> animalstarList() {
        return OkGo.<JSONArray>get(WEIRUAN_URL + "v1/chatbot/animalstar/list")
                .params("token", User.getInstance().getToken())
                .converter(new JsonConvert<JSONArray>() {
                })
                .adapt(new ObservableBody<JSONArray>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 登陆接口
     */
    public static Observable<JSONObject> authLogin(String mobile, String code) {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/phoneLogin")
                .params("phoneNum", mobile)
                .params("salt", code)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  我的收藏
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> storyGetStoryList(int pageNumber, int rows) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + "story/getStoryList")
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("rows", rows)
                .params("page", pageNumber)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }


    /**
     * 记录
     *  播放记录0  收藏1  下载2
     */
    public static Observable<JSONObject> storySavePlayNum(String storyId, int type) {
        return OkGo.<JSONObject>post(SERVLET_URL + "story/savePlayNum")
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("storyId", storyId)
                .params("type", type)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 3.1.1获取验证码注册 接口(好)
     */
    public static Observable<Response<BaseResponseBean>> authGetSms(String phone) {
        return OkGo.<BaseResponseBean>post(SERVLET_URL + "user/sentPhoneVerCode")
                .params("phoneNum", phone)
                .converter(new JsonConvert<BaseResponseBean>() {
                })
                .adapt(new ObservableResponse<BaseResponseBean>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 10.5、上传用户头像
     */
    public static Observable<JSONObject> userUploadAvatar(String head) {
        LogUtils.e(ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId());
        return OkGo.<JSONObject>post(SERVLET_URL + "user/uploadImg")
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("file", new File(head))
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取用户信息
     */
    public static Observable<JSONObject> userGetUserInfo() {
        return OkGo.<JSONObject>post(SERVLET_URL + "user/getUserDetail")
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 修改用户信息
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> userSaveUserPhone(String name, String sex, String  birthDate) {
        GetRequest<BaseResponseBean<DataBean>> post = OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "user/saveUserPhone");
        if (!StringUtils.isEmpty(name)){
            post.params("userNickName", name);
        }
        if (!StringUtils.isEmpty(sex)){
            post.params("sex", sex);
        }
        if (!StringUtils.isEmpty(birthDate)){
            post.params("birthDate", birthDate);
        }
        return post
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  站内信
     */
    public static String messageGetMsgList = "message/getMsgList";
    /**
     *  故事
     */
    public static String storyGetStoryList = "story/getStoryListNew";

    /**
     *  用户协议   关于我们
     */
    public static String agreementGetUserAgreement = "agreement/getUserAgreement";

    /**
     * 取消收藏
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> storyDelPlayNum(String id) {
        return OkGo.<BaseResponseBean<DataBean>>post(SERVLET_URL + "story/delPlayNum")
                .params("storyIds", id)
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 新增反馈
     */
    public static Observable<Response<BaseResponseBean<DataBean>>> feedbackSaveFeedbackMsg(String title, String content) {
        return OkGo.<BaseResponseBean<DataBean>>get(SERVLET_URL + "feedback/saveFeedbackMsg")
                .params("title", title)
                .params("content", content)
                .params("userId", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .converter(new NewsCallback<BaseResponseBean<DataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<DataBean>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<DataBean>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 第三方登录找我换userId
     */
    public static Observable<JSONObject> userOpenIdChangeUserId(String openid, String addr, String name, String head) {
        return OkGo.<JSONObject>get(SERVLET_URL + "user/openIdChangeUserId")
                .params("openid", openid)
                .params("addr", addr)
                .params("name", name)
                .params("picUrl", head)
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 通用list数据
     */
    public static Observable<Response<BaseResponseBean<BaseListBean<DataBean>>>> list(int pageNumber, String url) {
        return OkGo.<BaseResponseBean<BaseListBean<DataBean>>>post(SERVLET_URL + url)
                .params("rows", Constants.pageSize)
                .params("page", pageNumber)
                .converter(new NewsCallback<BaseResponseBean<BaseListBean<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<BaseListBean<DataBean>>> response) {

                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<BaseListBean<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }
    /**
     * 通用list 2
     */
    public static Observable<Response<BaseResponseBean<List<DataBean>>>> list2(String url) {
        return OkGo.<BaseResponseBean<List<DataBean>>>post(SERVLET_URL + url)
                .converter(new NewsCallback<BaseResponseBean<List<DataBean>>>() {
                    @Override
                    public void onSuccess(Response<BaseResponseBean<List<DataBean>>> response) {
                    }
                })
                .adapt(new ObservableResponse<BaseResponseBean<List<DataBean>>>())
                .subscribeOn(Schedulers.io());
    }

    /**
     *  长隆红包核销功能
     */
    public static Observable<JSONObject> lotteryMyprize(String photoID) {
        long mills = TimeUtils.getNowMills();
        float v = (float) (Math.random() * mills);
        String m = mills + v + mills + v + "";
        byte[] bytes = EncryptUtils.encryptMD5(m.getBytes());
        return OkGo.<JSONObject>post("https://ar.chimelong.com/lottery/myprize?")
                .params("photoID", photoID)
                .params("memberID", ShareSessionIdCache.getInstance(Utils.getApp()).getSessionId())
                .params("t", mills)
                .params("n", v)
                .params("s", bytes.toString())
                .converter(new JsonConvert<JSONObject>() {
                })
                .adapt(new ObservableBody<JSONObject>())
                .subscribeOn(Schedulers.io());
    }
}