package com.hc.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysNationalDto {

    /** 主键id */
    private Integer nationalId;

    /** 国家名称 */
    private String name;

    /** 电话编号 */
    private String code;

    /** 国旗图标 */
    private String svgIcon;

    /** 排序编号 */
    private int orderId;

}
