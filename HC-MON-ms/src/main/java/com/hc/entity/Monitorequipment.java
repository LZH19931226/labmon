package com.hc.po;

import com.hc.model.InstrumentMonitorInfoModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
@ToString
public class Monitorequipment implements Serializable {
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

    private Monitorequipmentlastdata monitorequipmentlastdata;

    private List<InstrumentMonitorInfoModel> instrumentMonitorInfoModel;
    /**
     * 最低值  ： 最低值为0 就是报警信号开
     */
    private String lowlimit;

    private static final long serialVersionUID = 1L;


}