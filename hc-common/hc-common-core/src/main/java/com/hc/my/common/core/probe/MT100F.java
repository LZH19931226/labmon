package com.hc.my.common.core.probe;

import lombok.Data;


/**
 * 设备MT100F
 * @author user
 */
@Data
public class MT100F extends PublicMethod {

    /**温度*/
    private String temperature;

    /**电量*/
    private String electricity;

    /**
     * 获取温度的异常编码
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode) {
        return getExceptionInfo(exceptionCode);
    }


    /**
     * 获取电量的异常码
     * @param exceptionCode 异常码
     * @return null
     */
    public String getProbeOutlierElectricity(String exceptionCode) {
        return null;
    }
}
