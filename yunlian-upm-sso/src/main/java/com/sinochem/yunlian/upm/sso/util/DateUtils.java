/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.sinochem.yunlian.upm.sso.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	/** 日期 */
	public final static String DEFAILT_DATE_PATTERN = "yyyy-MM-dd";
	private final static SimpleDateFormat wkDateFm = new SimpleDateFormat("EEEE",Locale.CHINA);
	private final static SimpleDateFormat dateFm = new SimpleDateFormat("MM-dd");


	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(String before, String after) {
		Date start= DateUtils.parseDate(before);
		Date end= DateUtils.parseDate(after);
		long beforeTime = start.getTime();
		long afterTime = end.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	
	
	/**
	  * 增加年数
	  * @param optype
	  * @param date
	  * @param num
	  * @return
	  */
	public static String ADD_DATE(int optype,String date,int num){
		String st_return = "";  
		 try {
			DateFormat daf_date = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.CHINA);
			daf_date.parse(date);
			Calendar calendar = daf_date.getCalendar();
			calendar.add(optype, num); 
				String st_m = "";
				String st_d = "";
			    int y = calendar.get(Calendar.YEAR);
			    int m = calendar.get(Calendar.MONTH) + 1;
			    int d = calendar.get(Calendar.DAY_OF_MONTH); 
			    if (m <= 9) {
			      st_m = "0" + m;
			    }
			    else {
			      st_m = "" + m;
			    }
			    if (d <= 9) {
			      st_d = "0" + d;
			    }
			    else {
			      st_d = "" + d;
			    }
			    st_return = y + "-" + st_m + "-" + st_d;
		} catch (ParseException e) { 
			e.printStackTrace();
		}
		return st_return;
	}
	
	/**
	 * @return 返回时间格式类型字符串 YYYY-MM-DD
	 */
	public static String getCurDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String strDate = sdf.format(date);
		return strDate;
	}
	/**
	 * 比较日期大小 *
	 * 
	 * @param date1
	 *            String
	 * @param date2
	 *            String
	 * @return int
	 */
	@SuppressWarnings("deprecation")
	public static int compareDate(String date1, String date2) {
		String[] date1Array = date1.split("-");
		String[] date2Array = date2.split("-");
		java.sql.Date date11 = new java.sql.Date(Integer.parseInt(
				date1Array[0], 10), Integer.parseInt(date1Array[1], 10),
				Integer.parseInt(date1Array[2], 10));
		java.sql.Date date22 = new java.sql.Date(Integer.parseInt(
				date2Array[0], 10), Integer.parseInt(date2Array[1], 10),
				Integer.parseInt(date2Array[2], 10));
		return date11.compareTo(date22);
	}
	/**
	 * 加减分钟
	 * 
	 * @param num
	 * @param Date
	 * @return
	 */
	public static Date addMinute(int num, Date Date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date);
		calendar.add(Calendar.MINUTE, num);// 把日期往后增加 num 天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}
	
	/**
	 * 时间比较
	 * 
	 * 判断当前时间是否在时间time之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isTimeBefore(String time) {
		if (time == null) {
			return false;
		}
		try {
			Date date1 = new Date();
			//DateFormat df = DateFormat.getDateTimeInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = df.parse(time);
			return date1.before(d);
		} catch (ParseException e) {
			return false;
		}
	}
	public static boolean isSystemDateBefore(String time) {
		if (time == null) {
			return false;
		}
		try {
			Date date1 = new Date();
			//DateFormat df = DateFormat.getDateTimeInstance();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date d = df.parse(time);
			return d.before(date1);
		} catch (ParseException e) {
			return false;
		}
	}

    /**
     * 比较时间date1是否在时间date2之前 时间格式 2008-11-25 16:30:10
     *
     * @param date1
     * @param date2
     * @return boolean; true:在date2之前
     * @author
     */
    public static boolean beforeDate(String date1, String date2) {
        try {
            Date d1 = convertString2Date(DEFAILT_DATE_PATTERN, date1);
            Date d2 = convertString2Date(DEFAILT_DATE_PATTERN, date2);
            return d1.before(d2);
        } catch (ParseException e) {
            return false;
        }
    }

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws Exception {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
//		Date date = convertString2Date("yyyy-MM-dd HH:mm:sss","2016-10-16 10:30:000");
//		System.out.println(transformDate(date));
//		boolean flag=isSystemDateBefore("2017-02-23");
//		System.out.println(flag);

		
		System.out.println(translateBdRepayDay("2017-06-15",25));

	}

	
	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isBlank(targetDate))
			return null;
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}
	
	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DEFAILT_DATE_PATTERN, targetDate);
	}
	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}
	
	/**
	 * 返回系统当前日期时间的文本格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getSystemDateTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	
	/**
	 * 按天数获取date
	 * @param date
	 * @param value
	 * @return
	 */
	public static Date addDay(Date date, int value) {  
		   Calendar now = Calendar.getInstance();  
		   now.setTime(date);  
		   now.add(Calendar.DAY_OF_YEAR, value);  
		   return now.getTime();  
	}  
	/**
	 * 获取N前天的日期
	 * @param n
	 * @return
	 */
	public static String getStatetime(int n){
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  Calendar c = Calendar.getInstance();  
		  c.add(Calendar.DATE, - n);  
		  Date monday = c.getTime();
		  String preMonday = sdf.format(monday);
		  return preMonday;
    }

	public static String getTimeSubtractDay(String date,int day){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
			c.setTime(sdf.parse(date));
		}catch (Exception e){
			e.printStackTrace();
		}

		c.add(Calendar.DATE, - day);
		Date monday = c.getTime();
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	public static String addMonth(String date,int month){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
			c.setTime(sdf.parse(date));
		}catch (Exception e){
			e.printStackTrace();
		}

		c.add(Calendar.MONTH, month);
		Date monday = c.getTime();
		String preMonday = sdf.format(monday);
		return preMonday;
	}

	
	
	
	/**
	 * 转换日期得到类微信消息时间格式
	 * @return
	 */
	public static String transformDate(Date date) {
		String result = "";
		Date cDate = new Date();
		Calendar d1 = new GregorianCalendar();  
        d1.setTime(date);  
        Calendar d2 = new GregorianCalendar();  
        d2.setTime(cDate);  
        int days = d2.get(Calendar.DAY_OF_YEAR)- d1.get(Calendar.DAY_OF_YEAR);  
        int y2 = d2.get(Calendar.YEAR);  
        if (d1.get(Calendar.YEAR) != y2) {  
            do {  
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);  
                d1.add(Calendar.YEAR, 1);  
            } while (d1.get(Calendar.YEAR) != y2);  
        }  
        if (days == 0) {
        	result = "今天";
        } else if (days == 1) {
        	result = "昨天";
        } else if (days >1 && days <=7 ) {
        	result = wkDateFm.format(date);
        } else {
        	result = dateFm.format(date);
        }
		return result;
	}

	/**
	 * 获取当前日期的上一天
	 * @param date
	 * @return
     */
	public static Date getProviousDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	public static String getDateStr(Long timeMIllis,Object... format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMIllis);
		return DateUtils.formatDate(calendar.getTime(),format);
	}

	public static Date getDate(Long timeMIllis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timeMIllis);
		return calendar.getTime();
	}

	public static String getDayStr(String date,int day){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try{
			calendar.setTime(sdf.parse(date));
		}catch (Exception e){
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return formatDate(calendar.getTime(),"yyyy-MM-dd");
	}





	/**
	 * 获取当前日期的上一天
	 * @param date
	 * @param format
     * @return
     */
	public static String getProviousDay(Date date,String format){
		Date d=getProviousDay(date);
		return formatDate(d,format);
	}



	/**
	 * 将天数转换成几个月几天的格式
	 * 每月默认取30天
	 * @return
	 */
	public static String translateDateToStr(int liveDay) {
		String liveStr = "";
		int liveMonth =  liveDay/30;
		int liveDayNum = liveDay%30;
		if (liveMonth != 0) {
			liveStr += liveMonth +"个月";
		}
		if (liveDayNum != 0) {
			liveStr += liveDayNum +"天";
		}
		return liveStr;
	}
	
	
	/**
	 * 计算百度还款日
	 * @return
	 */
	public static Integer translateBdRepayDay(String startDate,Integer prepaymentDay) {
		
		Date stDate = DateUtils.parseDate(startDate);
		if (prepaymentDay == 0) {
			prepaymentDay = 1;
		}
		Date prepayDate = DateUtils.addDay(stDate, -prepaymentDay);
		Integer resultDay = prepayDate.getDate();
		
		if (resultDay == 29 || resultDay == 30 || resultDay ==31) {
			resultDay = 28;
		} 
		
		return resultDay;
	}

	/**
	 * 获取指定时间的时间戳
	 * @param date
	 * @param day
	 * @return
	 */
	public static Long getTimeInMillis(Date date,int day){
		String dateStr=formatDate(date,"yyyy-MM-dd");
		date=parseDate(dateStr);
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, day);
		return now.getTimeInMillis();
	}

	

}
