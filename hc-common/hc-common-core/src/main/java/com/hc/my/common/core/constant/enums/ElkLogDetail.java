package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ElkLogDetail {

    MSWK_SERIAL_NUMBER01(1,"数据存储订阅数据包"),
    MSWK_SERIAL_NUMBER02(2,"当前设备sn号未注册"),
    MSWK_SERIAL_NUMBER03(3,"设备未启用"),
    MSWK_SERIAL_NUMBER04(4,"sn数据相同命令上传间隔异常"),
    MSCT_SERIAL_NUMBER05(5,"报警服务订阅数据包"),
    MSCT_SERIAL_NUMBER06(6,"缓存探头信息不存在"),
    MSCT_SERIAL_NUMBER07(7,"报警规则1,探头是未开启报警"),
    MSCT_SERIAL_NUMBER08(1,"MSWK数据存储订阅数据包"),
    MSCT_SERIAL_NUMBER09(1,"MSWK数据存储订阅数据包"),
    MSCT_SERIAL_NUMBER10(1,"MSWK数据存储订阅数据包"),
    MSCT_SERIAL_NUMBER11(1,"MSWK数据存储订阅数据包"),
    MSCT_SERIAL_NUMBER12(1,"MSWK数据存储订阅数据包");


    private int code;
    private String message;

    public static ElkLogDetail from(int code) {
        return Arrays
                .stream(ElkLogDetail.values())
                .filter(c -> c.getCode()==(code))
                .findFirst()
                .orElseThrow(() -> new IedsException("Illegal enum value {}", code));
    }
}
