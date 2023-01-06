package com.hc.my.common.core.util;

import com.hc.my.common.core.util.date.DateConstant;
import com.hc.my.common.core.util.date.DateDto;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

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
    private static SimpleDateFormat dateFormatMMdd = new SimpleDateFormat("MM-dd");



    /**
     * 当前时间是否在此时间区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 当前时间是否在此时间区间时分内
     * 例如： 2022-11-12 01:00:00(nowTime)   2022-11-11 00:00:00(startTime) 2022-11-11 02:00:00(endTime) true
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean whetherItIsIn(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        date.set(Calendar.YEAR,2022);
        date.set(Calendar.MONTH,1);
        date.set(Calendar.DATE,1);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        begin.set(Calendar.YEAR,2022);
        begin.set(Calendar.MONTH,1);
        begin.set(Calendar.DATE,1);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        end.set(Calendar.YEAR,2022);
        end.set(Calendar.MONTH,1);
        end.set(Calendar.DATE,1);
        //统一年月日后对比
        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
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
     * 解析时间
     * @param date
     * @return "MM-dd"
     */
    public static String paseDateMMdd(Date date){
        return dateFormatMMdd.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
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
//        Date date = DateUtils.initDateByDay();
//        for (int i = 1; i <= 12; i++) {
//            Date addHour = DateUtils.getAddHour(date, 2 * (i - 1));
//            Date endTime ;
//            if (i==12){
//                endTime =DateUtils.getEndOfDay();
//            }else {
//                endTime =DateUtils.getAddHour(date, i*2);
//            }
//            System.out.println(addHour+"======"+endTime);
//        }
        System.out.println(getCurrentYYMM());
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
     * 以 yyyy-MM 的格式解析String
     * @param str
     * @return year+month
     */
    public static String parseDateYm(String str){
        Date date = parseDate(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);
        return month >9 ? ""+year+month : ""+year+"0"+month;
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
     * 获取前一个小时的时间
     * @param date
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static Date getPreviousHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY,-1);
        return cal.getTime();
    }

    /**
     * 获得当天零时零分零秒
     * @return
     */
    public static Date initDateByDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    // 获得某天最大时间 2022-12-09 23:59:59
    public static Date getEndOfDay() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(new Date().getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取累加之后的时间
     * @param date
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static Date getAddHour(Date date,int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY,hour);
        return cal.getTime();
    }


    /**
     *  转换日期格式
     * @param date
     * @return "HH:mm"
     */
    public static String dateReduceHHmm(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String h = hours > 9 ? String.valueOf(hours) : "0"+hours;
        return minute > 9 ? h+":"+minute : h+":0"+minute;
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

    /**
     * 获取两个时间相隔的时间
     *
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 时间对象{年，月，日，时，分，秒}
     */
    public static DateDto convert(Date startDate, Date endDate) {
        //获取开始时间毫秒数
        long startTime = startDate.getTime();
        //获取结束毫秒数
        long endTime = endDate.getTime();
        long timeDifference = endTime-startTime;
        //计算秒
        long time = (timeDifference/1000);
        return getDateDto(time);
    }

    /**
     * 获取时间对象
     * @param obj Date或long类型
     * @return 时间对象{年，月，日，时，分，秒}
     */
    public static DateDto getDateDto(Object obj){
        return obj instanceof Long ? getDateDto(((Long) obj)) :
                obj instanceof Date ? getDateDto(((Date)obj).getTime()/1000)  : null;
    }

    /**
     * 获取时间对象
     * @param time 毫秒
     * @return
     */
    public static DateDto getDateDto(Long time){
        DateDto dateDto = new DateDto();
        if(time< DateConstant.SECOND_THRESHOLD){
            //设置秒
            dateDto.setSecond(time);
        }else{
            long minute =  time/DateConstant.SECOND_THRESHOLD;
            if(minute < DateConstant.MINUTE_THRESHOLD){
                //设置分
                dateDto.setMinute(minute);
                dateDto.setSecond(time%DateConstant.SECOND_THRESHOLD);
            }else {
                long hour = minute/DateConstant.MINUTE_THRESHOLD;
                if(hour < DateConstant.HOUR_THRESHOLD){
                    //设置时
                    dateDto.setHour(hour);
                }else {
                    long date = hour/DateConstant.HOUR_THRESHOLD;
                    if(date < DateConstant.DATE_THRESHOLD){
                        //设置日
                        dateDto.setDate(date);
                    }else {
                        long month = date/DateConstant.DATE_THRESHOLD;
                        if(month<DateConstant.MONTH_THRESHOLD){
                            //设置月
                            dateDto.setMonth(month);
                        }else {
                            Long year = month/DateConstant.MONTH_THRESHOLD;
                            //设置年
                            dateDto.setYear(year);
                            dateDto.setMonth(month%DateConstant.MONTH_THRESHOLD);
                        }
                        dateDto.setDate(date%DateConstant.DATE_THRESHOLD);
                    }
                    dateDto.setHour(hour%DateConstant.HOUR_THRESHOLD);
                }
                dateDto.setMinute(minute%DateConstant.MINUTE_THRESHOLD);
                dateDto.setSecond(time%DateConstant.SECOND_THRESHOLD);
            }
        }
        return dateDto;
    }

    public static int getCurrentYYMM(){
        LocalDateTime now = LocalDateTime.now();
        int month =now.getMonth().getValue();
        int year = now.getYear();
        String mon = String.valueOf(month);
        if(mon.length()==1){
            mon="0"+mon;
        }
        String yymm = year+mon;
        return Integer.parseInt(yymm);
    }

    /**
     * 获取年份
     * @param time 格式为yyyy-MM-dd
     * @return yyyy
     */
    public static int getYear(String time){
        Date date = parseDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     * @param time yyyy-MM-dd
     * @return mm
     */
    public static int getMonth(String time){
        Date date = parseDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH)+1;
    }

    /**
     * 获取两个时间之间所有的年月集合
     *  开始时间不能大于结束时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return '202201','202202'...
     */
    public static String getYearMonth(String startTime,String endTime){
        String startYm = DateUtils.parseDateYm(startTime);
        String endYm = DateUtils.parseDateYm(endTime);
        //相同时任意返回一个
        if(startYm.equals(endYm)){
            return startYm;
        }

        int startYear = DateUtils.getYear(startTime);
        int endYear = DateUtils.getYear(endTime);
        List<String> list = new ArrayList<>();
        //获取月
        int startMonth = DateUtils.getMonth(startTime);
        int endMonth = DateUtils.getMonth(endTime);
        list.add(startMonth>9?""+startYear+startMonth:""+startYear+"0"+startMonth);
        Date date = DateUtils.parseDate(startTime);
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        while(true){
            cal.add(Calendar.MONTH,1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) +1;
            if(year == endYear){
                if(month>endMonth){
                    break;
                }
            }
            list.add( month > 9 ? ""+ year + month: ""+year+"0"+month);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append("'");
            stringBuilder.append(s);
            stringBuilder.append("'");
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }


    /**
     * 获取时间的月份和年
     * @param time YYYY-MM-dd HH:mm:ss
     * @return MM-dd
     */
    public static String getMMdd(String time){
        Date date = parseDate(time);
        return dateFormatMMdd.format(date);
    }
}
