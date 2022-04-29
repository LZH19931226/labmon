package com.hc.vo.equimenttype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author liuzhihao
 * @date 2022-04-18 15:27:01
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ApiModel(value = "monitorinstrument")
public class MonitorinstrumentVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "")
    private String instrumentno;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String instrumentname;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String equipmentno;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private Integer instrumenttypeid;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String sn;
    /**
     * 智能报警限制次数
     */
    @ApiModelProperty(value = "智能报警限制次数")
    private Integer alarmtime;
    /**
     * 医院编码
     */
    @ApiModelProperty(value = "医院编码")
    private String hospitalcode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String channel;

}

