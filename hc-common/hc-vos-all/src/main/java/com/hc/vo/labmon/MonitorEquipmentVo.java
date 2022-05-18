package com.hc.vo.labmon;

import com.hc.vo.labmon.model.InstrumentMonitorInfoModel;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;

@Builder
@Getter
@Accessors(chain = true)
public class MonitorEquipmentVo {

    private static final long serialVersionUID = 1L;
    /**
     * 设备编号
     */
    private String equipmentNo;

    /**
     * 设备类型编码
     */
    private String equipmentTypeId;

    /**
     * 医院编号
     */
    private String hospitalCode;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备品牌
     */
    private String equipmentBrand;

    /**
     * 是否显示
     */
    private Boolean clientVisible;

    /**
     * 最低值  ： 最低值为0 就是报警信号开
     */
    private String lowLimit;

    private String  sn;

    private MonitorEquipmentLastDataModel monitorEquipmentLastDataModel;

    private List<InstrumentMonitorInfoModel> instrumentMonitorInfoModel;


}
