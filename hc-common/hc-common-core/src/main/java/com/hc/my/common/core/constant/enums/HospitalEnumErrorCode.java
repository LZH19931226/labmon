package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author hc
 */
@Getter
@AllArgsConstructor
public enum HospitalEnumErrorCode {
    HOSPITAL_FULL_NAME_ALREADY_EXISTS("当前医院已存在，请勿重复增加"),
    ADD_HOSPITAL_INFO_FAILED("添加医院信息失败"),
    HOSPITAL_NAME_NOT_NULL("医院名称不能为空");
    private String code;
}
