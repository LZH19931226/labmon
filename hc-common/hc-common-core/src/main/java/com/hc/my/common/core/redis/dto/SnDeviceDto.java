package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
//缓存sn对应设备信息相关的内容
public class SnDeviceDto implements Serializable {

    /** 设备编号 */
    private String equipmentNo;

    /** 设备类型编号 */
    private String equipmentTypeId;

    /** 医院编号 */
    private String hospitalCode;

    /** 设备名称 */
    private String equipmentName;

    /** 设备品牌 */
    private String equipmentBrand;

    /** 是否显示 */
    private Long clientVisible;

    /**  */
    private Long sort;

    /** 全天警报 1=开启 0=关闭 */
    private String alwaysAlarm;

    /** 探头编号 */
    private String instrumentNo;

    /** 探头名称 */
    private String instrumentName;

    /** 检测类型id */
    private String instrumentTypeId;

    /** sn号 */
    private String sn;

    /** 智能报警限制次数 */
    private Long alarmTime;

    /** 管道 */
    private String channel;

    /** 报警时段 */
    private List<MonitorEquipmentWarningTimeDto> warningTimeList;

    /** 设备状态 0为正常 1为报警中 */
    private String state;

    /**设备报警开关0为关闭1为开启*/
    private String warningSwitch;

    /** 备注 */
    private String remark;

    /** 市电恢复通知 */
    private String upsNotice;

    /** 设备地址 */
    private String address;
}
