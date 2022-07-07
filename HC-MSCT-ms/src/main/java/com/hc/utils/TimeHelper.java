package com.hc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017-05-25.
 */
public class TimeHelper {

    protected static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat datetimeformat = new SimpleDateFormat("HH:mm");
    protected static SimpleDateFormat dateformats = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    protected static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getNowDate(Date date){
        return formatter.format(date);
    }


    /**
     * 时间比较方法
     */
    public static double getDatePoor(Date nowDate, Date endDate) {
        double nd = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        return diff / nd;

    }

    /**
     * 获取与指定时间的差值
     * @param endDate
     * @return
     */
    public static double getDatePoorMin(Date endDate) {
        double nd = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        Date nowDate = new Date();
        long diff = nowDate.getTime() - endDate.getTime();
        // 计算差多少秒//输出结果
        return diff / nd;

    }

    public static void main(String[] args) throws InterruptedException {
        Date date = new Date();
        Thread.sleep(5000);
        double datePoor = getDatePoor(date, new Date());
        System.out.println(datePoor);


    }





    /**
     * String类型当前时间比较方法
     */
    public static double getDatePoorMinByString(String startTime,String endTime){
        double nd = 1000 * 60;
// 获得两个时间的毫秒时间差异

        long diff = 0;
        try {
            diff = dateformats.parse(endTime).getTime() - dateformats.parse(startTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double diffe = diff;


        double s = diff / nd;

// 计算差多少秒//输出结果

// long sec = diff % nd % nh % nm / ns;

        return s;
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        return dateformats.format(new Date());
    }

    public static Date dateHelp(Date date) {
        try {
            return datetimeformat.parse(datetimeformat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String getDateString(Date date) {
        String format = dateformat.format(date);
        return format;
    }

//    public static void main(String [] args) {
//        Date date = new Date();
//        String format = dateformat.format(date);
//        System.out.println(format);
//    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return datetimeformat.format(new Date());
    }

    public static Date getCurrentTime() {
        Date parse = null;
        try {
            parse = datetimeformat.parse(getCurrentDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static Date getDateTime(String date) {
        try {
            return dateformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间增加方法
     */
    public static String getCurrentDateAdd(Integer time) {
        Calendar now = Calendar.getInstance();

        now.add(Calendar.MINUTE, time);


        String dateStr = datetimeformat.format(now.getTimeInMillis());
        return dateStr;
    }

    public static String dateDD() {

        Date parse = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();
        String format = dateformat.format(time);
        return format;

    }


}
