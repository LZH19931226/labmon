package com.hc.my.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 用户排班枚举编码
 * @author hc
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public enum UserScheduleEnumCode {
    TIME_PERIOD_DISAGREE("时间段不统一"),
    NO_SCHEDULE_INFORMATION_FOUND("未找到改时间段排班信息"),
    SCHEDULE_INFORMATION_EXISTS("排班信息已存在");
    private String message;
}
