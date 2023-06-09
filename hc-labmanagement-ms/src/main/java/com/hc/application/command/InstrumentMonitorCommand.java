package com.hc.application.command;

import com.hc.dto.InstrumentmonitorDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class InstrumentMonitorCommand {

    /**
     * 监控参数类型编码
     */
    private Integer instrumentConfigId;

    /**
     * 探头类型编码
     */
    private Integer instrumentTypeId;

    /**
     * 当前分页
     */
    private Integer pageCurrent;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 最低限值
     */
    @ApiModelProperty(value = "最低限值")
    private BigDecimal lowlimit;

    /**
     * 最高限值
     */
    @ApiModelProperty(value = "最高限值")
    private BigDecimal highlimit;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private BigDecimal saturation;

    /**
     * 通道
     */
    private String channel;

    /**
     * 单位
     */
    private String unit;

    /**
     * 样式最小值
     */
    private String styleMin;

    /**
     * 样式最大值
     */
    private String styleMax;

    /**
     * 最高限值
     */
    private List<InstrumentmonitorDTO> instrumentmonitorDTOList;
}
