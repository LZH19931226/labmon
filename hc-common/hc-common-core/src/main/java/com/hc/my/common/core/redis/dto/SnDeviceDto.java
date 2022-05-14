package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
    private String sort;

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

}
