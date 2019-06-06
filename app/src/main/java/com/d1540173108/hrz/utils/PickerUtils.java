package com.d1540173108.hrz.utils;

import android.app.Activity;
import android.graphics.Color;

import cn.addapp.pickers.picker.DatePicker;

/**
 * Created by edison on 2018/4/28.
 */

public class PickerUtils {

    private static final String mViolet = "#773FE3";
    private static final String mWhite = "#ffffff";

    /**
     * 选择日期类
     */
    public static void setPickerData(Activity act, final OnDatePickListener listener) {
        final DatePicker picker = new DatePicker(act);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(true);
        picker.setTopPadding(15);
        picker.setRangeStart(TimeUtils.getYear() - 30, TimeUtils.getMonth(), TimeUtils.getDay());
        picker.setRangeEnd(TimeUtils.getYear() + 1, TimeUtils.getMonth(), TimeUtils.getDay());
        picker.setSelectedItem(TimeUtils.getYear(), TimeUtils.getMonth(), TimeUtils.getDay());
        picker.setWeightEnable(true);
        picker.setTopBackgroundColor(Color.parseColor(mViolet));
        picker.setSubmitTextColor(Color.parseColor(mWhite));
        picker.setCancelTextColor(Color.parseColor(mWhite));
//        picker.setTitleText("选择时间");
        picker.setTitleTextColor(Color.parseColor(mWhite));
        picker.setSelectedTextColor(Color.parseColor(mViolet));//前四位值是透明度
//        picker.setUnSelectedTextColor(getResources().getColor(R.color.b));
        picker.setLineColor(Color.parseColor(mViolet));
        picker.setLineVisible(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                if (listener != null){
                    listener.onDatePicked(year, month, day);
                }
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public interface OnDatePickListener{
        void onDatePicked(String year, String month, String day);
    }

}
