package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备MT200L
 * @author hc
 */
@Data
public class MT200L extends PublicMethod{

    /**温度*/
    private  String temperature;

    /**电量*/
    private String electricity;

    /**
     * 获取温度的异常信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode){
        return getExceptionInfo(exceptionCode);
    }

    /**
     * 获取电量的异常信息
     * @param exceptionCode
     * @return null
     */
    public String getProbeOutlierElectricity(String exceptionCode){
        return null;
    }
}
