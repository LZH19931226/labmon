package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备MT200LM
 * @author hc
 */
@Data
public class MT200LM extends PublicMethod{

    /**液氮温度一*/
    private String liquidNitrogenTemperature1;

    /**液氮温度二*/
    private String liquidNitrogenTemperature2;

    /**室温*/
    private String roomTemperature;

    /**壁温*/
    private String wallTemperature;

    /**电量*/
    private String electricity;

    /**温差*/
    private String temperatureDifference;

    /**
     * 获取探针异常值液氮温度 1
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLiquidNitrogenTemperature1(String exceptionCode){
        return getExceptionInfo3(exceptionCode);
    }

    /**
     * 获取探针异常值液氮温度 2
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLiquidNitrogenTemperature2(String exceptionCode){
        return getExceptionInfo3(exceptionCode);
    }

    /**
     * 获取探针异常值室温
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRoomTemperature(String exceptionCode){
        return getExceptionInfo3(exceptionCode);
    }

    /**
     * 获取探针异常值壁温
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierWallTemperature(String exceptionCode){
        return getExceptionInfo3(exceptionCode);
    }

    /**
     * 获取探针异常值电量
     * @param exceptionCode
     * @return null
     */
    public String getProbeOutlierElectricity(String exceptionCode){
        return null;
    }

    /**
     * 获取探针异常值带温差
     * @param exceptionCode
     * @return null
     */
    public String getProbeOutlierTemperatureDifference(String exceptionCode){
        return null;
    }

}
