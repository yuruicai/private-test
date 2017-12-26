package com.sinochem.yunlian.upm.common.time;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhangxi
 * @version 1.0
 * @created 2012-12-24
 */
public class TimeUtil {
    public static final String DAY_FORMAT = "yyyy-MM-dd";
    public static final String HOUR_FORMAT = "yyyy-MM-dd HH";
    public static final String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String[] PARSE_PATTERNS = new String[]{SECOND_FORMAT, MINUTE_FORMAT, HOUR_FORMAT, DAY_FORMAT};
    public static final int MINUTE_SECONDS = 60;
    public static final int HOUR_SECONDS = 3600;
    public static final int DAY_SECONDS = HOUR_SECONDS * 24;

    private TimeUtil() {
    }

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

    /**
     * 小时开始时间
     *
     * @param hour
     * @return
     */
    public static Date hourStart(int hour) {
        Date currentHourStart = DateUtils.truncate(now(), Calendar.HOUR);
        return DateUtils.addHours(currentHourStart, hour);
    }

    /**
     * 小时开始时间，unixtime
     *
     * @param hour
     * @return
     */
    public static int hourStartSecond(int hour) {
        return unixtime(hourStart(hour));
    }

    /**
     * 每天的开始时间
     *
     * @param day
     * @return
     */
    public static Date dayStart(int day) {
        Date todayStart = DateUtils.truncate(now(), Calendar.DATE);
        return DateUtils.addDays(todayStart, day);
    }

    public static int dayStartSecond(int day) {
        return unixtime(dayStart(day));
    }

    /**
     * @param start
     * @param end
     * @return
     */
    public static List<String> buildDayRange(int start, int end) {
        List<String> days = new ArrayList<String>();
        for (int index = start; index < end; index++) {
            SimpleDateFormat formatter = new SimpleDateFormat(DAY_FORMAT);
            days.add(formatter.format(dayStart(index)));
        }
        return days;
    }

    public static List<Integer> buildDayRangeSecond(int start, int end) {
        List<Integer> days = new ArrayList<Integer>();
        for (int index = start; index < end; index++) {
            days.add(dayStartSecond(index));
        }
        return days;
    }

    /**
     * @param start
     * @param end
     * @return
     */
    public static List<String> buildHourRange(final Date date, int start, int end) {
        List<String> hours = new ArrayList<String>();
        Date hourStart = DateUtils.truncate(date, Calendar.HOUR);
        Date tempDate = DateUtils.addHours(hourStart, start);
        Date endDate = DateUtils.addHours(hourStart, end);
        while (tempDate.before(endDate)) {
            SimpleDateFormat formatter = new SimpleDateFormat(HOUR_FORMAT);
            hours.add(formatter.format(tempDate));
            tempDate = DateUtils.addHours(tempDate, 1);
        }
        return hours;
    }

    public static List<Integer> buildHourRangeSecond(Date date, int start, int end) {
        List<Integer> hours = new ArrayList<Integer>();
        Date hourStart = DateUtils.truncate(date, Calendar.HOUR);
        Date tempDate = DateUtils.addHours(hourStart, start);
        Date endDate = DateUtils.addHours(hourStart, end);
        while (tempDate.before(endDate)) {
            hours.add(unixtime(tempDate));
            tempDate = DateUtils.addHours(tempDate, 1);
        }
        return hours;
    }

    public static String format(String format, Integer unixtime) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date(unixtime));
    }

    public static String format(Integer unixtime) {
        return format(SECOND_FORMAT, unixtime);
    }

    /**
     * 将时间串转换为秒
     *
     * @param timeStr
     * @return
     * @throws IllegalArgumentException
     */
    public static int unixtime(String timeStr) {
        return unixtime(parse(timeStr));
    }

    public static Date parse(String timeStr) {
        try {
            return DateUtils.parseDate(timeStr, PARSE_PATTERNS);
        } catch (ParseException e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    public static String beautify(Integer sec) {
        int day = sec / DAY_SECONDS;
        int hour = (sec - day * DAY_SECONDS) / HOUR_SECONDS;
        int minute = (sec - day * DAY_SECONDS - hour * HOUR_SECONDS) / MINUTE_SECONDS;
        int second = sec - day * DAY_SECONDS - hour * HOUR_SECONDS - minute * MINUTE_SECONDS;
        return Joiner.on("").join(day == 0 ? "" : day + "天", hour == 0 ? "" : hour + "小时",
                minute == 0 ? "" : minute + "分钟", second == 0 ? "" : second + "秒");
    }

    public static String getAuthDate(Date date) {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }
}
