package com.hc.my.common.core.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

    //正则判断字符串有无数字
    public static boolean checkContainsNumbers(String searchParam) {
        //正则判断字符串有无数字
        String haveNum = ".*\\d+.*";
        Pattern pattern = Pattern.compile(haveNum);
        Matcher match = pattern.matcher(searchParam);
        return match.find();
    }

    public static void  main(String[] args){
     System.out.println(checkContainsNumbers("881e"));

    }
}
