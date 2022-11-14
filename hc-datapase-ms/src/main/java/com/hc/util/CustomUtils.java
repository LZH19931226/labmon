package com.hc.util;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import com.hc.my.common.core.util.RegularUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 判断
 * Created by xxf on 2019-04-08.
 */
public class CustomUtils {
    /**
     * 85 温度判断是否在有效值内
     *
     * @return
     */
    public static String tem85(String data, String sn) {
        if (!RegularUtil.checkContainsNumbers(data)){
             return data;
        }
        String substring = sn.substring(4, 6);
        if (StringUtils.equalsAny(substring, "02", "17", "18", "19", "05")) {
            //MT200  -200 -- 40
            boolean comparison = comparison(data, "-200", "40");
            if (comparison) {
                return ProbeOutlier.THE_RANGE_FILTER_VALUE_IS_INVALID.getCode();
            }
            return data;
        } else if (StringUtils.equals("14", substring)) {
            // 因为MT300LITE走的是85（老版本） 故将值范围拉低至-100
            boolean comparison = comparison(data, "-100", "50");
            if (comparison) {
                return ProbeOutlier.THE_RANGE_FILTER_VALUE_IS_INVALID.getCode();
            }
        } else {

            boolean comparison = comparison(data, "0", "50");
            if (comparison) {
                return ProbeOutlier.THE_RANGE_FILTER_VALUE_IS_INVALID.getCode();
            }
        }
        return data;
    }

    /**
     * 91 温度 、CO2 、 O2
     *
     * @return
     */
    public static String agreementAll(String data, String lowData, String highData) {
        boolean comparison = comparison(data, lowData, highData);
        if (comparison) {
            return ProbeOutlier.THE_RANGE_FILTER_VALUE_IS_INVALID.getCode();
        }
        return data;
    }


    public static boolean comparison(String data, String lowData, String highData) {
        //与最大值进行比较，为1 返回false
        BigDecimal bigDecimal = new BigDecimal(data);
        int i = bigDecimal.compareTo(new BigDecimal(highData));
        if (i == 1) {
            return true;
        }
        //与最小值进行比较，为-1 返回false
        int i1 = bigDecimal.compareTo(new BigDecimal(lowData));
        if (i1 == -1) {
            return true;
        }
        return false;
    }

    public static String tempB1(String data) {
        if (!RegularUtil.checkContainsNumbers(data)){
            return data;
        }
        boolean comparison = comparison(data, "-200", "20");
        if(comparison){
            return ProbeOutlier.THE_RANGE_FILTER_VALUE_IS_INVALID.getCode();
        }
        return data;
    }



    public static void main(String args[]) {
//            BigDecimal bigDecimal = new BigDecimal(0);
//            int i = bigDecimal.compareTo(new BigDecimal(2));
//            if (i == 1) {
//                System.out.println("比最高值大");
//            }
//            //与最小值进行比较，为-1 返回false
//            int i1 = bigDecimal.compareTo(new BigDecimal(1));
//            if (i1 == -1 ) {
//                System.out.println("比最低值小");
//            }
        if (StringUtils.endsWithAny("05", "05", "07", "02")) {
            System.out.println("哈哈");
        }

}

}
