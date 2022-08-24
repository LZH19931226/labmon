package com.hc.constants.error;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

@Getter
@AllArgsConstructor
public enum  HospitalequimentEnumErrorCode {
    THE_DEVICE_HAS_AN_ALARM_PERIOD("该设备存在报警时段"),
    THE_SAME_DEVICE_TYPE_EXISTS("当前设备类型存在设备无法进行删除，请先删除对应的设备"),
    HOSPITAL_TYPE_ID_NOT_NULL("医院类型id不能为空"),
    DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE("该设备类型底下存在设备");
    private String code;
}
