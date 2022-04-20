package com.hc.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@ApiModel(value = "hospitalequiment")
public class HospitalequimentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备类型编码
     */
    @ApiModelProperty(value = "设备类型编码")
    private String equipmenttypeid;
    /**
     * 医院编号
     */
    @ApiModelProperty(value = "医院编号")
    private String hospitalcode;
    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示")
    private String isvisible;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderno;
    /**
     * 设备类型超时报警设置
     */
    @ApiModelProperty(value = "设备类型超时报警设置")
    private String timeout;
    /**
     * 设备类型超时时间设置
     */
    @ApiModelProperty(value = "设备类型超时时间设置")
    private Integer timeouttime;
    /**
     * 全天报警
     */
    @ApiModelProperty(value = "全天报警")
    private String alwayalarm;

	@ApiModelProperty(value = "医院名称")
    private String hospitalname;

	@ApiModelProperty(value = "设备名称")
    private String equipmenttypename;

}


