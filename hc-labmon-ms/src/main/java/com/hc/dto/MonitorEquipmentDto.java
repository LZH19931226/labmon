package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "monitorequipment")
public class MonitorEquipmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @TableId(type = IdType.INPUT,value = "equipmentno")
    private String equipmentno;

    /**
     * 设备类型编码
     */
    @TableField(value = "equipmenttypeid")
    private String equipmenttypeid;

    /**
     * 医院编号
     */
    @TableField(value = "hospitalcode")
    private String hospitalcode;

    /**
     * 设备名称
     */
    @TableField(value = "equipmentname")
    private String equipmentname;

    /**
     * 设备品牌
     */
    @TableField(value = "equipmentbrand")
    private String equipmentbrand;

    /**
     * 是否显示
     */
    @TableField(value = "clientvisible")
    private Boolean clientvisible;

    /**
     * 设备地址
     */
    @TableField(value = "address")
    private Boolean address;

    @TableField(exist = false)
    private String  sn;

    @TableField(exist = false)
    private MonitorequipmentlastdataDto monitorequipmentlastdataDto;

    @TableField(exist = false)
    private List<InstrumentMonitorInfoDto> instrumentMonitorInfoDtoList;

    /**
     * 全天报警
     */
    private String alwayalarm;

    /**
     * 最低值  ： 最低值为0 就是报警信号开
     */
    @TableField(exist = false)
    private String lowlimit;

    /**
     * 设备状态
     */
    @TableField(value = "state")
    private String state;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**设备报警开关0为关闭1为开启*/
    @TableField(value = "warning_switch")
    private String warningSwitch;

    @TableField(exist = false)
    private String instrumentNo;

    @TableField(exist = false)
    /** 设备型号id  */
    private String instrumenttypeid;

    @TableField(exist = false)
    private String equipmentTypeName;

}
