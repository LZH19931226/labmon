package com.hc.my.common.core.probe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EquipmentCommand {

    CMD88("88","MT600/1100上传UPS协议"),
    HEART_RATE_RESPONSE("484308000323","固定应答心跳包指令");




    private String cmdId;
    private String describe;
}
