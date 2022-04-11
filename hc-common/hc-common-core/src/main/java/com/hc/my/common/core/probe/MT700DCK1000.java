package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT700D-CK1000
 * @author hc
 */
@Data
public class MT700DCK1000 extends PublicMethod{

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

    /**舱室7温度*/
    private String compartment7Temperature;

    /**舱室8温度*/
    private String compartment8Temperature;

    /**舱室9温度*/
    private String compartment9Temperature;

    /**舱室10温度*/
    private String compartment10Temperature;

    /**氧气*/
    private String o2;

    /**二氧化碳*/
    private String Co2;

    /**N2 压力*/
    private String N2pressure;

    /**CO2 压力*/
    private String co2pressure;

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
     * 获取探测器异常值舱室1 温度
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
     * 获取探测器异常值舱室2 温度
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
     * 获取探测器异常值舱室3 温度
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
     * 获取探测器异常值舱室4 温度
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
     * 获取探测器异常值舱室5 温度
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
     * 获取探测器异常值舱室6 温度
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
     * 获取探测器异常值舱室7 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment7Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值舱室8 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment8Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值舱室9 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment9Temperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值舱室10 温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierCompartment10Temperature(String exceptionCode){
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
     * 获取探测器异常值氮气压力
     * @param exceptionCode
     * @return
     */
    public String getProneOutlierN2Pressure(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值二氧化碳压力
     * @param exceptionCode
     * @return
     */
    public String getProneOutlierCO2Pressure(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }
    /**
     * 获取探测器异常值气流
     * @param exceptionCode
     * @return
     */
    public String getProneOutlierAirflow(String exceptionCode){
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
