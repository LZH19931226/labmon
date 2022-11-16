package com.hc.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 医院设备表
 * @author hc
 */
@TableName(value = "hospitalequiment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HospitalEquipmentDto {

    /**
     * 设备类型编码
     */
    @TableField(value = "equipmenttypeid")
    private String equipmentTypeId;

    /**
     * 医院编号
     */
    @TableField(value = "hospitalcode")
    private String hospitalCode;

    /**
     * 是否显示
     */
    @TableField(value = "isvisible")
    private String isVisible;

    /**
     * 排序
     */
    @TableField(value = "orderno")
    private Long orderNo;

    /**
     * 设备类型超时报警设置
     */
    @TableField(value = "timeout")
    private String timeout;

    /**
     * 设备类型超时时间设置
     */
    @TableField(value = "timeouttime")
    private Long timeoutTime;

    /**
     * 全天警报 1=开启 0=关闭
     */
    @TableField(value = "alwayalarm")
    private String alwaysAlarm;

    /** 设备报警中的数量 */
    @TableField(select = false)
    private String alarmNum;

    /** 设备正常的数量 */
    @TableField(select = false)
    private String normalNum;

    @TableField(exist = false)
    private String totalNum;

    @TableField(exist = false)
    private String equipmenttypename;

    @TableField(exist = false)
    private String hospitalname;

}
