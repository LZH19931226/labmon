package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT100
 * @author user
 */
@Data
public class MT100 {

    /**温度1(平均值)*/
    private String temperature1;

    /**温度2(实时值)*/
    private String temperature2;

    /**电量*/
    private String electricity;


    /**
     * 获取温度一的异常编码
     * @param exceptionCode 异常码
     * @return null
     */
    public String getProbeOutlierTemperature1(String exceptionCode) {
        return null;
    }

    /**
     * 获取温度二的异常编码
     * @param exceptionCode 异常码
     * @return
     */
    public String getProbeOutlierTemperature2(String exceptionCode) {
        if (StringUtils.equals(exceptionCode, ProbeOutlier.ZERO.name())) {
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
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