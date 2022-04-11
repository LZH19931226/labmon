package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT800
 * @author hc
 */
@Data
public class MT800 extends PublicMethod{

    /**电量*/
    private String electricity;

    /**市电ups*/
    private String ups;

    /**电压*/
    private String voltage;

    /**电流*/
    private String electricCurrent;

    /**功率*/
    private String power;

    /**
     * 获取探测器异常值 电量
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierElectricity(String exceptionCode){
        return null;
    }

    /**
     * 获取探测器异常值 市电
     * @param ups
     * @return
     */
    public String getUpsCodeInfO(String ups){
       return getUpsStateInfo(ups);
    }

    /**
     * 获取探测器异常值电压
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierVoltage(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值电流
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierElectricCurrent(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }

    /**
     * 获取探测器异常值 功率
     * @param exceptionCode
     * @return
     */
    public String getProbeOutlierPower(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())) {
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        return null;
    }
}
