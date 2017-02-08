package com.dateviewdemo.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhairui on 2017/2/8.
 */

public class DateUtil {
    public static String getTodayDateFormat() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}
