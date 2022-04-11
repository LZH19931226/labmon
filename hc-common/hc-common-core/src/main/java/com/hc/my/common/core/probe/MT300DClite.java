package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 设备MT300DClite
 * @author hc
 */
@Data
public class MT300DClite extends PublicMethod {
    /**二氧化碳*/
    private String Co2;

    /**氧气*/
    private String o2;

    /**温度*/
    private String temperature;

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
        return getExceptionInfo2(exceptionCode);
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
    public String getProbeOutlierTemperature(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.F000.name())){
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        return null;
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
     * @return
     */
    public String getVoltageInfo(String exceptionCode){
        return null;
    }
}
