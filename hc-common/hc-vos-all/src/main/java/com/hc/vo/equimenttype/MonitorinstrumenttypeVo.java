package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author liuzhihao
 * @date 2022-04-18 15:27:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ApiModel(value = "monitorinstrumenttype")
public class MonitorinstrumenttypeVo implements Serializable {
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


	/**
	 * 探头信息
	 */
	@ApiModelProperty(value = "探头信息")
    private List<InstrumentmonitorVo> instrumentmonitorVos;

}

