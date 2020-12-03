package com.geek.geekstudio.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");

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
}
