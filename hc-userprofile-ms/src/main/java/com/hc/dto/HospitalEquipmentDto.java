package com.hc.dto;

import com.hc.po.HospitalEquipmentPo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 医院设备传输对象
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HospitalEquipmentDto extends HospitalEquipmentPo {
    /**
     * 设备类型编码
     */
    private String equipmentTypeId;

    /**
     * 医院编号
     */
    private String hospitalCode;

    /**
     * 是否显示
     */
    private String isVisible;

    /**
     * 排序
     */
    private Long orderNo;

    /**
     * 设备类型超时报警设置
     */
    private String timeout;

    /**
     * 设备类型超时时间设置
     */
    private Long timeoutTime;

    /**
     * 全天警报 1=开启 0=关闭
     */
    private String alwaysAlarm;

}
