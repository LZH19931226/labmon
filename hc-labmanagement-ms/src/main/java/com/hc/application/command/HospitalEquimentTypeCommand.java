package com.hc.application.command;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class HospitalEquimentTypeCommand {
    /**
     * 设备类型编码
     */
    @ApiModelProperty(value="设备类型编码")
    private String equipmenttypeid;
    /**
     * 医院编号
     */
    @ApiModelProperty(value="医院编号")
    private String hospitalcode;
    /**
     * 是否显示
     */
    @ApiModelProperty(value="是否显示")
    private String isvisible;
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer orderno;
    /**
     * 设备类型超时报警设置
     */
    @ApiModelProperty(value="设备类型超时报警设置")
    private String timeout;
    /**
     * 设备类型超时时间设置
     */
    @ApiModelProperty(value="设备类型超时时间设置")
    private Integer timeouttime;
    /**
     * 全天报警
     */
    @ApiModelProperty(value="全天报警")
    private String alwayalarm;


    /**
     * 工作报警时间段
     */
    private WorkTimeBlockCommand[] workTimeBlock;


}
