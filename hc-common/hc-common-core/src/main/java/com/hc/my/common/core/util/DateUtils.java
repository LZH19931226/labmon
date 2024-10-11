package com.hc.my.common.core.util;

import com.hc.my.common.core.util.date.DateConstant;
import com.hc.my.common.core.util.date.DateDto;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author LiuZhiHao
 * @date 2020/7/13 9:54
 * 描述:
 **/
public class DateUtils {


    //将date类型转换指定时区的日期数据
    public static String designatedAreaDate(Date date,String zone){
        if (StringUtils.isEmpty(zone)){
            zone= "America/Phoenix";
        }
        ZoneId of = ZoneId.of(zone);
        TimeZone timeZone = TimeZone.getTimeZone(of);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    public static Date designatedAreaDateLog(Date date,String zone){
        if (StringUtils.isEmpty(zone)){
            zone= "America/Phoenix";
        }
        ZoneId of = ZoneId.of(zone);
        TimeZone timeZone = TimeZone.getTimeZone(of);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        String format = sdf.format(date);
        return parseDate(format);
    }

    //将date类型转换指定时区的日期数据
    public static String designatedAreaString(Date date,String zone){
        if (StringUtils.isEmpty(zone)){
            zone= "America/Phoenix";
        }
        ZoneId of = ZoneId.of(zone);
        TimeZone timeZone = TimeZone.getTimeZone(of);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }

    /**
     * 过滤数组元素,是否满足对应日期格式
     *
     * @param list
     * @return
     */
    public static List<String> filterDate(List<String> list) {
        List<String> timeList = new ArrayList<>();
        list.forEach(s -> {
            if (paseDatetimeFormat(s)){
                timeList.add(s);
            }
        });
        return timeList;
    }

    public static boolean paseDatetimeFormat(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
             simpleDateFormat.parse(time);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


    /**
     * 当前时间是否在此时间区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return 不再工作时间内返回true,在工作时间内返回false
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
     * 当前时间是否在此时间区间内
     *
     * @param nowTime1
     * @param startTime2
     * @param endTime3
     * @return 不再工作时间内返回true,在工作时间内返回false
     */
    public static boolean isEffectiveHhMm(Date nowTime1, Date startTime2, Date endTime3){
        List<Date> nowTime = sameDate(nowTime1);
        List<Date> dateInterval = sameDate(startTime2, endTime3);
        return DateUtils.isEffectiveDate(nowTime.get(0), dateInterval.get(0), dateInterval.get(1));
    }

    /**
     * 修改时间的年月日
     */
    private static List<Date> sameDate(Date... dates) {
        if (dates != null) {
            Calendar nowCalendar = Calendar.getInstance();
            List<Date> dateList = new ArrayList<Date>();
            for (int i = 0; i < dates.length; i++) {
                Date date = dates[i];
                if (date == null) {
                    continue;
                }
                nowCalendar.setTime(date);
                nowCalendar.set(Calendar.YEAR, 1970);
                nowCalendar.set(Calendar.MONTH, 12);
                nowCalendar.set(Calendar.DAY_OF_MONTH, 12);
                Date nowTime = nowCalendar.getTime();
                dateList.add(i, nowTime);
            }
            return dateList;
        }
        return null;
    }



    /**
     * 当前时间是否在此时间区间时分内
     * （只在同月生效）,只在同月生效,只在同月生效 重要 说三遍
     * 例如： 2022-11-12 01:00:00(nowTime)   2022-11-11 00:00:00(startTime) 2022-11-11 02:00:00(endTime) true
     *
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
        date.set(Calendar.YEAR, 2022);
        date.set(Calendar.MONTH, 1);
        date.set(Calendar.DATE, 1);
        Date time1 = date.getTime();

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);
        begin.set(Calendar.YEAR, 2022);
        begin.set(Calendar.MONTH, 1);
        begin.set(Calendar.DATE, 1);
        Date time2 = begin.getTime();

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        end.set(Calendar.YEAR, 2022);
        end.set(Calendar.MONTH, 1);
        end.set(Calendar.DATE, 1);
        Date time3 = end.getTime();
        //统一年月日后对比
        if (time1.after(time2) && time1.before(time3)) {
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(nowTime);

    }

    public static String paseDate(Date nowTime,String zone) {
        if (StringUtils.isEmpty(zone)){
            zone= "America/Phoenix";
        }
        ZoneId of = ZoneId.of(zone);
        TimeZone timeZone = TimeZone.getTimeZone(of);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(timeZone);
        return sdf.format(nowTime);

    }

    /**
     * 解析时间
     *
     * @param nowTime 当前时间 yyyy-MM-dd HH:mm:ss
     * @author liu
     */
    public static String paseDatetime(Date nowTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(nowTime);

    }

    /**
     * 解析时间
     *
     * @param nowTime 当前时间 HH:mm
     * @author liu
     */
    public static String parseDatetime(Date nowTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        return simpleDateFormat.format(nowTime);
    }


    /**
     * 解析时间
     *
     * @param date
     * @return "MM-dd"
     */
    public static String paseDateMMdd(Date date) {
        SimpleDateFormat dateFormatMMdd = new SimpleDateFormat("MM-dd");
        return dateFormatMMdd.format(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取两个时间段的所有月日
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 01-06 01-07
     */
    public static List<String> getTimePeriod(String startTime, String endTime) {
        SimpleDateFormat dateFormatMMdd = new SimpleDateFormat("MM-dd");
        List<String> list = new ArrayList<>();
        Date start = DateUtils.parseDate(startTime);
        Date end = DateUtils.parseDate(endTime);
        while (start.before(end)) {
            String md = dateFormatMMdd.format(start);
            list.add(md);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.DATE, 1);
            start = cal.getTime();
        }
        return list;
    }

    /**
     * 获取指定时间前一天
     *
     * @param day 指定时间
     * @author liu
     */
    public static String getYesterday(Date day) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long ms = day.getTime() - 1 * 24 * 3600 * 1000L;
        Date prevDay = new Date(ms);
        return simpleDateFormat.format(prevDay);
    }

    //根据输入秒数,计算时间差
    public static boolean calculateIntervalTime(Date nowTime, Date wornTime, int seconds) {
        long timeInterval = (nowTime.getTime() - wornTime.getTime()) / (1000);
        return timeInterval > seconds;
    }

    /**
     * 根据输入分钟,计算输入时间与当前时间的时间差
     *
     * @param wornTime 输入时间
     * @param minute
     * @return
     */
    public static boolean calculateIntervalTime(Date wornTime, String minute) {
        int i = Integer.parseInt(minute);
        long timeInterval = (new Date().getTime() - wornTime.getTime()) / (1000 * 60);
        return timeInterval > i;
    }

    public static void main(String[] args) throws ParseException, InterruptedException {
//        Date date = designatedAreaDate(new Date(), "America/Chicago");
//        String hHmm = getHHmm("2024-10-11 20:04:00", "America/Chicago");
//        System.out.println(hHmm);
        String time ="2024-10-11 20:04:00";
        System.out.println(time.substring(11,16));
    }

    public static Date getChinaTime(){
        // 获取中国时区
        TimeZone chinaTimeZone = TimeZone.getTimeZone("GMT+8");
        // 使用中国时区的Calendar实例
        Calendar calendar = Calendar.getInstance(chinaTimeZone);
        // 获取当前时间的Date对象
        return calendar.getTime();
    }


    /**
     * 以 yyyy-MM-dd HH:mm:ss 的格式解析String
     *
     * @param str
     * @return
     */
    public static Date parseDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = simpleDateFormat.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    /**
     * 以 yyyy-MM 的格式解析String
     *
     * @param str
     * @return year+month
     */
    public static String parseDateYm(String str) {
        Date date = parseDate(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        return month > 9 ? "" + year + month : "" + year + "0" + month;
    }

    /**
     * 获取前一周得时间
     *
     * @param date
     * @return "HH-mm"
     */
    public static String getPreviousHour(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, -(24*7));
        Date time = cal.getTime();
        return paseDatetime(time);
    }


    /**
     * 获得当天零时零分零秒
     *
     * @return
     */
    public static Date initDateByDay() {
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
     *
     * @param date
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static Date getAddHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }


    /**
     * 转换日期格式
     *
     * @param date
     * @return "HH:mm"
     */
    public static String dateReduceHHmm(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        String h = hours > 9 ? String.valueOf(hours) : "0" + hours;
        return minute > 9 ? h + ":" + minute : h + ":0" + minute;
    }

    /**
     * 获取时间的年月
     *
     * @param date
     * @return
     */
    public static String getYearMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return month > 9 ? year + "-" + month : year + "-0" + month;
    }


    /**
     * 获取两个时间相隔的时间
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 时间对象{年，月，日，时，分，秒}
     */
    public static DateDto convert(Date startDate, Date endDate) {
        //获取开始时间毫秒数
        long startTime = startDate.getTime();
        //获取结束毫秒数
        long endTime = endDate.getTime();
        long timeDifference = endTime - startTime;
        //计算秒
        long time = (timeDifference / 1000);
        return getDateDto(time);
    }


    /**
     * 获取时间对象
     *
     * @param time 毫秒
     * @return
     */
    public static DateDto getDateDto(Long time) {
        DateDto dateDto = new DateDto();
        if (time < DateConstant.SECOND_THRESHOLD) {
            //设置秒
            dateDto.setSecond(time);
        } else {
            long minute = time / DateConstant.SECOND_THRESHOLD;
            if (minute < DateConstant.MINUTE_THRESHOLD) {
                //设置分
                dateDto.setMinute(minute);
                dateDto.setSecond(time % DateConstant.SECOND_THRESHOLD);
            } else {
                long hour = minute / DateConstant.MINUTE_THRESHOLD;
                if (hour < DateConstant.HOUR_THRESHOLD) {
                    //设置时
                    dateDto.setHour(hour);
                } else {
                    long date = hour / DateConstant.HOUR_THRESHOLD;
                    if (date < DateConstant.DATE_THRESHOLD) {
                        //设置日
                        dateDto.setDate(date);
                    } else {
                        long month = date / DateConstant.DATE_THRESHOLD;
                        if (month < DateConstant.MONTH_THRESHOLD) {
                            //设置月
                            dateDto.setMonth(month);
                        } else {
                            Long year = month / DateConstant.MONTH_THRESHOLD;
                            //设置年
                            dateDto.setYear(year);
                            dateDto.setMonth(month % DateConstant.MONTH_THRESHOLD);
                        }
                        dateDto.setDate(date % DateConstant.DATE_THRESHOLD);
                    }
                    dateDto.setHour(hour % DateConstant.HOUR_THRESHOLD);
                }
                dateDto.setMinute(minute % DateConstant.MINUTE_THRESHOLD);
                dateDto.setSecond(time % DateConstant.SECOND_THRESHOLD);
            }
        }
        return dateDto;
    }

    public static int getCurrentYYMM() {
        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonth().getValue();
        int year = now.getYear();
        String mon = String.valueOf(month);
        if (mon.length() == 1) {
            mon = "0" + mon;
        }
        String yymm = year + mon;
        return Integer.parseInt(yymm);
    }

    /**
     * 获取年份
     *
     * @param time 格式为yyyy-MM-dd
     * @return yyyy
     */
    public static int getYear(String time) {
        Date date = parseDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param time yyyy-MM-dd
     * @return mm
     */
    public static int getMonth(String time) {
        Date date = parseDate(time);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        return instance.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取两个时间之间所有的年月集合
     * 开始时间不能大于结束时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return '202201','202202'...
     */
    public static String getYearMonth(String startTime, String endTime) {
        String startYm = DateUtils.parseDateYm(startTime);
        String endYm = DateUtils.parseDateYm(endTime);
        //相同时任意返回一个
        if (startYm.equals(endYm)) {
            return startYm;
        }

        int startYear = DateUtils.getYear(startTime);
        int endYear = DateUtils.getYear(endTime);
        List<String> list = new ArrayList<>();
        //获取月
        int startMonth = DateUtils.getMonth(startTime);
        int endMonth = DateUtils.getMonth(endTime);
        list.add(startMonth > 9 ? "" + startYear + startMonth : "" + startYear + "0" + startMonth);
        Date date = DateUtils.parseDate(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        while (true) {
            cal.add(Calendar.MONTH, 1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            if (year == endYear) {
                if (month > endMonth) {
                    break;
                }
            }
            list.add(month > 9 ? "" + year + month : "" + year + "0" + month);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append("'");
            stringBuilder.append(s);
            stringBuilder.append("'");
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }


    /**
     * 获取时间的月份和年
     *
     * @param time YYYY-MM-dd HH:mm:ss
     * @return MM-dd
     */
    public static String getMMdd(String time) {
        SimpleDateFormat dateFormatMMdd = new SimpleDateFormat("MM-dd");
        Date date = parseDate(time);
        return dateFormatMMdd.format(date);
    }


    /**
     * 传入string的时间 改为String年月日返回
     */
    public static String getYMD(String time) {
        SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
        Date date = parseDate(time);
        return dateFormatYMD.format(date);
    }

    /**
     * 获取时间的月份和年
     *
     * @param time YYYY-MM-dd HH:mm:ss
     * @return HH:mm
     */
    public static String getHHmm(String time) {
        return time.substring(11,16);
    }
//    public static String getHHmm(Date time,String zone) {
//        if (StringUtils.isEmpty(zone)){
//            zone= "America/Phoenix";
//        }
//        ZoneId of = ZoneId.of(zone);
//        TimeZone timeZone = TimeZone.getTimeZone(of);
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
//        sdf.setTimeZone(timeZone);
//        return  sdf.format(time);
//    }

    public static List<String> getBetweenDate(String beginDate, String endDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(simpleDateFormat.parse(beginDate));
            for (long d = cal.getTimeInMillis(); d <= simpleDateFormat.parse(endDate).getTime(); d = get_D_Plaus_1(cal)) {
                String format = simpleDateFormat.format(d);
                dates.add(format);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates;
    }

    public static long get_D_Plaus_1(Calendar c) {
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        return c.getTimeInMillis();
    }
}
