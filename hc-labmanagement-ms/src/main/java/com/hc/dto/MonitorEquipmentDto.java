package com.hc.dto;

import com.hc.po.MonitorEquipmentPo;
import com.hc.vo.equimenttype.WarningTimeVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MonitorEquipmentDto extends MonitorEquipmentPo {

    /** 设备号 */
    private String equipmentNo;

    /** 设备类型id */
    private String  equipmentTypeId;

    /** 医院编码 */
    private String hospitalCode;

    /** 医院名称 */
    private String  hospitalName;

    /** 设备类型名称 */
    private String equipmentTypeName;

    /** 设备名称 */
    private String equipmentName;

    /** 设备型号 */
    private String equipmentBrand;

    /** 是否显示  */
    private Long clientVisible;

    /** 分类 */
    private Long sort;

    /** 全天警报 1=开启 0=关闭 */
    private String alwaysAlarm;

    /** sn */
    private String sn;

    /** 监控设备 */
    private String instrumentTypeName;

    /** 仪器类型id */
    private String instrumentTypeId;

    /** 仪器id */
    private String instrumentNo;

    /** 超时时段集合 */
    private List<WarningTimeVo> warningTimeList;

    /** 管道 */
    private String channel;

    /** 饱和值 */
    private String saturation;

    /** 设备状态 */
    private String state;
}
