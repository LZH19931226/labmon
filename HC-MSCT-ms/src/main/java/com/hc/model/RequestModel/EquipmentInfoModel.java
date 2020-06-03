package com.hc.model.RequestModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("添加设备信息模型")
public class EquipmentInfoModel {
    private String equipmentname;
    private String sn;
    @ApiModelProperty("后台解析服务配置sn")
    private String configSn;
    private Integer instrumenttypeid;// 探头类型id 表示是MT100  MT200 MT300 还是什么鬼
    private String equipmenttypeid;
    private String hospitalcode;
    @ApiModelProperty("是否显示")
    private boolean clientvisible;

    private String equipmentno;

    private String equipmentbrand;
}
