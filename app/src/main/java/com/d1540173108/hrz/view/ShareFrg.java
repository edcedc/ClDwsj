package com.d1540173108.hrz.view;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.base.User;
import com.d1540173108.hrz.bean.DataBean;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FShareBinding;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.utils.ShareTool;
import com.d1540173108.hrz.utils.ZXingUtils;
import com.google.zxing.WriterException;
import com.umeng.socialize.ShareAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by edison on 2019/1/13.
 * 分享
 */

public class ShareFrg extends BaseFragment<BasePresenter, FShareBinding> implements IBaseView, View.OnClickListener {

    private View lyShare1, lyShare2, lyShare3, lyShare4, lyShare5;
    private ImageView ivImg1, ivImg2, ivImg3, ivImg4, ivImg5;
    private ImageView ivZking1, ivZking2, ivZking3, ivZking4, ivZking5;
    private AppCompatTextView tvName1, tvName2, tvName3, tvName4, tvName5;
    private AppCompatTextView tvContent1, tvContent2, tvContent3, tvContent4, tvContent5;
    private ShareAction shareAction;

    private JSONObject data;
    private String path;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        try {
            data = new JSONObject(bundle.getString("data"));
            path = bundle.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_share;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.share), R.color.white, R.color.blue_6759AE);
        lyShare1 = view.findViewById(R.id.ly_share1);
        lyShare2 = view.findViewById(R.id.ly_share2);
        lyShare3 = view.findViewById(R.id.ly_share3);
        lyShare4 = view.findViewById(R.id.ly_share4);
        lyShare5 = view.findViewById(R.id.ly_share5);

        ivImg1 = view.findViewById(R.id.iv_img1);
        ivZking1 = view.findViewById(R.id.iv_zking1);
        tvName1 = view.findViewById(R.id.tv_name1);
        tvContent1 = view.findViewById(R.id.tv_content1);

        ivImg2 = view.findViewById(R.id.iv_img2);
        ivZking2 = view.findViewById(R.id.iv_zking2);
        tvName2 = view.findViewById(R.id.tv_name2);
        tvContent2 = view.findViewById(R.id.tv_content2);

        ivImg3 = view.findViewById(R.id.iv_img3);
        ivZking3 = view.findViewById(R.id.iv_zking3);
        tvName3 = view.findViewById(R.id.tv_name3);
        tvContent3 = view.findViewById(R.id.tv_content3);

        ivImg4 = view.findViewById(R.id.iv_img4);
        ivZking4 = view.findViewById(R.id.iv_zking4);
        tvName4 = view.findViewById(R.id.tv_name4);
        tvContent4 = view.findViewById(R.id.tv_content4);

        ivImg5 = view.findViewById(R.id.iv_img5);
        ivZking5 = view.findViewById(R.id.iv_zking5);
        tvName5 = view.findViewById(R.id.tv_name5);
        tvContent5 = view.findViewById(R.id.tv_content5);


        lyShare2.setVisibility(View.GONE);
        lyShare3.setVisibility(View.GONE);
        lyShare4.setVisibility(View.GONE);
        lyShare5.setVisibility(View.GONE);
        mB.ivMoban1.setOnClickListener(this);
        mB.ivMoban2.setOnClickListener(this);
        mB.ivMoban3.setOnClickListener(this);
        mB.ivMoban4.setOnClickListener(this);
        mB.ivMoban5.setOnClickListener(this);
        mB.btSubmit.setOnClickListener(this);
        mB.ivMoban1.setBackgroundResource(R.mipmap.icon_mb1);

        setData(tvName1, tvContent1, ivImg1, ivZking1);
        setData(tvName2, tvContent2, ivImg2, ivZking2);
        setData(tvName3, tvContent3, ivImg3, ivZking3);
        setData(tvName4, tvContent4, ivImg4, ivZking4);
        setData(tvName5, tvContent5, ivImg5, ivZking5);

        shareAction = ShareTool.getInstance().shareActionImage(act, "http://chimelong.peanuts.cc/?ch=25");
    }

    private void setData(AppCompatTextView tvName, AppCompatTextView tvContent, final ImageView ivImg, final ImageView ivZking) {
        JSONArray media = data.optJSONArray("media");
        JSONArray array = new JSONArray();
        if (media != null && media.length() != 0) {
            for (int i = 0; i < media.length(); i++) {
                JSONObject object1 = media.optJSONObject(i);
                if (object1.optString("type").equals("img")) {//判断是否有音频
                    array.put(object1);
                    break;
                }
            }
        }
        if (path != null) {
            GlideLoadingUtils.load(act, path, ivImg);
        }else  if (array.length() != 0 && StringUtils.isEmpty(path)) {
            GlideLoadingUtils.load(act, array.optJSONObject(0).optString("url"), ivImg);
        }
        tvName.setText(data.optString("name"));
        tvContent.setText(getText(R.string.tishiyu));
        ivZking.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivZking.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                Bitmap bitmap = CodeCreator.createQRCode("http://pic36.photophoto.cn/20150717/1155116831702065_b.jpg", 100, 100, null);
                Bitmap bitmap = null;
                try {
                    bitmap = ZXingUtils.creatBarcode("https://m.chimelong.com/?dis=10023", 200);
                    if (bitmap != null) {
                        ivZking.setImageBitmap(bitmap);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_moban1:
                setMoBan(1);
                break;
            case R.id.iv_moban2:
                setMoBan(2);
                break;
            case R.id.iv_moban3:
                setMoBan(3);
                break;
            case R.id.iv_moban4:
                setMoBan(4);
                break;
            case R.id.iv_moban5:
                setMoBan(5);
                break;
            case R.id.bt_submit:
                if (!User.getInstance().isLogin()) {
                    UIHelper.startLoginAct();
                    return;
                }

//                if(Build.VERSION.SDK_INT>=23){
//                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
//                    ActivityCompat.requestPermissions(act,mPermissionList,123);
//                }

                Bitmap bitmap = ImageUtils.view2Bitmap(mB.layout);
                ShareTool.getInstance().setImgBitmap(bitmap);
                shareAction.open();
                break;
        }
    }

    private void setMoBan(int type) {
        switch (type) {
            case 1:
                lyShare1.setVisibility(View.VISIBLE);
                lyShare2.setVisibility(View.GONE);
                lyShare3.setVisibility(View.GONE);
                lyShare4.setVisibility(View.GONE);
                lyShare5.setVisibility(View.GONE);
                mB.ivMoban1.setBackgroundResource(R.mipmap.icon_mb1);
                mB.ivMoban2.setBackgroundResource(R.mipmap.icon_moban2);
                mB.ivMoban3.setBackgroundResource(R.mipmap.icon_moban3);
                mB.ivMoban4.setBackgroundResource(R.mipmap.icon_moban4);
                mB.ivMoban5.setBackgroundResource(R.mipmap.icon_moban5);
                break;
            case 2:
                lyShare1.setVisibility(View.GONE);
                lyShare2.setVisibility(View.VISIBLE);
                lyShare3.setVisibility(View.GONE);
                lyShare4.setVisibility(View.GONE);
                lyShare5.setVisibility(View.GONE);
                mB.ivMoban1.setBackgroundResource(R.mipmap.icon_moban1);
                mB.ivMoban2.setBackgroundResource(R.mipmap.icon_mb2);
                mB.ivMoban3.setBackgroundResource(R.mipmap.icon_moban3);
                mB.ivMoban4.setBackgroundResource(R.mipmap.icon_moban4);
                mB.ivMoban5.setBackgroundResource(R.mipmap.icon_moban5);
                break;
            case 3:
                lyShare1.setVisibility(View.GONE);
                lyShare2.setVisibility(View.GONE);
                lyShare3.setVisibility(View.VISIBLE);
                lyShare4.setVisibility(View.GONE);
                lyShare5.setVisibility(View.GONE);
                mB.ivMoban1.setBackgroundResource(R.mipmap.icon_moban1);
                mB.ivMoban2.setBackgroundResource(R.mipmap.icon_moban2);
                mB.ivMoban3.setBackgroundResource(R.mipmap.icon_mb3);
                mB.ivMoban4.setBackgroundResource(R.mipmap.icon_moban4);
                mB.ivMoban5.setBackgroundResource(R.mipmap.icon_moban5);
                break;
            case 4:
                lyShare1.setVisibility(View.GONE);
                lyShare2.setVisibility(View.GONE);
                lyShare3.setVisibility(View.GONE);
                lyShare4.setVisibility(View.VISIBLE);
                lyShare5.setVisibility(View.GONE);
                mB.ivMoban1.setBackgroundResource(R.mipmap.icon_moban1);
                mB.ivMoban2.setBackgroundResource(R.mipmap.icon_moban2);
                mB.ivMoban3.setBackgroundResource(R.mipmap.icon_moban4);
                mB.ivMoban4.setBackgroundResource(R.mipmap.icon_mb4);
                mB.ivMoban5.setBackgroundResource(R.mipmap.icon_moban5);
                break;
            case 5:
                lyShare1.setVisibility(View.GONE);
                lyShare2.setVisibility(View.GONE);
                lyShare3.setVisibility(View.GONE);
                lyShare4.setVisibility(View.GONE);
                lyShare5.setVisibility(View.VISIBLE);
                mB.ivMoban1.setBackgroundResource(R.mipmap.icon_moban1);
                mB.ivMoban2.setBackgroundResource(R.mipmap.icon_moban2);
                mB.ivMoban3.setBackgroundResource(R.mipmap.icon_moban4);
                mB.ivMoban4.setBackgroundResource(R.mipmap.icon_moban5);
                mB.ivMoban5.setBackgroundResource(R.mipmap.icon_mb5);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareTool.getInstance().release(act);
    }
}
