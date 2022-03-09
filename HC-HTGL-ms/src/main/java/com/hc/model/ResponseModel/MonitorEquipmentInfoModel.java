package com.hc.model.ResponseModel;

import com.alibaba.fastjson.annotation.JSONField;
import com.hc.entity.MonitorEquipmentWarningTime;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

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

    private String sn;

    /**
     * 全天报警
     *  1=全天报警`1
     *  0=不全天报警
     */
    private String alwayalarm;


    /**
     * 报警时间段
     */
    List<MonitorEquipmentWarningTime> warningTimeList;
}
