package com.d1540173108.hrz.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.d1540173108.hrz.R;
import com.d1540173108.hrz.base.BaseFragment;
import com.d1540173108.hrz.base.BasePresenter;
import com.d1540173108.hrz.base.IBaseView;
import com.d1540173108.hrz.controller.UIHelper;
import com.d1540173108.hrz.databinding.FIdentifyingBinding;
import com.d1540173108.hrz.presenter.IdentifyingPresenter;
import com.d1540173108.hrz.utils.Constants;
import com.d1540173108.hrz.utils.GlideLoadingUtils;
import com.d1540173108.hrz.utils.PopupWindowTool;
import com.d1540173108.hrz.view.impl.IdentifyingContract;
import com.d1540173108.hrz.weight.WPopupWindow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2019/1/14.
 *  识别中
 */

public class IdentifyingFrg extends BaseFragment<IdentifyingPresenter, FIdentifyingBinding> implements IdentifyingContract.View, View.OnClickListener{

    private String path;
    private Bitmap ratio;
    private WPopupWindow popupWindow;
    private String carPath;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        path = bundle.getString("path");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_identifying;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.distinguish));
        mB.btSubmit.setOnClickListener(this);
        mB.btCancel.setOnClickListener(this);
        GlideLoadingUtils.load(act, path, mB.ivImg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_submit:


//                act.getWindow().getDecorView().setDrawingCacheEnabled(true);
//                Bitmap bmp = act.getWindow().getDecorView().getDrawingCache();
//                int[] viewLocation = new int[2];
//                mB.vCaijian.getLocationInWindow(viewLocation);
//                int viewX = viewLocation[0]; // x 坐标
//                int viewY = viewLocation[1]; // y 坐标
////                LogUtils.e(viewX, viewY);
//                double v = ScreenUtils.getScreenWidth() / 1.35;
//                Bitmap clip = ImageUtils.clip(bmp, viewX, viewY, (int)v, (int)v, true);
//                ratio = ratio(clip, 240f, 120f);
//
////                mB.ivPlayImg.setImageBitmap(clip);
//                act.getWindow().getDecorView().setDrawingCacheEnabled(false);
////                mB.ivCaijian.setVisibility(View.VISIBLE);
////                mB.ivSearch.setVisibility(View.VISIBLE);
////                floatAnim(mB.ivSearch, 3);
                View wh = LayoutInflater.from(act).inflate(R.layout.p_identifying, null);
                if (popupWindow == null){
                    popupWindow = new WPopupWindow(wh);
                }
                popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
                View ivSearch = wh.findViewById(R.id.iv_search);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                floatAnim(ivSearch, 3);
//                carPath = Constants.imageUrl + TimeUtils.getNowMills();
//                ImageUtils.save(ratio, carPath, Bitmap.CompressFormat.PNG, true);
                mPresenter.onSend(path);
                break;
            case R.id.bt_cancel:
                act.onBackPressed();
                break;
        }
    }

    private Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    @SuppressLint("WrongConstant")
    private static void floatAnim(View view, int delay) {
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(view, "translationX", -6.0f, 6.0f, -6.0f);
        translationXAnim.setDuration(1000);
        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        translationXAnim.setRepeatMode(ValueAnimator.INFINITE);//
        translationXAnim.start();
        animators.add(translationXAnim);
        ObjectAnimator translationYAnim = ObjectAnimator.ofFloat(view, "translationY", -3.0f, 3.0f, -3.0f);
        translationYAnim.setDuration(1000);
        translationYAnim.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim.setRepeatMode(ValueAnimator.INFINITE);
        translationYAnim.start();
        animators.add(translationYAnim);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(delay);
        btnSexAnimatorSet.start();

    }

    @Override
    public void setData(String result) {
//        mB.ivSearch.setVisibility(View.GONE);
        popupWindow.dismiss();
        UIHelper.startIdentifyingResultFrg(IdentifyingFrg.this, result, path);
    }

    @Override
    public void setGetError() {
//        mB.ivSearch.setVisibility(View.GONE);
        popupWindow.dismiss();
        PopupWindowTool.showDistinguishError(act, new PopupWindowTool.DialogListener() {
            @Override
            public void onClick() {
                act.onBackPressed();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (popupWindow != null){
            popupWindow.dismiss();
        }
    }
}
