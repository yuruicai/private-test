package com.sinochem.yunlian.upm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author zhangxi
 * @version 1.0
 * @created 2012-12-24
 */
public class TimeUtil {
    public final static String DAY_FORMAT = "yyyy-MM-dd";
    public final static String HOUR_FORMAT = "yyyy-MM-dd HH";
    public final static String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String[] PARSE_PATTERNS = new String[]{SECOND_FORMAT, MINUTE_FORMAT, HOUR_FORMAT, DAY_FORMAT};
    public final static int MINUTE_SECONDS = 60;
    public final static int HOUR_SECONDS = 3600;
    public final static int DAY_SECONDS = HOUR_SECONDS * 24;

    public static int unixtime() {
        return ms2second(System.currentTimeMillis());
    }

    public static int unixtime(Date date) {
        return ms2second(date.getTime());
    }

    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    public static Date date(Integer unixtime) {
        return new Date(unixtime * 1000L);
    }

    public static int ms2second(long ms) {
        return (int) (ms / 1000);
    }

    public static String format(String format, Integer unixtime) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date(unixtime));
    }

    public static String format(Integer unixtime) {
        return format(SECOND_FORMAT, unixtime);
    }


    public static String getAuthDate(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }
}
