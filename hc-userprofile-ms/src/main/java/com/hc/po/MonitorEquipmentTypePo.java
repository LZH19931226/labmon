package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 监控设备类型
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "monitorequipmenttype")
public class MonitorEquipmentTypePo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 设备类型id */
    @TableField(value = "equipmenttypeid")
    private String equipmentTypeId;

    /** 设备类型名称 */
    @TableField(value = "equipmenttypename")
    private String equipmentTypeName;
}
