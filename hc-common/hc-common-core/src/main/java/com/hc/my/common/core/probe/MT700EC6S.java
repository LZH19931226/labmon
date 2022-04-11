package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT700EC6S
 * @author hc
 */
@Data
public class MT700EC6S extends PublicMethod{

    /**舱室1温度*/
    private String compartment1Temperature;

    /**舱室2温度*/
    private String compartment2Temperature;

    /**舱室3温度*/
    private String compartment3Temperature;

    /**舱室4温度*/
    private String compartment4Temperature;

    /**舱室5温度*/
    private String compartment5Temperature;

    /**舱室6温度*/
    private String compartment6Temperature;

    /**氧气*/
    private String o2;

    /**二氧化碳*/
    private String Co2;

    /**舱室流量1*/
    private String cabinFlow1;

    /**舱室流量2*/
    private String cabinFlow2;

    /**舱室流量3*/
    private String cabinFlow3;

    /**舱室流量4*/
    private String cabinFlow4;

    /**舱室流量5*/
    private String cabinFlow5;

    /**舱室流量6*/
    private String cabinFlow6;

    /**开关量编号*/
    private String switchNum;

    /**DOOR开关状态*/
    private String switchStates;

    /**UPS*/
    private String ups;

    /**电压*/
    private String voltage;

    /**
     *  获取探测器异常值舱室1 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment1Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     *  获取探测器异常值舱室2 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment2Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     *  获取探测器异常值舱室3 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment3Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     *  获取探测器异常值舱室4 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment4Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     *  获取探测器异常值舱室5 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment5Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     *  获取探测器异常值舱室6 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment6Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值氧气
     * @param exceptionCode
     * @return
     */
    public String getProneOutlierO2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值二氧化碳
     * @param exceptionCode
     * @return
     */
    public String getProneOutlierCo2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量1
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow1(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量2
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow2(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量3
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow3(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量4
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow4(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量5
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow5(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 舱室流量6
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCabinFlow6(String exceptionCode){
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
