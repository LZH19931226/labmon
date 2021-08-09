package com.hc.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017-05-25.
 */
public class TimeHelper {

    protected static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    protected static SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected static SimpleDateFormat dateFormats = new SimpleDateFormat("HH:mm");

    public static String formats(Date date) {
        String format = dateFormats.format(date);

        return format;

    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        return dateformat.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return datetimeformat.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDateTimeBeforOneMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date m = c.getTime();
        return datetimeformat.format(m);

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


    public static Date getDateTimeFormat(String date) {
        try {
            return datetimeformat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateReduce(String date) {
        try {
            Date parse = datetimeformat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.HOUR, -1);
            Date time = calendar.getTime();
            String format = datetimeformat.format(time);
            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将时间转化为时分
     *
     * @param date
     * @return
     */
    public static String dateReduceHHmm(String date) {
        try {
            Date parse = datetimeformat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            //calendar.add(Calendar.HOUR, -1);
            Date time = calendar.getTime();
            String format = dateFormats.format(time);
            return format;
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

    /**
     * 判断当前时间是否在这个月
     */

    public static boolean isCurrentMonth(String date) {
        String format = dateformat.format(new Date());
        if (StringUtils.equals(format.substring(0, 7), date.substring(0, 7))) {
            return true;
        } else {
            return false;
        }

    }

    public static void main(String args[]) {
        String format = dateFormats.format(new Date());
        System.out.println(format);
        System.out.println();

        try {
            Date parse = dateFormats.parse(format);
            System.out.println(parse);
            // return parse;
        } catch (ParseException e) {
            e.printStackTrace();
            //  return null;
        }
    }

}
