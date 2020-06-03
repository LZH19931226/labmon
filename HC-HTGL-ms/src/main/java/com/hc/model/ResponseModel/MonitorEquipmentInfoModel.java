package com.hc.model.ResponseModel;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-07.
 */
@Getter
@Setter
@ToString
@ApiModel("设备类型信息")
public class MonitorEquipmentInfoModel {

    private String hospitalcode;

    private String hospitalname;

    private String equipmenttypeid;

    private String equipmentname;

    private boolean clientvisible;

    private String equipmenttypename;

    private String equipmentbrand;

    private String equipmentno;
}
