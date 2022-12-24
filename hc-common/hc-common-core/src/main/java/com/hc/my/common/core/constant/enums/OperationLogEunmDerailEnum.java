package com.hc.my.common.core.constant.enums;

import com.hc.my.common.core.exception.IedsException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum OperationLogEunmDerailEnum {
    ADD("0", "新增"),
    EDIT("1","修改"),
    REMOVE("2","删除"),
    EXPORT("3","导出");
    String code;
    String message;

    public static OperationLogEunmDerailEnum from(String code){
        return Arrays
                .stream(OperationLogEunmDerailEnum.values())
                .filter(res->code.equals(res.getCode()))
                .findFirst()
                .orElseThrow(()-> new IedsException("Illegal enum value {}", code));
    }

}
