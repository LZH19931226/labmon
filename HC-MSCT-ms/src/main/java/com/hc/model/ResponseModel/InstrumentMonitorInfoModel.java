package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("探头监控类型参数模型")
@Component
public class InstrumentMonitorInfoModel {
    @ApiModelProperty("探头类型编码")
    private Integer instrumenttypeid;

    @ApiModelProperty("监控参数类型编码")
    private Integer instrumentconfigid;
    @ApiModelProperty("探头类型名称")
    private String instrumenttypename;
    @ApiModelProperty("监控参数类型名称")
    private String instrumentconfigname;

    private BigDecimal lowlimit;

    private BigDecimal highlimit;
    @ApiModelProperty("智能报警次数") //连续几次才推送报警，条件之一
    private Integer alarmtime;
    @ApiModelProperty("探头监控类型编号")
    private String instrumentparamconfigNO;

    @ApiModelProperty("设备名称")
    private String equipmentname;
    @ApiModelProperty("APP推送时间")
    private Date pushtime;
    @ApiModelProperty("是否启用短信、电话、APP报警推送")
    private String warningphone;
    @ApiModelProperty("探头编号")
    private String instrumentno;
    @ApiModelProperty("报警推送时间")
    private Date warningtime;



}
