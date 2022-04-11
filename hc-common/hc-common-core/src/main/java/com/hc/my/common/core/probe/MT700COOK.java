package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备MT700COOK
 * @author hc
 */
@Data
public class MT700COOK extends PublicMethod{

    /**左舱室温度*/
    private String leftTemperature;

    /**右舱室温度*/
    private String rightTemperature;

    /**气流*/
    private String airflow;

    /**开关量编号*/
    private String switchNum;

    /**DOOR开关状态*/
    private String switchStates;

    /**UPS*/
    private String ups;

    /**电压*/
    private String voltage;

    /**
     * 获取探针离群值左舱室温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftTemperature(String exceptionCode){
        return getRangeFilterInfo(exceptionCode);
    }

    /**
     * 获取探针异常值右舱室温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRigthTemperature(String exceptionCode){
        return getRangeFilterInfo(exceptionCode);
    }

    /**
     * 获取探针异常值气流
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierAirflow(String exceptionCode){
        return getRangeFilterInfo(exceptionCode);
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
