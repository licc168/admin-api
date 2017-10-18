package com.lccf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/5/17 14:28
 * @see
 */
public class TimeUtil {
    public static final String Formater_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private static final String[] parsePatterns = new String[] {
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd",
        "yyyy-MM",
        "HH:mm",
        "MM/dd/yyyy HH:mm:ss",
        "MM/dd/yyyy"
        // 这里可以增加更多的日期格式，用得多的放在前面
    };
    public static Date longToTime(Long time) {
        LocalDateTime localDateTime = LocalDateTime
            .ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }
    public static String format(Date date, String format) {
        if(date == null) return "";
        SimpleDateFormat df = new SimpleDateFormat(StringUtils.defaultIfEmpty(
            format, Formater_yyyy_MM_dd_HH_mm_ss));
        return df.format(date);
    }
    public static Date parse(String date) {
        try {
            return DateUtils.parseDate(date, parsePatterns);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
