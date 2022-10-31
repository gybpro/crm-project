package com.high.crm.commons.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Classname DateFormatUtil
 * @Description 日期格式转换工具
 * @Author high
 * @Create 2022/10/31 9:42
 * @Version 1.0
 */
public class DateUtil {
    /**
     * 对指定Date对象格式化：yyyy-MM-dd HH:mm:ss
     * @param date
     * @return 格式化字符串
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 对指定Date对象格式化：yyyy-MM-dd
     * @param date
     * @return 格式化字符串
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 对指定Date对象格式化：HH:mm:ss
     * @param date
     * @return 格式化字符串
     */
    public static String formatTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(date);
    }
}
