package com.hc.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "monitorequipmentwarningtime")
public class MonitorEquipmentWarningTimeDTO {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "timeblockid",type = IdType.AUTO)
    private Integer timeblockid;
    /**
     * 设备id或者设备类别id
     */
    @TableField(value = "equipmentid")
    private String equipmentid;
    /**
     * 警报起始时间
     */
    @TableField(value = "begintime")
    private Date begintime;
    /**
     * 警报结束时间
     */
    @TableField(value = "endtime")
    private Date endtime;
    /**
     * 设备类别(TYPE:设备类型, EQ:单个设备)
     * */
    @TableField(value = "equipmentcategory")
    private String equipmentcategory;
    /**
     * 医院编码
     */
    @TableField(value = "hospitalcode")
    private String hospitalcode;
}
