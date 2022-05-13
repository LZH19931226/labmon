package com.hc.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum HospitalEnumErrorCode {
    HOSPITAL_DEVICE_TYPE_DOES_NOT_EXIST("医院设备类型不存在"),
    HOSPITAL_FULL_NAME_ALREADY_EXISTS("当前医院已存在，请勿重复增加"),
    HOSPITAL_NAME_ALREADY_EXISTS("当前医院已存在,请修改医院名"),
    ADD_HOSPITAL_INFO_FAILED("添加医院信息失败"),
    HOSPITAL_FULL_NAME_NOT_NULL("医院全称不能为空"),
    HOSPITAL_CODE_NOT_NULL("医院编码不能为空"),
    UPDATE_HOSPITAL_INFO_FAIL("修改医院信息失败"),
    HOSPITAL_INFO_NOTABLE_DELETED("当前医院存在设备无法进行删除，请先删除对应的设备"),
    HOSPITAL_INFO_DELETE_FAIL("医院信息删除失败");
    private String code;
}
