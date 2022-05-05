package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "monitorequipment")
@Data
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

    private static final long serialVersionUID = 1L;


}