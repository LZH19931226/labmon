package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备MT1100/MT500
 * @author hc
 */
@Data
public class MT1100MT500 extends PublicMethod{

    /**UPS*/
    private String ups;

    /**电压*/
    private String voltage;

    /**
     * 获取ups信息
     * @param exceptionCode
     * @return
     */
    public String getEnumUps(String exceptionCode){
        return getUpsInfo(exceptionCode);
    }

    /**
     * 获取电压值
     * @param exceptionCode
     * @return null
     */
    public String getVoltageInfo(String exceptionCode){
        return null;
    }
}
