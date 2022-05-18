package com.hc.my.common.core.probe;

import com.hc.my.common.core.constant.enums.DoorEnum;
import com.hc.my.common.core.constant.enums.ProbeOutlier;
import com.hc.my.common.core.constant.enums.SwitchNumEnum;
import com.hc.my.common.core.constant.enums.UpsEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * 公共的方法
 * @author user
 */
public  class PublicMethod {

    /**
     * 获取异常编码信息的方法 0000-FF00-FFF0-FFFF
     * @param exceptionCode 异常编码
     * @return
     */
    public static String getExceptionInfo(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.ZERO.name())){
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())){
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFFF.name())){
            return ProbeOutlier.NO_CALIBRATION.name();
        }
        return null;
    }

    /**
     * 获取异常编码信息的方法 F000-FF00-FFF0-FFFF
     * @param exceptionCode 异常编码
     * @return
     */
    public static String getExceptionInfo2(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.F000.name())){
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FF00.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())){
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFFF.name())){
            return ProbeOutlier.NO_CALIBRATION.name();
        }
        return null;
    }

    /**
     * 获取异常编码信息的方法 FFF0-FFF1-FFF2-FFF3
     * @param exceptionCode 异常编码
     * @return
     */
    public static String getExceptionInfo3(String exceptionCode){
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF0.name())){
            return ProbeOutlier.NO_SENSOR_IS_CONNECTED.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF1.name())){
            return ProbeOutlier.OUT_OF_TEST_RANGE.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF2.name())){
            return ProbeOutlier.VALUE_IS_INVALID.name();
        }
        if(StringUtils.equals(exceptionCode, ProbeOutlier.FFF3.name())){
            return ProbeOutlier.NO_CALIBRATION.name();
        }
        return null;
    }



    /**
     * 获取开关量信息的方法   1-2
     * @param info 异常编码
     * @return
     */
    public static String getSwitchInfo(String info){
        if(StringUtils.equals(info, SwitchNumEnum.ONE.name())){
            return SwitchNumEnum.ONE.name();
        }
        if(StringUtils.equals(info, SwitchNumEnum.two.name())){
            return SwitchNumEnum.two.name();
        }
        return null;
    }

    /**
     * 获取开关状态信息的方法 1-2-3-4
     * @param exceptionCode 异常编码
     * @return
     */
    public static String getSwitchStateInfo(String exceptionCode) {
        if(StringUtils.equals(exceptionCode, DoorEnum.ONE.name())){
            return DoorEnum.OPEN_TO_CLOSE.name();
        }
        if(StringUtils.equals(exceptionCode, DoorEnum.TWO.name())){
            return DoorEnum.TURN_OFF_TO_ON.name();
        }
        if(StringUtils.equals(exceptionCode, DoorEnum.THREE.name())){
            return DoorEnum.CONTINUOUS_CLOSING.name();
        }
        if(StringUtils.equals(exceptionCode, DoorEnum.FOUR.name())){
            return DoorEnum.CONTINUOUS_ON.name();
        }
        return null;
    }

    /**
     * 获取ups信息的方法 0-1
     * @param exceptionCode 异常编码
     * @return
     */
    public static String getUpsInfo(String exceptionCode){
        if(StringUtils.equals(exceptionCode, UpsEnum.ZERO.name())){
            return UpsEnum.NORMAL_POWER_SUPPLY.name();
        }
        if(StringUtils.equals(exceptionCode,UpsEnum.ONE.name())){
            return UpsEnum.ABNORMAL_POWER_SUPPLY.name();
        }
        return null;
    }

    /**
     * 获取量程过滤信息
     * @param exceptionCode
     * @return
     */
    public static String getRangeFilterInfo(String exceptionCode){
        if (StringUtils.equals(exceptionCode, ProbeOutlier.OXFFF0.name())) {
            return ProbeOutlier.NO_DATA_WAS_OBTAINED.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.OXFFF1.name())) {
            return ProbeOutlier.TEMPERATURE_CONTROL_OFF.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.OXFFF2.name())) {
            return ProbeOutlier.COMPARTMENT_DOOR_OPEN.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.OXFFF3.name())) {
            return ProbeOutlier.ABNORMAL_TEMPERATURE_CONTROL.name();
        }
        if (StringUtils.equals(exceptionCode, ProbeOutlier.OXFFF4.name())) {
            return ProbeOutlier.MAIN_SWITCH_OFF.name();
        }
        return null;
    }

    /**
     *  市电状态值
     *  1：市电通->市电断；(报警信息：发生断电事件)
     *
     * 2：市电断->市电断；(报警信息：持续断电事件)
     *
     * 3：市电断->市电通；(报警信息：发生通电事件)
     *
     * 4：市电正常；(状态信息)
     *
     * 5：市电异常；(状态信息)
     *
     * @param ups
     * @return
     */
    public static String getUpsStateInfo(String ups){
        if (StringUtils.equals(ups, UpsEnum.ONE.name())) {
            return UpsEnum.POWER_ON_TO_POWER_OFF.name();
        }
        if (StringUtils.equals(ups, UpsEnum.TWO.name())) {
            return UpsEnum.POWER_OFF_TO_POWER_OFF.name();
        }
        if (StringUtils.equals(ups, UpsEnum.THREE.name())) {
            return UpsEnum.POWER_OFF_TO_POWER_ON.name();
        }
        if (StringUtils.equals(ups, UpsEnum.FOUR.name())) {
            return UpsEnum.MAINS_IS_NORMAL.name();
        }
        if (StringUtils.equals(ups, UpsEnum.FIVE.name())) {
            return UpsEnum.MAINS_ABNORMALITY.name();
        }
        return null;
    }

}
