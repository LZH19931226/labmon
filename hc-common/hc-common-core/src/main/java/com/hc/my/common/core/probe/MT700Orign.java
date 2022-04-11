package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * MT700Orign(BT37)
 * @author hc
 */
@Data
public class MT700Orign extends PublicMethod{

    /**左舱室顶温度*/
    private String leftRoofTemperature;

    /**右舱室顶温度*/
    private String rightRoofTemperature;

    /**左舱室底温度*/
    private String leftBottomTemperature;

    /**右舱室底温度*/
    private String rightBottomTemperature;

    /**加湿器温度*/
    private String humidifierTemperature;

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
     * 获取探测器异常值左屋顶温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftRoofTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值右顶温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRightRoofTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值左底温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierLeftBottomTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值右底温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierRightBottomTemperature(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值加湿器温度
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierHumidifierTemperature(String exceptionCode){
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
    public String getProbeOutlierAirflow(String exceptionCode){
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

