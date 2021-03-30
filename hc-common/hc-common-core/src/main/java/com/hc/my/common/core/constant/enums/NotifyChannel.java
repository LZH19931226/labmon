package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author LiuZhiHao
 * @date 2019/10/24 13:56
 * 描述:
 **/
@Getter
public enum NotifyChannel {
    /**推送类型*/
    SMS("SMS", "短信"),
    MAIL("MAIL", "邮件"),
    PUSH("PUSH", "WEB推送"),
    TEST("DemoUse","测试用"),
    ;

    private String code;
    private String name;

    NotifyChannel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static NotifyChannel from(String code) {
        return Arrays
                .stream(NotifyChannel.values())
                .filter(c -> c.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", code));
    }}

