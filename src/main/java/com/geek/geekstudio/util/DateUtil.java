package com.geek.geekstudio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
    private static SimpleDateFormat shortGFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 时间转字符串
     * @param date
     * @return
     */
    public static String DateToStr(Date date) {
        String str = format.format(date);
        return str;
    }

    /**
     * 生成系统时间字符串
     * @return
     */
    public static String creatDate() {
        String str = format.format(new Date(System.currentTimeMillis()));
        return str;
    }

    /**
     * 字符串转时间
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date StrToDate(String str) throws ParseException {
        Date date = null;
        date = format.parse(str);
        return date;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 时间戳
     * @return
     */
    public static String timeStampToDate(long seconds) {
        return format.format(new Date(seconds));
    }

    /**
     * 日期格式字符串转换成时间戳
     * @return
     */
    public static long date2TimeStamp(String date_str) throws ParseException {
        return format.parse(date_str).getTime();
    }
}
