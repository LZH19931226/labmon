package com.hc.utils;

import com.hc.model.ResponseModel.InstrumentMonitorInfoModel;

import java.math.BigDecimal;

/**
 * Created by 16956 on 2018-08-09.
 */

/**
 * 最大值最小值验证
 */
public class LowHighVerify {


    public static boolean verify(InstrumentMonitorInfoModel monitorInfoModel,String date){
        BigDecimal bigDecimal = new BigDecimal(date);
        BigDecimal low = monitorInfoModel.getLowlimit();
        BigDecimal high = monitorInfoModel.getHighlimit();
        Integer a = bigDecimal.compareTo(low);
        Integer b = bigDecimal.compareTo(high);
        if (a == -1 || b == 1) {
            return true;
        }
        return false;
    }
}
