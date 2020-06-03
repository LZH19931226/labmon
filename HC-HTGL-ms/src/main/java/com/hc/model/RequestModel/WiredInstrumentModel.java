package com.hc.model.RequestModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by 16956 on 2018-08-30.
 */
@Getter
@Setter
@ToString
@ApiModel("添加有线探头信息模型")
public class WiredInstrumentModel {
    @ApiModelProperty("探头监控类型")
    private Integer instrumentconfigid;
    @ApiModelProperty("最高值")
    private BigDecimal highlimit;
    @ApiModelProperty("最低值")
    private BigDecimal lowlimit;
    private String sn;

}
