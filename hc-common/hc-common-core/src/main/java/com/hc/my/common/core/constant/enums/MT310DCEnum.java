package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author user
 */
@Getter
public enum MT310DCEnum {
    ZERO("0","未接传感器",""),
    ONE("1","温度","℃"),
    TWO("2","湿度","%"),
    THREE("3","O2浓度","%"),
    FOUR("4","CO2浓度","%"),
    PROBE_ONE_MODEL("probe1model","外置探头1监测类型",null),
    PROBE_TWO_MODEL("probe2model","外置探头2监测类型",null),
    PROBE_THREE_MODEL("probe3model","外置探头3监测类型",null),
    PROBE_ONE_DATA("probe1data","外置探头1值",null),
    PROBE_TWO_DATA("probe2data","外置探头2值",null),
    PROBE_THREE_DATA("probe3data","外置探头3值",null),
    CURRENT_CARBON_DIOXIDE("currentcarbondioxide","内置探头CO2","%"),
    CURRENT_OXYGEN("currento2","内置探头O2","%"),
    CURRENT_VOC("currentvoc","内置探头VOC","PPM");
    private String eName;
    private String cName;
    private String unit;

    MT310DCEnum(String eName, String cName,String unit) {
        this.eName = eName;
        this.cName = cName;
        this.unit = unit;
    }

    public static MT310DCEnum from(String probeEName){
        return Arrays
                .stream(MT310DCEnum.values())
                .filter(c->probeEName.equals(c.getEName()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", probeEName));
    }
}
