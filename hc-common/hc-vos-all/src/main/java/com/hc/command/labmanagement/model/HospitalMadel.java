package com.hc.command.labmanagement.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 本类用于服务之间的传递
 */
@Data
public class HospitalMadel {
    /** 医院编号 */
    @ApiModelProperty(value = "医院id")
    private String hospitalCode;

    /** 医院名称 */
    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    /** 是否可用 */
    @ApiModelProperty(value = "是否禁用")
    private String isEnable;

    /** 医院全称 */
    @ApiModelProperty(value = "医院全称")
    private String hospitalFullName;

    /** 全天报警 */
    @ApiModelProperty(value = "全天报警")
    private String alwaysAlarm;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    /**
     * 报警时间间隔
     */
    private String timeInterval;

    /** 超时变红时长  */
    private String timeoutRedDuration;

    /**
     * 是否开启声光报警是1否0
     */
    private String soundLightAlarm;

    /**
     * 医院对应得设备类型集合
     */
    private List<HospitalEquipmentTypeModel> hospitalEquipmentTypeModelList;
}
