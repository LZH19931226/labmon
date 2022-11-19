package com.hc.my.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LabSystemEnum {
    /**mon服务*/
    HOSPITAL_IS_NOT_BOUND_EQUIPMENT_TYPE("该医院没有绑定设备类型"),
    EQUIPMENT_INFO_NOT_FOUND("未找到设备信息"),
    NO_UTILITY_RECORD("未找到市电记录"),
    THE_DEVICE_HAS_NO_PROBE_INFORMATION("未找到设备探头信息"),
    NO_DATA_FOR_CURRENT_TIME("当前时间未找到数据"),
    /**user服务*/
    LANG_NOT_NULL("语种不能为空"),
    PASSWORD_CAN_NOT_NULL("密码不能为空"),
    USERID_NOT_NULL("用户id不能为空"),
    HOSPITAL_FULL_NAME_NOT_NULL("医院全称不能为空"),
    HOSPITAL_CODE_NOT_NULL("医院编码不能为空"),
    USERNAME_NOT_NULL("用户名不能为空"),
    NICKNAME_NOT_NULL("用户名称不能为空"),
    SUPERMARKET_CONTACT_CANNOT_BE_EMPTY("超时联系人不能为空"),
    HOSPITAL_NAME_NOT_NULL("所属医院不能为空"),
    USER_ROLE_NOT_NULL("用户角色不能为空"),
    USER_NOT_EXISTS("用户不存在"),
    USER_ACCOUNT_OR_PASSWORD_ERROR("账号或者密码有误"),
    LOGIN_ACCOUNT_ALREADY_EXISTS("登录账号已存在"),
    PHONE_NUM_EXISTS("同医院下手机号已存在"),
    USERNAME_NOT_EXIST("用户名不存在"),
    INCORRECT_USERNAME_OR_PASSWORD("用户名或密码不正确"),
    USER_NOT_ENABLED("用户未启用,请联系管理员"),
    TIME_PERIOD_DISAGREE("时间段不统一"),
    NO_SCHEDULE_INFORMATION_FOUND("未找到该时间段排班信息"),
    THIS_INFORMATION_NO_LONGER_EXISTS("该信息已不存在"),
    HOSPITAL_FULL_NAME_ALREADY_EXISTS("当前医院已存在，请勿重复增加"),
    HOSPITAL_NAME_ALREADY_EXISTS("当前医院已存在,请修改医院名"),
    HOSPITAL_INFO_NOTABLE_DELETED("当前医院存在设备无法进行删除，请先删除对应的设备"),
    HOSPITALS_CANNOT_HAVE_THE_SAME_MOBILE_NUMBER("同医院不能有相同的手机号"),
    THE_ACCOUNT_IS_THE_REGISTERED_MOBILE_PHONE_NUMBER("账号未绑定手机号"),
    MOBILE_NUMBER_CANNOT_BE_EMPTY("手机号不能为空"),
    VERIFICATION_CODE_MUST_BE_FILLED("验证码不能为空"),
    VERIFICATION_CODE_HAS_EXPIRED("验证码已过期"),
    VERIFICATION_CODE_ERROR("验证码错误"),
    HOSPITAL_INFO_DELETE_FAIL("医院信息删除失败"),
    /**后台管理*/
    VERSION_NUMBER_ALREADY_EXISTS("版本号已存在"),
    START_TIME_AND_END_TIME_ARE_ABNORMAL("开始时间不能大于或等于结束时间"),
    THERE_IS_AN_OVERLAP_BETWEEN_THE_TWO_TIME_PERIODS("两个时间段之间存在重叠，请检查时间"),
    THERE_IS_AN_OVERLAP_OF_THE_THREE_TIME_PERIODS("三个时间段存在交集重叠，请检测时间"),
    THE_SAME_DEVICE_TYPE_EXISTS("已经存在相同设备类型"),
    HOSPITAL_TYPE_ID_NOT_NULL("医院类型id不能为空"),
    DEVICES_EXIST_UNDER_THIS_DEVICE_TYPE("当前设备类型存在设备无法进行删除，请先删除对应的设备"),
    NAME_ALREADY_EXISTS("监控参数类型名称已存在"),
    DEVICE_NAME_ALREADY_EXISTS("设备名称已存在"),
    PROBE_INFORMATION_ALREADY_EXISTS("添加探头失败，探头信息已存在"),
    EQUIPMENT_PROBE_AND_DETECTION_TYPE_MISMATCH("设备探头与检测类型不匹配"),
    FAILED_TO_DELETE("删除失败，当前设备已绑定探头"),
    FAILED_TO_UPDATE_DEVICE("修改设备失败，sn已被占用"),
    FAILED_TO_ADD_DEVICE("添加设备失败,sn已被占用"),
    START_TIME_OR_END_TIME_CANNOT_BE_EMPTY("开始时间或结束时间不能为空"),
    END_TIME_CANNOT_BE_EARLIER_THAN_START_TIME("结束时间不能早于开始时间"),
    NO_PROBE_INFO_FOUND("暂无设备探头信息"),
    FILE_NAME_IS_TOO_LONG("文件名称太长"),
    FILE_MUST_NOT_EXCEED_50M("文件不得超过50M"),
    WRONG_FILE_FORMAT("文件格式错误"),
    HOSPITAL_DEVICE_TYPE_DOES_NOT_EXIST("医院设备类型不存在");

    private String message;
    public static LabSystemEnum from(String message) {
        return Arrays
                .stream(LabSystemEnum.values())
                .filter(c -> c.getMessage().equals(message))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", message));
    }
}
