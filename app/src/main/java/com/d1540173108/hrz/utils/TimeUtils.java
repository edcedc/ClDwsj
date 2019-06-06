package com.d1540173108.hrz.utils;

import java.util.Calendar;

/**
 * Created by edison on 2018/5/2.
 */

public class TimeUtils {

    /**
     * 获取当前年
     */
    public static int getYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     */
    public static int getMonth() {
        Calendar now = Calendar.getInstance();
        return (now.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取当前日
     */
    public static int getDay() {
        Calendar now = Calendar.getInstance();
        return (now.get(Calendar.DAY_OF_MONTH));
    }

}
