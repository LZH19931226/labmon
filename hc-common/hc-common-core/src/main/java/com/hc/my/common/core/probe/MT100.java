package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MT100 {

    //温度1(平均值)
    private String temperature1;
    //温度2(实时值)
    private String temperature2;
    //电量
    private String electricity;



    public String getProbeOutlierTemperature1(String exceptionCode) {
        return null;
    }

    public String getProbeOutlierTemperature2(String exceptionCode) {
        if (StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())) {
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        return null;
    }

    public String getProbeOutlierElectricity(String exceptionCode) {
        return null;
    }
}
