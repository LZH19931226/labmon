package com.hc.vo.country;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 国家信息视图对象
 * @author hc
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SysNationalVo implements Serializable {

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
