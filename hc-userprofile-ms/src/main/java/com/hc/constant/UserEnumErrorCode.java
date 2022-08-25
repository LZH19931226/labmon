package com.hc.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author user
 */
@Getter
@AllArgsConstructor
public enum UserEnumErrorCode {
    NOT_LOGGED_IN("未登录"),
    LOGIN_ACCOUNT_ALREADY_EXISTS("登录账号已存在"),
    USERNAME_ALREADY_EXISTS("用户名称已存在"),
    USER_NOT_EXISTS("用户不存在"),
    USER_ACCOUNT_OR_PASSWORD_ERROR("账号或者密码有误"),
    REQUEST_PARAMETER_NOT_VALID("请求参数不合法"),
    USERNAME_CAN_NOT_NULL("用户名不能为空"),
    PASSWORD_CAN_NOT_NULL("密码不能为空"),
    USERID_NOT_NULL("用户id不能为空"),
    ;
    private String message;

}
