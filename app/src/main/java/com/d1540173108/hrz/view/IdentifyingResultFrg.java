package com.d1540173108.hrz.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseActivity;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FIdentifyingResultBinding;
import com.d1540173108.hrz.presenter.IdentifyingResultPresenter;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.view.impl.IdentifyingResultContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by edison on 2019/1/14.
 *  识别结果
 */

public class IdentifyingResultFrg extends BaseFragment<IdentifyingResultPresenter, FIdentifyingResultBinding> implements IdentifyingResultContract.View, View.OnClickListener{

    private String result;
    private String path;
    private String tagId;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        result = bundle.getString("result");
        path = bundle.getString("path");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_identifying_result;
    }

    @Override
    protected void initView(View view) {
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mAppCompatActivity.setSupportActionBar(toolbar);
        mAppCompatActivity.getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.onBackPressed();
            }
        });
        mB.tvMore.setOnClickListener(this);
        mB.lyAnimals.setOnClickListener(this);
        try {
            GlideLoadingUtils.load(act, path, mB.ivHead);
            JSONObject data = new JSONObject(result);
            JSONArray attachments = data.optJSONArray("attachments");
            if (attachments != null && attachments.length() != 0){
                for (int i = 0;i < attachments.length();i++){
                    JSONObject object = attachments.optJSONObject(i);

                    String content1 = object.optString("content");
                    if (content1.equals("Result:0"))return;

                    JSONObject content = object.optJSONObject("content");
                    GlideLoadingUtils.load(act, content.optString("ImageUrl"), mB.ivImg2);
                    tagId = content.optString("TagId");
                    int probability = (int) content.optDouble("Probability");
                    if (probability < 40){
                        mB.tvContent.setTextColor(act.getColor(R.color.reb_FE4D49));
                        mB.ivImg.setBackgroundResource(R.mipmap.icon_shibie3);
                        mB.tvLessThan.setVisibility(View.VISIBLE);
                    }else if (probability > 80){
                        mB.tvContent.setTextColor(act.getColor(R.color.blue_765BE7));
                        mB.ivImg.setBackgroundResource(R.mipmap.icon_shibiebeijing1);
                    }else {//小于90 大于30
                        mB.tvContent.setTextColor(act.getColor(R.color.blue_FE84607));
                        mB.ivImg.setBackgroundResource(R.mipmap.icon_shibiebeijing2);
                        mB.tvLessThan.setVisibility(View.VISIBLE);
                    }
                    mB.tvContent.setText(probability +
                            "%");
                    mB.tvName.setText(content.optString("Family"));
                    mB.tvNick.setText(content.optString("En"));
                    mB.tvDesc.setText(content.optString("Intro"));
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

   }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_more:
                UIHelper.startIdentifyingDetailsFrg(this, result, path);
                break;
            case R.id.ly_animals:
                UIHelper.startAnimalDescAct(tagId, path);
                break;
        }
    }
}
