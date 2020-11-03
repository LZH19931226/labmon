package com.hc.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class paramaterModelUtils {
    // 解析温度
    public static String temperature(String data) {
        String rule = "";
        // 16进制转2进制每一位都要转然后拼接
        String hexadecima11 = MathUtil.hexadecimal(data.substring(0, 1));
        String hexadecimal2 = MathUtil.hexadecimal(data.substring(1, 2));
        String hexadecimal3 = MathUtil.hexadecimal(data.substring(2, 3));
        String hexadecimal4 = MathUtil.hexadecimal(data.substring(3, 4));
        String hexadecimal = hexadecima11 + hexadecimal2 + hexadecimal3 + hexadecimal4;
        // 判断正负
        if (StringUtils.equals(hexadecimal.substring(0, 1), "1")) {
            rule = "-";
        }
        // 二进制转10进制(占位符除外)
        int a = Integer.parseInt(hexadecimal.substring(1, hexadecimal.length()), 2);
        int b = 100;
        Double f = (double) a / b;
        return rule + f.toString();
    }

    public static String temperature10(String data) {
        String rule = "";
        // 16进制转2进制每一位都要转然后拼接
        String hexadecima11 = MathUtil.hexadecimal(data.substring(0, 1));
        String hexadecimal2 = MathUtil.hexadecimal(data.substring(1, 2));
        String hexadecimal3 = MathUtil.hexadecimal(data.substring(2, 3));
        String hexadecimal4 = MathUtil.hexadecimal(data.substring(3, 4));
        String hexadecimal = hexadecima11 + hexadecimal2 + hexadecimal3 + hexadecimal4;
        // 判断正负
        if (StringUtils.equals(hexadecimal.substring(0, 1), "1")) {
            rule = "-";
        }
        // 二进制转10进制(占位符除外)
        int a = Integer.parseInt(hexadecimal.substring(1, hexadecimal.length()), 2);
        int b = 10;
        double f = (double) a / b;
        return rule + f;
    }


    // 解析电量/市电/开门记录/压力/PM2.5/PM10/甲醛
    public static String electricity(String data) {
        // 16进制转10进制
        int parseInt1 = Integer.parseInt(data, 16);
        return String.valueOf(parseInt1);

    }

    // MT400甲醛
    public static String electricity2(String data) {
        // 16进制转10进制
        Integer a = Integer.parseInt(data, 16);
   //     int b = 1000;
        return chu(a,"1000");
//        Double f = (double) a / b;
//        return f.toString();
    }


    // MT400 pm2.5 pm5.0
    public static String electricity1(String data) {
        // 16进制转10进制
        Integer a = Integer.parseInt(data, 16);
      //  int b = 10;
        return new BigDecimal(a).multiply(new BigDecimal("10")).toString();
//        Double f = (double) a * b;
//        return f.toString();

    }


    // 解析co2/02/湿度/VOC
    public static String gas(String data) {
        Integer a = Integer.parseInt(data, 16);
        // ppb 转换 PPM要转化
//        if (a > 100000) {
//            return "A";
//        }
  //      int b = 100;
        return chu(a,"100");
//        Double f = (double) a / b;
//        return f.toString();
    }
    // 解析co2/02/湿度/VOC
    public static String gas10(String data) {
        Integer a = Integer.parseInt(data, 16);
        // ppb 转换 PPM要转化
//        if (a > 100000) {
//            return "A";
//        }
        //      int b = 100;
        return chu(a,"10");
//        Double f = (double) a / b;
//        return f.toString();
    }
    public static String chu(Integer a,String b){
        return new BigDecimal(a).divide(new BigDecimal(b), 3, BigDecimal.ROUND_HALF_UP).toString();


    }

    public static void  main(String atgs[]){
        System.out.println(gas("4843a51a32303136303630303138f000f000f000f000f0000000000000004e23"));

    }
}
