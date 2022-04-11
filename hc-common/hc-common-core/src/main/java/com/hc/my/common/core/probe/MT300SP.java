package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备MT300SP（MT300+MT300SP）
 * @author hc
 */
@Data
public class MT300SP extends PublicMethod{

    /**二氧化碳*/
    private String Co2;

    /**氧气*/
    private String o2;

    /**温度*/
    private String temperature;

    /**空气质量*/
    private String rh;

    /**开关量编号*/
    private String switchNum;

    /**DOOR开关状态*/
    private String switchStates;

    /**UPS*/
    private String ups;

    /**电压*/
    private String voltage;

    /**
     *获取二氧化碳的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCo2(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取氧气的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierO2(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取温度的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取空气质量的异常码信息
     * @param exceptionCode
     * @return null
     */
    public String getProbeOutlierRh(String exceptionCode){
        return getExceptionInfo2(exceptionCode);
    }

    /**
     * 获取开关量信息
     * @param exceptionCode
     * @return
     */
    public String getEnumSwitchNum(String exceptionCode){
        return getSwitchInfo(exceptionCode);
    }

    /**
     * 获取DOOR开关状态信息
     * @param exceptionCode
     * @return
     */
    public String getEnumSwitchStates(String exceptionCode) {
        return getSwitchStateInfo(exceptionCode);
    }

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
