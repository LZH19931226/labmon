package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Arrays;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum DataFieldEnum {
    CO2("CO2","currentcarbondioxide","CO2","CO2","%"),
    O2("O2","currento2","O2","O2","%"),
    VOC("VOC","currentvoc","VOC","VOC","PPM"),
    TEMP("TEMP","currenttemperature","温度","TEMP","℃"),
    RH("RH","currenthumidity","湿度","HUMIDITY","%"),
    PRESS("PRESS","currentairflow","压力","PRESS","%"),
    QC("QC","currentqc","电量","ELECTRICITY","%"),
    PM25("PM2.5","currentpm25","PM2.5","PM2.5","L/MIN"),
    PM10("PM10","currentpm10","PM10","PM10","L/MIN"),
    UPS("UPS","currentups","适配器供电","UPS",""),
    DOOR("DOOR","currentdoorstate","报警信号","ALARM",""),
    HCHO("甲醛","currentformaldehyde","甲醛","HCHO","N2/MPa"),
    TEMP1("TEMP1","currenttemperature1","温度一","TEMP1","℃"),
    TEMP2("TEMP2","currenttemperature2","温度二","TEMP2","℃"),
    TEMP3("TEMP3","currenttemperature3","温度三","TEMP3","℃"),
    TEMP4("TEMP4","currenttemperature4","温度四","TEMP4","℃"),
    TEMP5("TEMP5","currenttemperature5","温度五","TEMP5","℃"),
    TEMP6("TEMP6","currenttemperature6","温度六","TEMP6","℃"),
    TEMP7("TEMP7","currenttemperature7","温度七","TEMP7","℃"),
    TEMP8("TEMP8","currenttemperature8","温度八","TEMP8","℃"),
    TEMP9("TEMP9","currenttemperature9","温度九","TEMP9","℃"),
    TEMP10("TEMP10","currenttemperature10","温度十","TEMP10","℃"),
    LEFT_TEMP("LEFTTEMP","currentlefttemperature","左舱室温度","TEMPERATURE_IN_THE_LEFT_COMPARTMENT","℃"),
    RIGHT_TEMP("RIGHTTEMP","currentrigthtemperature","右舱室温度","TEMPERATURE_IN_THE_RIGHT_COMPARTMENT","℃"),
    AIR_FLOW("气流","currentairflow1","气流","AIRFLOW","ml/min"),
    DIFF_TEMP("DIFFTEMP","currenttemperaturediff","温差","TEMPERATURE_DIFFERENCE","℃"),
    PM5("PM5","currentpm5","PM5","PM5","L/MIN"),
    PM05("PM0.5","currentpm05","PM05","PM05","L/MIN"),
    LEFT_COVER_TEMP("LEFTCOVERTEMP","currentleftcovertemperature","左盖板温度","LEFT_COVER_TEMP","℃"),
    LEFT_END_TEMP("LEFTENDTEMP","currentleftendtemperature","左底板温度","LEFT_END_TEMP","℃"),
    LEFT_AIR_FLOW("左气流","currentleftairflow","左气流","LEFT_AIR_FLOW","ml/min"),
    RIGHT_COVER_TEMP("RIGHTCOVERTEMP","currentrightcovertemperature","右盖板温度","RIGHT_COVER_TEMP","℃"),
    RIGHT_END_TEMP("RIGHTENDTEMP","currentrightendtemperature","右底板温度","RIGHT_END_TEMP","℃"),
    RIGHT_AIR_FLOW("右气流","currentrightairflow","右气流","Right_AIR_FLOW","℃"),
    QCL("QCL","currentqcl","锁电量","LOCK_POWER","%"),
    N2("N2","currentn2","N2压力","N2_PRESSURE","Mpa"),
    LEFT_COMPARTMENT_HUMIDITY("leftCompartmentHumidity","leftCompartmentHumidity","左舱室湿度","LEFT_COMPARTMENT_HUMIDITY","%"),
    RIGHT_COMPARTMENT_HUMIDITY("rightCompartmentHumidity","rightCompartmentHumidity","右舱室湿度","RIGHT_COMPARTMENT_HUMIDITY","%"),
    voltage("voltage","voltage","电压","VOLTAGE","mV"),
    power("power","power","功率","POWER","W"),
    LIQUIDLEVEL("LIQUIDLEVEL","liquidLevel","液位","LIQUID_LEVEL","mm"),
    DOOR2("DOOR2","currentdoorstate2","报警信号2","ALARM2",""),
    outerCO2("outerCO2","currentcarbondioxide","内置CO2","BUILT-IN CO2","%"),
    current("current","qccurrent","电流","ELECTRIC_CURRENT","mA"),
    outerO2("outerO2","currento2","内置O2","BUILT-IN O2","%");

    private final String imField;

    private final String lastDataField;

    private final String cName;

    private final String eName;

    private final String unit;
    public static DataFieldEnum from(String imField) {
        return Arrays
                .stream(DataFieldEnum.values())
                .filter(c -> Objects.equals(c.getImField(), imField))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", imField));
    }

    public static DataFieldEnum fromByLastDataField(String lastDataField) {
        return Arrays
                .stream(DataFieldEnum.values())
                .filter(c -> Objects.equals(c.getLastDataField(), lastDataField))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", lastDataField));
    }
}
