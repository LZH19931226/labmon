package com.hc.my.common.core.redis.dto;

import lombok.Data;

import java.io.Serializable;

@Data
//缓存sn对应设备信息相关的内容
public class SnDeviceDto implements Serializable {


    private String sn;


}
