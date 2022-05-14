package com.hc.my.common.core.redis.namespace;

import lombok.Getter;

@Getter
public enum LabManageMentServiceEnum {


    //用于存储设备信息,feildkey为sn号,value为设备信息,包含设备时间段报警信息
    DEVICEINFO("DEVICEINFO");


    private String code;

    LabManageMentServiceEnum(String code){
        this.code = code;
    }



}
