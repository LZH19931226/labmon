package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @author hc
 * 监控设备警告时间
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "monitorequipmentwarningtime")
public class MonitorEquipmentWarningTimePo {

    @TableId(type = IdType.AUTO)
    @TableField(value = "timeblockid")
    private Long timeBlockId;

    /**  */
    @TableField(value = "equipmentid")
    private String equipmentId;

    /** 警报起始时间*/
    @TableField(value = "begintime")
    private Date beginTime;

    /** 警报结束时间 */
    @TableField(value = "endtime")
    private Date endTime;

    /** 设备类别(TYPE:设备类型, EQ:单个设备) */
    @TableField(value = "equipmentcategory")
    private String equipmentCategory;

    /** 医院编码 */
    @TableField(value = "hospitalcode")
    private String hospitalCode;
}
