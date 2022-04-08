package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.ProbeOutlier;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MT100F {

    //温度1
    private String temperature;
    //电量
    private String electricity;


    public String getProbeOutlierTemperature(String exceptionCode) {


        return null;
    }


    public String getProbeOutlierElectricity(String exceptionCode) {
        return null;
    }
}
