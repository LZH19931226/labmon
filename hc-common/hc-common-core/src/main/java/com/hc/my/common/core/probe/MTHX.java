package com.hc.my.common.core.probe;

import lombok.Data;

/**
 * 设备 MTHX
 * @author hc
 */
@Data
public class MTHX {

    /**左舱室顶温度*/
    private String leftRoofTemperature;

    /**左舱室底温度*/
    private String leftBottomTemperature;

    /**左气流*/
    private String leftAirflow;

    /**右舱室顶温度*/
    private String rightRoofTemperature;

    /**右舱室底温度*/
    private String rightBottomTemperature;

    /**右气流*/
    private String rightAirflow;

    /**
     * 获取探测器异常值左舱室顶温度
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierLeftRoofTemperature(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值左舱室底温度
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierLeftBottomTemperature(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值左气流
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierLeftAirflow(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值右舱室顶温度
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierRightRoofTemperature(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值右舱室底温度
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierRightBottomTemperature(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值右气流
     * @param exceptionCode
     * @return
     */
    private String getProbeOutlierRightAirflow(String exceptionCode){
        return null;
    }
}
