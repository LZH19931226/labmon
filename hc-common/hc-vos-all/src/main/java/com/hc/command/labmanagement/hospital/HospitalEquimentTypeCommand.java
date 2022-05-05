package com.hc.command.labmanagement.hospital;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HospitalEquimentTypeCommand {
    /**
     * 设备类型编码
     */
    @ApiModelProperty(value="设备类型编码")
    private String equipmentTypeId;
    /**
     * 医院编号
     */
    @ApiModelProperty(value="医院编号")
    private String hospitalCode;
    /**
     * 是否显示
     */
    @ApiModelProperty(value="是否显示")
    private String isVisible;
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer orderNo;
    /**
     * 设备类型超时报警设置
     */
    @ApiModelProperty(value="设备类型超时报警设置")
    private String timeout;
    /**
     * 设备类型超时时间设置
     */
    @ApiModelProperty(value="设备类型超时时间设置")
    private Integer timeoutTime;
    /**
     * 全天报警
     */
    @ApiModelProperty(value="全天报警")
    private String alwaysAlarm;


    /** 分页大小 */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;

    /** 当前页 */
    @ApiModelProperty(value = "当前页")
    private Long pageCurrent;
}
