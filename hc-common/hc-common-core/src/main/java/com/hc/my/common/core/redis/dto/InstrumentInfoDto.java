package com.hc.my.common.core.redis.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
//后台管理探头缓存对象
public class InstrumentInfoDto {

    /** 设备名称 */
    private String equipmentName;

    /** 探头主键 */
    private String instrumentNo;

    /** 探头名称 */
    private String instrumentName;

    /** 设备主键 */
    private String equipmentNo;

    /** 设备类型主键 */
    private Integer instrumentTypeId;

    private String instrumentConfigName;

    /** 医院id */
    private String hospitalCode;

    /** sn */
    private String sn;

    /** 智能报警限制次数 默认是3*/
    private Integer alarmTime;

    /** 检测类型主键  监控参数类型  CO2  O2   甲醛 类似于这种*/
    private Integer instrumentConfigId;

    /** 探头检测信息主键 */
    private String instrumentParamConfigNO;

    /** 最低值 */
    private BigDecimal lowLimit;

    /** 最高值 */
    private BigDecimal highLimit;

    /** 是否启用报警 */
    private String warningPhone;

    /** 校验正负值 */
    private String calibration;

    /** 饱和值  */
    private BigDecimal saturation;

    /** 最新报警时间  */
    private Date  warningtime;

    /** 设备探头状态 */
    private String state;
}
