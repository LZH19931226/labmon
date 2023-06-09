package com.hc.po;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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

    @TableField(exist = false)
    private Boolean clientvisible;




}
