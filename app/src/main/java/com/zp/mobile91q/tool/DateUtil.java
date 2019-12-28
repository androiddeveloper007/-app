package com.zp.mobile91q.tool;


import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 日期工具类
 * Created by AwenZeng on 2016/12/28.
 */
public class DateUtil {

    // 默认日期格式
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd";

    // 默认时间格式
    public static final String DEFAULT_FORMAT_DATETIME = "yyyy-MM-dd  HH:mm:ss";
    // 默认时间格式不显示秒
    public static final String DEFAULT_FORMAT_DATETIME_WITHOUT_SECOND = "yyyy-MM-dd  HH:mm";

    // 默认时间格式
    public static final String DEFAULT_FORMAT_DAYTIME = "MM-dd  HH:mm";

    public static final String DEFAULT_FORMAT_TIME = "HH:mm:ss";

    public static final String DEFAULT_FORMAT_HOUR = "HH:mm";

    public static final String DATE_YEAR_FORMAT = "yyyy";
    public static final String DATE_YEAR_MONTH_DAY_MIN_SEC_FORMAT = "yyyyMMddHHmmss";

    public static final String DATE_MONTH_FORMAT = "MM-dd";

    private static DateFormat dateFormat = null;
    private static DateFormat dateTimeFormat = null;
    private static DateFormat dateHourFormat = null;
    private static DateFormat timeFormat = null;

    private static Calendar calendar = null;

    static {
        dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_DATE);
        dateTimeFormat = new SimpleDateFormat(DEFAULT_FORMAT_DATETIME);
        dateHourFormat = new SimpleDateFormat(DEFAULT_FORMAT_HOUR);
        timeFormat = new SimpleDateFormat(DEFAULT_FORMAT_TIME);
        calendar = new GregorianCalendar();
    }


    /**
     * Date（long） 转换 String
     *
     * @param time
     * @param format
     * @return
     */
    public static String date2String(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(time);
        return s;
    }

    /**
    *  将时间转换为时间戳
    */
    public static long dateToLong(String s){
        try {
            Date date = dateFormat.parse(s);
            return date.getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     *  将小时转换为时间戳
     */
    public static long hourToLong(String s){
        try {
            Date date = dateHourFormat.parse(s);
            return date.getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * 日期格式化yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static Date formatDate(String date, String format) {
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getDateFormat(Date date) {
        return dateFormat.format(date);
    }

    /**
     * 日期格式化yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getDateTimeFormat(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return HH:mm:ss
     */
    public static String getTimeFormat(Date date) {
        return timeFormat.format(date);
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String getDateFormat(Date date, String formatStr) {
        if (!TextUtils.isEmpty(formatStr)) {
            return new SimpleDateFormat(formatStr).format(date);
        }
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    public static Date getDateFormat(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间格式化
     *
     * @param date
     * @return
     */
    public static Date getDateTimeFormat(String date) {
        try {
            return dateTimeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期(yyyy-MM-dd)
     *
     * @return
     */
    public static Date getNowDate() {
        return DateUtil.getDateFormat(dateFormat.format(new Date()));
    }

    /**
     * 获取当前日期星期一日期
     *
     * @return date
     */
    public static Date getFirstDayOfWeek() {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Monday
        return calendar.getTime();
    }

    /**
     * 获取当前日期星期日日期
     *
     * @return date
     */
    public static Date getLastDayOfWeek() {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Monday
        return calendar.getTime();
    }

    /**
     * 获取日期星期一日期
     *
     * @return date
     */
    public static Date getFirstDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek()); // Monday
        return calendar.getTime();
    }

    /**
     * 获取日期星期一日期
     *
     * @param date 指定日期
     * @return date
     */
    public static Date getLastDayOfWeek(Date date) {
        if (date == null) {
            return null;
        }
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6); // Monday
        return calendar.getTime();
    }

    /**
     * 获取当前月的第一天
     *
     * @return date
     */
    public static Date getFirstDayOfMonth() {
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取当前月的最后一天
     *
     * @return
     */
    public static Date getLastDayOfMonth() {
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取指定月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取指定月的最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 获取日期前一天
     *
     * @param date
     * @return
     */
    public static Date getDayBefore(Date date) {
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);
        return calendar.getTime();
    }

    /**
     * 获取日期后一天
     *
     * @param date
     * @return
     */
    public static Date getDayAfter(Date date) {
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day + 1);
        return calendar.getTime();
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getNowYear() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getNowMonth() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getNowDay() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前小时
     *
     * @return
     */
    public static int getNowHour() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getNowMinute() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.MINUTE);
    }
    /**
     * 获取周几
     *
     * @return
     */
    public static int getNowWeek() {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 获取当月天数
     *
     * @return
     */
    public static int getNowMonthDay() {
        Calendar d = Calendar.getInstance();
        return d.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取时间段的每一天
     *
     * @param startDate 开始日期
     * @param endDate 结算日期
     * @return List<Date> 日期列表
     */
    public static List<Date> getEveryDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        // 格式化日期(yy-MM-dd)
        startDate = DateUtil.getDateFormat(DateUtil.getDateFormat(startDate));
        endDate = DateUtil.getDateFormat(DateUtil.getDateFormat(endDate));
        List<Date> dates = new ArrayList<Date>();
        calendar.setTime(startDate);
        dates.add(calendar.getTime());
        while (calendar.getTime().compareTo(endDate) < 0) {
            // 加1天
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(calendar.getTime());
        }
        return dates;
    }

    /**
     * 获取提前多少个月
     * @param monty
     * @return
     */
    public static Date getPastMonth(int monty) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -monty);
        return c.getTime();
    }

    /**
     * 获取未来多少个月
     * @param monty
     * @return
     */
    public static Date getFutureMonth(int monty) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, monty);
        return c.getTime();
    }

    public static int getIntervalDays(Date date1,Date date2){
        long diff = date2.getTime() - date1.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return (int)days;
    }
}