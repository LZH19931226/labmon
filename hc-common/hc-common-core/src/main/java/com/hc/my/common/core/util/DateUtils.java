package com.hc.my.common.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author LiuZhiHao
 * @date 2020/7/13 9:54
 * 描述:
 **/
public class DateUtils {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
     * @param nowTime   当前时间 yyyy-mm-dd
     * @author liu
     */
    public static String paseDate(Date nowTime) {
       return dateFormat.format(nowTime);

    }





    /**
     * 获取指定时间前一天
     * @param day   指定时间
     * @author liu
     */
    public static String getYesterday(Date day){
        long ms = day.getTime() - 1*24*3600*1000L;
        Date prevDay = new Date(ms);
        return dateFormat.format(prevDay);
    }

    public static void main(String[] args) throws ParseException {
//        Date startTime = simpleDateFormat.parse("09:00:00");
//        Date endTime = simpleDateFormat.parse("19:50:59");
//        System.out.println(isEffectiveDate(new Date(), startTime, endTime));
//        System.out.println(getYesterday(new Date()));
        List<String> list = Arrays.asList("1","2");
        List<String> list2 = Arrays.asList("4","1");
        list=list2;
        System.out.println(list);
    }
}
