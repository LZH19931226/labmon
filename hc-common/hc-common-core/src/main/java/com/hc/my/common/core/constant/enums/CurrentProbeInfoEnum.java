package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrentProbeInfoEnum {

    //当前温度
    CURRENT_TEMPERATURE(4,"currenttemperature",""),
    //当前电量
    CURRENTQC(7,"currentqc",""),
    //当前锁电量
    CURRENTQCL(7,"currentqcl",""),
    //当前二氧化碳
    CURRENTCARBONDIOXIDE(1,"currentcarbondioxide",""),
    //当前O2
    CURRENTO2(2,"currento2",""),
    //当前市电是否异常
    CURRENTUPS(10,"currentups",""),
    //当前开门记录
    CURRENTDOORSTATE(11,"currentdoorstate",""),
    //当前空气质量
    CURRENTVOC(3,"currentvoc",""),
    //当前甲醛
    CURRENTFORMALDEHYDE(12,"currentformaldehyde",""),
    //当前PM10
    CURRENTPM10(9,"currentpm10",""),
    //当前PM2_5
    CURRENTPM25(8,"currentpm25",""),
    //当前气流
    CURRENTAIRFLOW(6,"currentairflow",""),
    //当前湿度
    CURRENTHUMIDITY(5,"currenthumidity",""),
    //左路温度  左舱室温度
    CURRENTLEFTTEMPERATURE(23,"currentlefttemperature",""),
    //右路温度  右舱室温度
    CURRENTRIGTHTEMPERATURE(24,"currentrigthtemperature",""),
    //培养箱气流
    CURRENTAIRFLOW1(25,"currentairflow1",""),
    //一路温度
    CURRENTTEMPERATURE1(13,"currenttemperature1",""),
    //二路温度
    CURRENTTEMPERATURE2(14,"currenttemperature2",""),
    //三路温度
    CURRENTTEMPERATURE3(15,"currenttemperature3",""),
    //四路温度
    CURRENTTEMPERATURE4(16,"currenttemperature4",""),
    //五路温度
    CURRENTTEMPERATURE5(17,"currenttemperature5",""),
    //温差
    CURRENTTEMPERATUREDIFF(26,"currenttemperaturediff",""),
    //当前PM5
    CURRENTPM5(27,"currentpm5",""),
    //当前PM0.5
    CURRENTPM05(28,"currentpm05",""),
    //电流
    QCCURRENT(40,"qccurrent",""),
    //温度六
    CURRENTTEMPERATURE6(18,"currenttemperature6",""),
    //温度七
    CURRENTTEMPERATURE7(19,"currenttemperature7",""),
    //温度八
    CURRENTTEMPERATURE8(20,"currenttemperature8",""),
    //温度九
    CURRENTTEMPERATURE9(21,"currenttemperature9",""),
    //温度十
    CURRENTTEMPERATURE10(22,"currenttemperature10",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    //当前O2
    CURRENTO2(2,"O2",""),
    private int instrumentConfigId;
    private String probeEName;
    private String probeCName;


}
