package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 监控设备
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "monitorequipment")
public class MonitorEquipmentPo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 设备号 */
    @TableId
    @TableField(value = "equipmentno")
    private String equipmentNo;

    /** 设备类型id */
    @TableField(value = "equipmenttypeid")
    private String  equipmentTypeId;

    /** 医院编码 */
    @TableField(value = "hospitalcode")
    private String hospitalCode;

    /** 设备名称 */
    @TableField(value = "equipmentname")
    private String equipmentName;

    /** 设备品牌 */
    @TableField(value = "equipmentbrand")
    private String equipmentBrand;

    /** 是否显示  */
    @TableField(value = "clientvisible")
    private Long clientVisible;

    /** 分类 */
    @TableField(value = "sort")
    private Long sort;

    /** 全天警报 1=开启 0=关闭 */
    @TableField(value = "alwayalarm")
    private String alwaysAlarm;
}
