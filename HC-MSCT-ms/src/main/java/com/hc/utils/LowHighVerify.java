package com.hc.utils;


import com.hc.my.common.core.redis.dto.InstrumentInfoDto;
import com.hc.my.common.core.util.RegularUtil;

import java.math.BigDecimal;

/**
 * Created by 16956 on 2018-08-09.
 */

/**
 * 最大值最小值验证
 */
public class LowHighVerify {

    /**
     * 判断 date是否在probe中的最高值和最低值之中
     * @param probe
     * @param date
     * @return
     */
    public static boolean verify(InstrumentInfoDto probe, String date) {
        BigDecimal bigDecimal = new BigDecimal(date);
        BigDecimal low = probe.getLowLimit();
        BigDecimal high = probe.getHighLimit();
        int a = bigDecimal.compareTo(low);
        int b = bigDecimal.compareTo(high);
        if (a < 0 || b > 0) {
            return true;
        }
        return false;
    }


    public static boolean verifyMt200m(BigDecimal high, String date) {
        BigDecimal bigDecimal = new BigDecimal(date);
        int i = bigDecimal.compareTo(high);
        if (i < 0) {
            return false;
        }
        return true;
    }

    public static void main(String[] args){
        InstrumentInfoDto probe = new InstrumentInfoDto();
        probe.setLowLimit(new BigDecimal(-190));
        probe.setHighLimit(new BigDecimal(-200));
        System.out.println( !verify(probe, "-185.24"));
        System.out.println(!RegularUtil.checkContainsNumbers("-185.24"));
    }


}
