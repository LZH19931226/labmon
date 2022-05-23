package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum LabManageMentServiceEnum {
    //用户存放异常的探头数据(W+hospitalCode,instrumentParamConfigNo,WarningRecordDto)
    W("W"),
    //用于存储设备信息,feildkey为sn号,value为设备信息,包含设备时间段报警信息
    DEVICEINFO("DEVICEINFO"),
    //用于存储探头信息，feildKey设备为探头表主键:监测类型主键
    P("P");


    private String code;

    LabManageMentServiceEnum(String code){
        this.code = code;
    }



}
