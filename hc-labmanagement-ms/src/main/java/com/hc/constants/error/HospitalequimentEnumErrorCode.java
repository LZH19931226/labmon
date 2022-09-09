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
    START_TIME_AND_END_TIME_ARE_ABNORMAL("开始时间不能大于或等于结束时间"),
    THERE_IS_AN_OVERLAP_BETWEEN_THE_TWO_TIME_PERIODS("两个时间段之间存在重叠，请检查时间"),
    THERE_IS_AN_OVERLAP_OF_THE_THREE_TIME_PERIODS("三个时间段存在交集重叠，请检测时间"),
    THE_SAME_DEVICE_TYPE_EXISTS("已经存在相同设备类型"),
    HOSPITAL_TYPE_ID_NOT_NULL("医院类型id不能为空"),
    DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE("当前设备类型存在设备无法进行删除，请先删除对应的设备");
    private String code;
}
