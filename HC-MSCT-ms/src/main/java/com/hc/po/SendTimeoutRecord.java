package com.hc.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "send_timeout_record")
@Data
public class SendTimeoutRecord {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 医院id */
    @TableField(value = "hospital_code")
    private String hospitalCode;

    /** 设备类型 */
    @TableField(value = "equipment_type")
    private String equipmentType;

    /** 设备报警数量 */
    @TableField(value = "count")
    private String count;

    /** 手机号码 */
    @TableField(value = "phonenum")
    private String phoneNum;

    /** 报警；类型 0为手机号和短信 1为手机 2为短信 */
    @TableField(value = "send_type")
    private String sendType;

    /** 创建时间 */
    @TableField(value = "create_time")
    private String createTime;

}
