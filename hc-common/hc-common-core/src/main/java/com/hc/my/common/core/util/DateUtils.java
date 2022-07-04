package com.hc.my.common.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author LiuZhiHao
 * @date 2020/7/13 9:54
 * 描述:
 **/
public class DateUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dateFormatHHmm = new SimpleDateFormat("HH:mm");


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @author liu
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        String format1 = simpleDateFormat.format(nowTime);
        String format2 = simpleDateFormat.format(startTime);
        String format3 = simpleDateFormat.format(endTime);
        try {
            Date parse1 = simpleDateFormat.parse(format1);
            Date parse2 = simpleDateFormat.parse(format2);
            Date parse3 = simpleDateFormat.parse(format3);
            Calendar date = Calendar.getInstance();
            date.setTime(parse1);

            Calendar begin = Calendar.getInstance();
            begin.setTime(parse2);

            Calendar end = Calendar.getInstance();
            end.setTime(parse3);
            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 解析时间
     *
     * @param nowTime 当前时间 yyyy-mm-dd
     * @author liu
     */
    public static String paseDate(Date nowTime) {
        return dateFormat.format(nowTime);

    }

    /**
     * 解析时间
     *
     * @param nowTime 当前时间 yyyy-MM-dd HH:mm:ss
     * @author liu
     */
    public static String paseDatetime(Date nowTime) {
        return datetimeFormat.format(nowTime);

    }

    /**
     * 解析时间
     *
     * @param nowTime 当前时间 HH:mm
     * @author liu
     */
    public static String parseDatetime(Date nowTime){
        return dateFormatHHmm.format(nowTime);
    }

    /**
     * 解析时间
     * @param date
     * @return  "HH:mm:ss"
     */
    public static String paseDateHHmmss(Date date) {
        return simpleDateFormat.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public String getNowDate() {
        return datetimeFormat.format(new Date());
    }

    /**
     * @return 一个月以前的当前日期
     */
    public String getCurrentDateTimeBeforeOneMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return datetimeFormat.format(m);
    }

    /**
     * 获取指定时间前一天
     *
     * @param day 指定时间
     * @author liu
     */
    public static String getYesterday(Date day) {
        long ms = day.getTime() - 1 * 24 * 3600 * 1000L;
        Date prevDay = new Date(ms);
        return dateFormat.format(prevDay);
    }

    //根据输入秒数,计算时间差
    public static boolean calculateIntervalTime(Date nowTime, Date wornTime, int seconds) {
        long timeInterval = (nowTime.getTime() - wornTime.getTime()) / (1000);
        return timeInterval > seconds;
    }

    public static void main(String[] args) throws ParseException {
//        Date startTime = simpleDateFormat.parse("09:00:00");
//        Date endTime = simpleDateFormat.parse("19:50:59");
//        System.out.println(isEffectiveDate(new Date(), startTime, endTime));
//        System.out.println(getYesterday(new Date()));
//        List<String> list = Arrays.asList("1","2");
//        List<String> list2 = Arrays.asList("4","1");
//        list=list2;
//        System.out.println(list);
        String nowDate = new DateUtils().getNowDate();
        System.out.println(nowDate);
    }

    /**
     * 以 yyyy-MM-dd HH:mm:ss 的格式解析String
     * @param str
     * @return
     */
    public static Date parseDate(String str){
        Date date ;
        try {
            date = datetimeFormat.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 获取前一个小时的时间
     * @param date
     * @return "HH-mm"
     */
    public static String getPreviousHourHHmm(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY,-1);
        Date time = cal.getTime();
        return parseDatetime(time);
    }

    /**
     * 获取前一个小时的时间
     * @param date
     * @return "HH:mm:ss"
     */
    public static String getPreviousHourHHmmss(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY,-1);
        Date time = cal.getTime();
        return simpleDateFormat.format(time);
    }

    /**
     *  转换日期格式
     * @param date
     * @return "HH-mm"
     */
    public static String dateReduceHHmm(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Date time = cal.getTime();
        return parseDatetime(time);
    }

    /**
     * 获取时间的年月
     * @param date
     * @return
     */
    public static String getYearMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        return month>9 ? year+"-"+month : year+"-0"+month;
    }

    /**
     * 将时间转化为 yyyy-MM-dd 类型
     * @param time
     * @return
     */
    public static Date getYearMonthDate(Date time)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        int date = cal.get(Calendar.HOUR_OF_DAY);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,date);
        return calendar.getTime();
    }
}
