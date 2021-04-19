package com.geek.geekstudio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //private static SimpleDateFormat shortGFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间转字符串
     */
    public static String DateToStr(Date date) {
        return format.format(date);
    }

    /**
     * 生成系统时间字符串
     */
    public static String creatDate() {
        return format.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 字符串转时间
     */
    public static Date StrToDate(String str) throws ParseException {
        return format.parse(str);
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 时间戳
     */
    public static String timeStampToDate(long seconds) {
        return format.format(new Date(seconds));
    }

    /**
     * 日期格式字符串转换成时间戳
     */
    public static long date2TimeStamp(String date_str) throws ParseException {
        return format.parse(date_str).getTime();
    }
}
