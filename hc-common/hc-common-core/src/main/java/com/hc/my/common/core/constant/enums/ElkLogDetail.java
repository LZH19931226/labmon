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
    MSCT_SERIAL_NUMBER07(7,"报警规则1,探头未开启报警"),
    MSCT_SERIAL_NUMBER08(8,"特殊报警规则,市电不走探头报警次数限制,只要开启报警,产生报警立马则报警"),
    MSCT_SERIAL_NUMBER09(9,"报警规则2,当前设备探头数据超过阀值,但是还未超过探头设置报警次数限制"),
    MSCT_SERIAL_NUMBER11(10,"报警规则3,当前设置产生报警,但是未超过医院设置的报警时间间隔"),
    MSCT_SERIAL_NUMBER13(11,"报警规则4,设备处于工作时段之内,则不产生报警通知"),
    MSCT_SERIAL_NUMBER14(12,"报警规则5,该医院无报警通知人员"),
    MSCT_SERIAL_NUMBER15(13,"探头产生报警,电话通知"),
    MSCT_SERIAL_NUMBER16(14,"探头产生报警,短信通知"),
    MSCT_SERIAL_NUMBER17(15,"探头产生报警,电话、短信通知");

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
