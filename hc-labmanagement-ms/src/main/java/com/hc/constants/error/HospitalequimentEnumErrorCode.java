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
    THE_SAME_DEVICE_TYPE_EXISTS("已经存在相同设备类型"),
    DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE("该设备类型底下存在设备");
    private String code;
}
