package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT300X
 * @author hc
 */
@Data
public class MT300X extends PublicMethod{

    /**二氧化碳*/
    private String Co2;

    /**氧气*/
    private String o2;

    /**温度*/
    private String temperature;

    /**开关量编号*/
    private String switchNum;

    /**DOOR开关状态*/
    private String switchStates;

    /**
     * 获取二氧化碳的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCo2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F000.name())) {
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        return null;
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
    public String getProbeOutlierTemperture(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.F000.name())) {
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        return null;
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
}
