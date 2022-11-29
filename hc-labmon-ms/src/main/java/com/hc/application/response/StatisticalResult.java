package com.hc.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统计结果对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalResult {
    /**
     * 医院名称
     */
    private String hospitalName;

    /**
     * 设备类型名称
     */
    private String equipmentTypeName;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备报警次数
     */
    private Long alarmNum;
}
