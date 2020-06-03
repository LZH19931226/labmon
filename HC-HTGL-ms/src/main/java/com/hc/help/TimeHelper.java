package com.hc.help;

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

    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate(){
        return dateformat.format(new Date());
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
