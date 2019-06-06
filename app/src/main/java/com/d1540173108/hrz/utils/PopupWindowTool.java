package com.d1540173108.hrz.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.d1540173108.hrz.R;
import com.d1540173108.hrz.weight.WPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/23.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PopupWindowTool {

    public static final int notLogin = 3; //未登录
    public static final int clear_cache = 4; //清除缓存
    public static final int sign_out= 6; //退出
    public static final int wifi= 7; //fei非wifi环境下


    public static void showDialog(final Context act, final int type, final DialogListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_dialog, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView tvTitle = wh.findViewById(R.id.tv_title);
        TextView btCancel = wh.findViewById(R.id.bt_cancel);
        TextView btSubmit = wh.findViewById(R.id.bt_submit);
        View view = wh.findViewById(R.id.view);
        switch (type){
            case notLogin:
                String str1 = "您还未登录，" + "<font color='#00CC66'> 前往登录>> </font>";
                tvTitle.setText(Html.fromHtml(str1));
                btCancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                break;
            case clear_cache:
                tvTitle.setText("确定清理缓存吗");
                break;
            case sign_out:
                tvTitle.setText("确定退出吗");
                break;
            case wifi:
                tvTitle.setText("将会消耗数据流量，是否继续？");
                break;
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (listener != null){
                    listener.onClick();
                }
            }
        });
    }

    public interface DialogListener{
        void onClick();
    }

    public static void showDistinguishError(final Context act, final DialogListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_distinguish, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        wh.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (listener != null){
                    listener.onClick();
                }
            }
        });
        wh.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    public static void showIdentifying(final Context act){

    }


}
