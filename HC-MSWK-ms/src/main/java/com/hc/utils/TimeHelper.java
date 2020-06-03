package com.hc.utils;

import javax.xml.crypto.Data;
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

    /**
     * 获取当前时间
     */
    public static String getCurrentTimes(){
        String format = dateformats.format(new Date());
        return format;
    }

    /**
     * 时间比较方法
     */
    public static double  getDatePoorMs(Date endDate, Date nowDate) {

        double nd = 1000  ;
// 获得两个时间的毫秒时间差异

        long diff = endDate.getTime() - nowDate.getTime();
        double diffe = diff;



        double s = diff/nd;

// 计算差多少秒//输出结果

// long sec = diff % nd % nh % nm / ns;

        return s;

    }

    /**
     * 时间比较方法
     */
    public static double  getDatePoor(Date endDate, Date nowDate) {

        double nd = 1000 * 60 ;
// 获得两个时间的毫秒时间差异

        long diff = endDate.getTime() - nowDate.getTime();
        double diffe = diff;



        double min = diff/nd;

// 计算差多少秒//输出结果

// long sec = diff % nd % nh % nm / ns;

        return min;

    }
    /**
     * 时间比较方法
     */
    public static int  getDatePoors( String args) {

        int nd = 1000 * 60 ;
// 获得两个时间的毫秒时间差异

        long diff = 0 ;
        try {
            diff = new Date().getTime() - dateformats.parse(args).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
      //  double diffe = diff;



        int min =(int) diff/nd;
        return min;
// 计算差多少秒//输出结果

// long sec = diff % nd % nh % nm / ns;

   //     System.out.println(min);

    }
    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate(){
        return dateformat.format(new Date());
    }

    public static Date dateHelp(Date date){
        try {
          return  datetimeformat.parse(datetimeformat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentDateTime(){
        return datetimeformat.format(new Date());
    }
    public static Date getCurrentTime(){
        Date parse = null;
        try {
            parse = datetimeformat.parse(getCurrentDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }
   public static Date getDateTime(String date){
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
    public static String getCurrentDateAdd(Integer time){
        Calendar now=Calendar.getInstance();

        now.add(Calendar.MINUTE,time);



        String dateStr=datetimeformat.format(now.getTimeInMillis());
        return dateStr;
    }


}
