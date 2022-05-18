package com.hc.dto;

import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class MonitorEquipmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    private String equipmentno;

    /**
     * 设备类型编码
     */
    private String equipmenttypeid;

    /**
     * 医院编号
     */
    private String hospitalcode;

    /**
     * 设备名称
     */
    private String equipmentname;

    /**
     * 设备品牌
     */
    private String equipmentbrand;

    /**
     * 是否显示
     */
    private Boolean clientvisible;

    private String  sn;

    private MonitorequipmentlastdataDto monitorequipmentlastdataDto;

    private List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList;
    /**
     * 最低值  ： 最低值为0 就是报警信号开
     */
    private String lowlimit;


}
