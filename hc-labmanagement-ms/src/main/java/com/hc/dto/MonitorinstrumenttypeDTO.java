package com.hc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "monitorinstrumenttype")
public class MonitorinstrumenttypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 探头类型编码
     */
    @ApiModelProperty(value = "探头类型编码")
    private Integer instrumenttypeid;
    /**
     * 探头类型名称
     */
    @ApiModelProperty(value = "探头类型名称")
    private String instrumenttypename;
    /**
     * 智能报警限制
     */
    @ApiModelProperty(value = "智能报警限制")
    private Integer alarmtime;

    /** 监控参数类型编码 */
    private Integer instrumentconfigid;

    /** 探头类型编码 */
    private String instrumentconfigname;

    /** 最低限值 */
    private BigDecimal lowlimit;

    /** 最高限值 */
    private BigDecimal highlimit;

    /** 饱和值 */
    private String saturation;

	/**
	 * 探头信息
	 */
	@ApiModelProperty(value = "探头信息")
	private List<InstrumentmonitorDTO> instrumentmonitorDTOS;


}


