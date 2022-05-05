package com.hc.model;

import java.util.List;

import com.hc.po.Monitorinstrument;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 16956 on 2018-08-01.
 */
@Getter
@Setter
@ToString
public class EquipmentCurrentDateModel {

    /**
     * 设备编号
     */

    private String equipmentno;

    /**
     * 记录时间
     */

    private String inputdatetime;



    /**
     * 获取设备编号
     *
     * @return equipmentno - 设备编号
     */
    public String getEquipmentno() {
        return equipmentno;
    }

    /**
     * 设置设备编号
     *
     * @param equipmentno 设备编号
     */
    public void setEquipmentno(String equipmentno) {
        this.equipmentno = equipmentno;
    }


    /**
     * 医院编号
     */
    private String hospitalcode;


    /**
     * 当前温度
     */
    private String currenttemperature;

    /**
     * 当前二氧化碳
     */
    private String currentcarbondioxide;

    /**
     * 当前O2
     */
    private String currento2;

    /**
     * 当前气流
     */
    private String currentairflow;

    /**
     * 当前开门记录
     */
    private String currentdoorstate;

    /**
     * 当前湿度
     */
    private String currenthumidity;

    /**
     * 当前空气质量
     */
    private String currentvoc;

    /**
     * 当前甲醛
     */
    private String currentformaldehyde;

    /**
     * 当前PM2_5
     */
    private String currentpm25;

    /**
     * 当前PM10
     */
    private String currentpm10;

    /**
     * 当前市电是否异常
     */
    private String currentups;

    /**
     * 当前电量
     */
    private String currentqc;

    private String equipmentname;

    private List<Monitorinstrument> sn;
}
