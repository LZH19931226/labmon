package com.hc.custom;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/** 判断
 * Created by xxf on 2019-04-08.
 */
public class CustomUtils {


    /**
     * 85 温度判断是否在有效值内
     * @return
     */
    public String tem85(String data,String sn){
        String substring = sn.substring(4, 6);
        if (StringUtils.equals("05",sn)) {
            //MT200  -200 -- 40


        }
        return null;
    }



    public boolean comparison(String data,String lowData,String highData) {
        //与最大值进行比较，为1 返回false
        BigDecimal bigDecimal = new BigDecimal(data);
        int i = bigDecimal.compareTo(new BigDecimal(highData));
        if (i == 1) {
            return false;
        }
        //与最小值进行比较，为-1 返回false
        int i1 = bigDecimal.compareTo(new BigDecimal(lowData));
        if (i1 == -1 ) {
            return false;
        }
        return true;
    }

}
