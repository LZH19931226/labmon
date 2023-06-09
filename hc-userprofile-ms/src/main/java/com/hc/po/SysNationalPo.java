package com.hc.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @author hc
 */
@Data
@Accessors(chain = true)
@TableName(value = "sys_national")
public class SysNationalPo {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId("national_id")
    private Integer nationalId;

    /** 国家名称 */
    @TableField("name")
    private String name;

    /** 电话编号 */
    @TableField("code")
    private String code;

    /** 国旗图标 */
    @TableField("svg_icon")
    private String svgIcon;

    /** 排序编号 */
    @TableField("order_id")
    private int orderId;

}
