package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT700C-TOP
 * @author hc
 */
@Data
public class MT700CTOP extends PublicMethod{

    /**左舱室温度*/
    private  String leftCabinTemperature;

    /**左舱室流量*/
    private String leftCompartmentFlow;

    /**左舱室湿度*/
    private String leftCabinHumidity;

    /**右舱室温度*/
    private String rightCabinTemperature;

    /**右舱室流量*/
    private String rightCompartmentFlow;

    /**右舱室湿度*/
    private String rightCabinHumidity;

    /**开关量编号*/
    private String switchNum;

    /**DOOR开关状态*/
    private String switchStates;

    /**UPS*/
    private String ups;

    /**电压*/
    private String voltage;

    /**
     * 获取探测器异常值左舱室温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftCabinTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 左舱室流量
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftCompartmentFlow(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 左舱室湿度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftCabinHumidity(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 右舱室温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRightCabinTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 右舱室流量
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRightCabinFlow(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 右舱室湿度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRightCabinHumidity(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取 开关量信息
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
