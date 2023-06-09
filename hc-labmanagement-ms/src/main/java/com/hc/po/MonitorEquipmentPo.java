package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
    @TableId(value = "equipmentno",type = IdType.INPUT)
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
    private Integer sort;

    /** 全天警报 1=开启 0=关闭 */
    @TableField(value = "alwayalarm")
    private String alwaysAlarm;

    /** 设备状态 */
    @TableField(value = "state")
    private String state;

    /** 创建时间 */
    @TableField(value = "create_time")
    private Date createTime;

    /**设备报警开关0为关闭1为开启*/
    @TableField(value = "warning_switch")
    private String warningSwitch;

    /** 备注 */
    @TableField(value = "remark")
    private String remark;

    /** 市电恢复通知 0和空为不开启 1为开启 */
    @TableField(value = "ups_notice")
    private String upsNotice;

    /** 设备地址 */
    private String address;

    /** 公司 */
    private String company;

    /** 品牌*/
    private String brand;

    /** 型号 */
    private String model;
}
