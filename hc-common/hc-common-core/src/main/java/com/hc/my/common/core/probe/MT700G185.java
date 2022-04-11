package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT700G185
 * @author hc
 */
@Data
public class MT700G185 extends PublicMethod{

    /**二氧化碳*/
    private String Co2;

    /**氧气*/
    private String o2;

    /**温度1*/
    private String temperature1;

    /**温度2*/
    private String temperature2;

    /**温度3*/
    private String temperature3;

    /**温度4*/
    private String temperature4;

    /**温度5*/
    private String temperature5;

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
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取氧气的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierO2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取温度1的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature1(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取温度2的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取温度3的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature3(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取温度4的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature4(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取温度5的异常码信息
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierTemperature5(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
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
