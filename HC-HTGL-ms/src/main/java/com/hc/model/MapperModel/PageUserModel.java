package com.hc.model.MapperModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-06.
 */
@ApiModel("请求分页模糊查询用户信息模型")
@Getter
@Setter
@ToString
public class PageUserModel {
	
    @ApiModelProperty("模糊查询参数")
    private String fuzzy;
    @ApiModelProperty("医院编码")
    private String hospitalcode;
    @ApiModelProperty("设备类型编码")
    private String equipmenttypeid;
    @ApiModelProperty("设备编码")
    private String equipmentno;
}
