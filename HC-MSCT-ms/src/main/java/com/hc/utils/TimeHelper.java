package com.hc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017-05-25.
 */
public class TimeHelper {

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




}
