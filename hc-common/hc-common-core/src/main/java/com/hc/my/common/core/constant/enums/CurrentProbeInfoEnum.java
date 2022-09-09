package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CurrentProbeInfoEnum {

    //当前温度
    CURRENT_TEMPERATURE(4,"currenttemperature","温度","℃"),
    //当前电量
    CURRENTQC(7,"currentqc","电量","%"),
    //当前锁电量
    CURRENTQCL(7,"currentqcl","锁电量","%"),
    //当前二氧化碳
    CURRENTCARBONDIOXIDE(1,"currentcarbondioxide","CO2","%"),
    //当前O2
    CURRENTO2(2,"currento2","O2","%"),
    //当前市电是否异常
    CURRENTUPS(10,"currentups","市电","mV"),
    //当前开门记录
    CURRENTDOORSTATE(11,"currentdoorstate","报警信号",null),
    //当前空气质量
    CURRENTVOC(3,"currentvoc","VOC","PPM"),
    //当前甲醛
    CURRENTFORMALDEHYDE(12,"currentformaldehyde","甲醛","N2/MPa"),
    //当前PM10
    CURRENTPM10(9,"currentpm10","PM10","L/MIN"),
    //当前PM2_5
    CURRENTPM25(8,"currentpm25","PM2.5","L/MIN"),
    //当前气流
    CURRENTAIRFLOW(6,"currentairflow","气流","Mpa"),
    //当前湿度
    CURRENTHUMIDITY(5,"currenthumidity","湿度","%"),
    //左路温度  左舱室温度
    CURRENTLEFTTEMPERATURE(23,"currentlefttemperature","左舱室温度","%"),
    //右路温度  右舱室温度
    CURRENTRIGTHTEMPERATURE(24,"currentrigthtemperature","右舱室温度","℃"),
    //培养箱气流
    CURRENTAIRFLOW1(25,"currentairflow1","气流","ml/min"),
    //一路温度
    CURRENTTEMPERATURE1(13,"currenttemperature1","温度一","℃"),
    //二路温度
    CURRENTTEMPERATURE2(14,"currenttemperature2","温度二","℃"),
    //三路温度
    CURRENTTEMPERATURE3(15,"currenttemperature3","温度三","℃"),
    //四路温度
    CURRENTTEMPERATURE4(16,"currenttemperature4","温度四","℃"),
    //五路温度
    CURRENTTEMPERATURE5(17,"currenttemperature5","温度五","℃"),
    //温差
    CURRENTTEMPERATUREDIFF(26,"currenttemperaturediff","温差","℃"),
    //当前PM5
    CURRENTPM5(27,"currentpm5","PM5","L/MIN"),
    //当前PM0.5
    CURRENTPM05(28,"currentpm05","PM0.5","L/MIN"),
    //电流
    QCCURRENT(40,"qccurrent","电流","mA"),
    //电压
    VOLTAGE(39,"voltage","电压","V"),
    //功率
    POWER(41,"power","功率","W"),
    //六路温度
    CURRENTTEMPERATURE6(18,"currenttemperature6","温度六","℃"),
    //左气流
    CURRENTLEFTAIRFLOW(31,"currentleftairflow","左气流","ml/min"),
    //左舱室湿度
    LEFTCOMPARTMENTHUMIDITY(37,"leftCompartmentHumidity","左舱室湿度","%"),
    //右气流
    CURRENTRIGHTAIRFLOW(34,"currentrightairflow","右气流","ml/min"),
    //右舱室湿度
    RIGHTCOMPARTMENTHUMIDITY(38,"rightCompartmentHumidity","右舱室湿度","%"),
    //九路温度
    CURRENTTEMPERATURE9(21,"currenttemperature9","温度九","℃"),
    //10路温度
    CURRENTTEMPERATURE10(22,"currenttemperature10","温度十","℃"),
    //当前N2
    CURRENTN2(36,"currentn2","N2压力","Mpa"),
    //7路温度
    CURRENTTEMPERATURE7(19,"currenttemperature7","温度七","℃"),
    //8路温度
    CURRENTTEMPERATURE8(20,"currenttemperature8","温度八","℃"),
    //左盖板温度
    CURRENTLEFTCOVERTEMPERATURE(29,"currentleftcovertemperature","左盖板温度","℃"),
    //右盖板温度
    CURRENTRIGHTCOVERTEMPERATURE(30,"currentrightcovertemperature","右盖板温度","℃"),
    //左底板温度
    CURRENTLEFTENDTEMPERATURE(32,"currentleftendtemperature","左底板温度","℃"),
    //右底板温度
    CURRENTRIGHTENDTEMPERATURE(33,"currentrightendtemperature","右底板温度","℃"),
    //
    PROBE1(101,null,"监测探头1",null),
    //
    PROBE2(102,null,"监测探头2",null),
    //
    PROBE3(103,null,"监测探头3",null),
    //
    OUTERCO2(42,"outerCO2","外置CO2探头",null),
    OUTERO2(43,"outerO2","外置O2探头",null),
    //二路开关量
    CURRENTDOORSTATE2(44,"currentdoorstate2","报警信号2",null);

    private int instrumentConfigId;
    private String probeEName;
    private String probeCName;
    private String unit;

    public static CurrentProbeInfoEnum from(int instrumentConfigId) {
        return Arrays
                .stream(CurrentProbeInfoEnum.values())
                .filter(c -> c.getInstrumentConfigId()==(instrumentConfigId))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", instrumentConfigId));
    }

    public static CurrentProbeInfoEnum from(String probeEName){
        return Arrays
                .stream(CurrentProbeInfoEnum.values())
                .filter(c->probeEName.equals(c.getProbeEName()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", probeEName));
    }
}
