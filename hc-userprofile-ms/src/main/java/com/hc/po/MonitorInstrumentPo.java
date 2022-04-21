package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 监控工具
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "monitorinstrument")
public class MonitorInstrumentPo {

    /** 仪器编号 */
    @TableId
    @TableField(value = "instrumentno")
    private String  instrumentNo;

    /** 仪器名称 */
    @TableField(value = "instrumentname")
    private String  instrumentName;

    /** 设备编号 */
    @TableField(value = "equipmentno")
    private String equipmentNo;

    /** 仪器类型id */
    @TableField(value = "instrumenttypeid")
    private String instrumentTypeId;

    @TableField(value = "sn")
    private  String sn;

    /** 智能报警限制次数 */
    @TableField(value = "alarmtime")
    private Long alarmTime;

    /** 医院编码 */
    @TableField(value = "hospitalcode")
    private String hospitalCode;
}
