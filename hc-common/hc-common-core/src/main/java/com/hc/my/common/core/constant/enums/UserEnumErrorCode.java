package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author user
 */
@Getter
@AllArgsConstructor
public enum UserEnumErrorCode {
    USER_NOT_EXISTS("用户不存在"),
    USER_ACCOUNT_OR_PASSWORD_ERROR("账号或者密码有误"),
    REQUEST_PARAMETER_NOT_VALID("请求参数不合法"),
    ;
    private String message;

}
