package com.hc.my.common.core.redis.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
//探头当前值对象
public class ProbeInfoDto {

    /** 设备名称 */
    private String equipmentName;

    /** 探头主键 */
    private String instrumentNo;

    /** 设备探头名称 */
    private String instrumentName;

    /** 设备主键 */
    private String equipmentNo;

    /** 设备类型主键 */
    private Integer instrumentTypeId;

    /** 医院id */
    private String hospitalCode;

    /** sn */
    private String sn;

    /** 检测类型主键  监控参数类型  CO2  O2   甲醛 类似于这种*/
    private Integer instrumentConfigId;

    /** 设备探头状态 */
    private String state;

    /** 探头当前值 */
    private String value;

    /** 探头监测类型中文名称*/
    private String probeCName;

    /** 探头监测类型英文称*/
    private String probeEName;

    /** 探头数据上传时间*/
    private Date inputTime;

    /** 下限值 */
    private BigDecimal lowLimit;

    /** 上线值 */
    private BigDecimal highLimit;

    /** 饱和值 */
    private BigDecimal saturation;

}
