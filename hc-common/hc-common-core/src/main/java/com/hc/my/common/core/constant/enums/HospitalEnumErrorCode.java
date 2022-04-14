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
    HOSPITAL_NAME_ALREADY_EXISTS("新增医院失败，医院名称已存在"),
    ADD_HOSPITAL_INFO_FAILED("添加医院信息失败");
    private String code;
}
