package com.hc.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户权限枚举代码
 * @author user
 */
@Getter
@AllArgsConstructor
public enum UserRightEnumCode {
    THE_ACCOUNT_IS_THE_REGISTERED_MOBILE_PHONE_NUMBER("账号未绑定手机号"),
    VERIFICATION_CODE_ERROR("验证码错误"),
    VERIFICATION_CODE_HAS_EXPIRED("验证码已过期"),
    VERIFICATION_CODE_MUST_BE_FILLED("验证码不能为空"),
    MOBILE_NUMBER_CANNOT_BE_EMPTY("手机号不能为空"),
    USER_NOT_ENABLED("用户未启用,请联系管理员"),
    INCORRECT_USERNAME_OR_PASSWORD("用户名或密码不正确"),
    USERNAME_NOT_EXIST("用户名不存在"),
    PHONE_NUMBER_FORMAT_IS_NOT_CORRECT("手机号格式不正确"),
    USE_ID_NOT_NULL("用户id不能为空"),
    USERNAME_NOT_NULL("用户名不能为空"),
    PWD_NOT_NULL("密码不能为空"),
    USER_ROLE_NOT_NULL("用户角色不能为空"),
    SUPERMARKET_CONTACT_CANNOT_BE_EMPTY("超时联系人不能为空"),
    NICKNAME_NOT_NULL("用户名称不能为空"),
    HOSPITAL_NAME_NOT_NULL("所属医院不能为空"),
    USER_PHONE_NOT_NULL("用户手机号不能为空"),
    HOSPITALS_CANNOT_HAVE_THE_SAME_MOBILE_NUMBER("同医院不能有相同的手机号");
    private final String message;
}
