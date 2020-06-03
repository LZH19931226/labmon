package com.hc.model.RequestModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by 16956 on 2018-08-06.
 */
@Getter
@Setter
@ToString
@ApiModel("添加设备信息模型")
public class EquipmentInfoModel {
    private String usernames;

    private String equipmentname;
    private String sn;
    private Integer instrumenttypeid;// 探头类型id 表示是MT100  MT200 MT300 还是什么鬼
    private String equipmenttypeid;
    private String hospitalcode;
    @ApiModelProperty("是否显示")
    private boolean clientvisible;
    private String equipmentno;
    private String equipmentbrand;
    @ApiModelProperty("探头编号")  // 当传递探头编号时候，则表示当前设备已经在后台解析服务里面绑定了探头
    private String instrumentno;
    @ApiModelProperty("开关量通道")
    private String channel;
    @ApiModelProperty("有线设备模型")
    private List<WiredInstrumentModel> list;


}
