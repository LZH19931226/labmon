package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@TableName(value = "monitorinstrument")
@Data
public class Monitorinstrument implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 4186073419054465771L;

    private String instrumentno;

    private String instrumentname;

    private String equipmentno;

    private Integer instrumenttypeid;
    private String hospitalcode;
    private String sn;
    //智能报警限制次数
    private Integer alarmtime;

    private String channel;

    /**
     * 全天报警
     *  1=全天报警
     *  0=不全天报警
     */
    @TableField(exist = false) //排除持久化属性
    private String alwayalarm;
    /**
     * 报警时间段
     */
    @TableField(exist = false) //排除持久化属性
    List<MonitorEquipmentWarningTime> warningTimeList;

    @TableField(exist = false) //排除持久化属性
    private String equipmenttypeid;
}