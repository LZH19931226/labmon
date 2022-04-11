package com.hc.my.common.core.probe;


import lombok.Data;

/**
 * 设备MT200
 * @author user
 */
@Data
public class MT200 extends PublicMethod {

    /**温度1*/
    private String temperature1;

    /**温度2*/
    private String temperature2;

    /**电量*/
    private String electricity;

    /**
     * 获取温度一的异常编码信息
     * @param exceptionCode
     * @return
     */
    static String getProbeOutlierTemperature1(String exceptionCode) {
        return getExceptionInfo(exceptionCode);
    }


    /**
     * 获取温度二的异常编码
     * @param exceptionCode 异常码
     * @return null
     */
    public String getProbeOutlierTemperature2(String exceptionCode) {
        return null;
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
