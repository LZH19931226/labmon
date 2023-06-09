package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备异常值
 * @author user
 */
@Getter
public enum ProbeOutlier {

    NO_SENSOR_IS_CONNECTED("未接传感器"),
    OUT_OF_TEST_RANGE("超出量程"),
    VALUE_IS_INVALID("值无效"),
    THE_RANGE_FILTER_VALUE_IS_INVALID("量程过滤值无效"),
    NO_CALIBRATION("未校准"),
    NO_DATA_WAS_OBTAINED("未获取到数据"),
    TEMPERATURE_CONTROL_OFF("温度控制关闭"),
    COMPARTMENT_DOOR_OPEN("仓室门开启"),
    ABNORMAL_TEMPERATURE_CONTROL("温度控制异常"),
    MAIN_SWITCH_OFF("总开关关闭，但未断电,无显示"),
    AIRFLOW_CONTROL_OFF("流量控制关闭"),
    GAS_FLOW_IS_UNSTABLE("气体流量不稳定"),
    LOW_INLET_PRESSURE("进气口压力低"),
    AIRFLOW_OUT_OF_RANGE("无气或气流超出范围"),
    G("有漏气倾向"),
    H("恢复正常"),
    I("发生报警事件"),
    J("手动关闭报警"),
    K("设备正常运行"),
    L("设备漏气预警"),
    M("设备漏气报警"),
    N("手动关闭报警"),
    O("设备气压低报警"),
    P("手动关闭气压低报警"),
    Q("休眠模式"),
    HAVE_A_TENDENCY_TO_LEAK_AIR("G"),
    RETURN_TO_NORMAL("H"),
    ALARM_EVENT_OCCURRED("I"),
    MANUAL_SHUTDOWN_ALARM("J"),
    NORMAL_OPERATION_OF_EQUIPMENT("K"),
    NORMAL("L"),
    ANOMALY("M"),
    EQUIPMENT_LEAKAGE_WARNING("N"),
    DEVICE_PRESSURE_LOW_ALARM("O"),
    MANUALLY_TURN_OFF_THE_LOW_PRESSURE_ALARM("P"),
    SLEEP_MODE("Q"),
    ZERO("0000"),
    F000("f000"),
    FF00("ff00"),
    FFF0("fff0"),
    FFFF("ffff"),
    FFF1("fff1"),
    FFF2("fff2"),
    FFF3("fff3"),
    OXFFF0("fff0"),
    OXFFF1("fff1"),
    OXFFF2("fff2"),
    OXFFF3("fff3"),
    OXFFF4("fff4"),
    OXFFF5("fff5"),
    FFFFFFF0("fffffff0"),
    FFFFFFF1("fffffff1"),
    F1("f1"),
    F0("f0");

    private String code;

    ProbeOutlier(String code){
        this.code =code;
    };

    public static List<String> getNameList(){
        return  Arrays
                .stream(ProbeOutlier.values())
                .map(ProbeOutlier::getCode)
                .collect(Collectors.toList());
    }

    public static ProbeOutlier from(String code){
        return Arrays
                .stream(ProbeOutlier.values())
                .filter(c->code.equals(c.getCode()))
                .findFirst()
                .orElseThrow(()-> new IedsException("没有该类型底下ProbeOutlier的enum {}", code));
    }
}
