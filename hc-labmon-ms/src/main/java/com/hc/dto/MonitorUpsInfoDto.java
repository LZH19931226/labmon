package com.hc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class MonitorUpsInfoDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备no
     */
    private String equipmentNo;

    /**
     * 当前市电是否异常
     */
    private String currentUps;

    /**
     * 电压
     */
    private String voltage;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * sn号
     */
    private String sn;
}
