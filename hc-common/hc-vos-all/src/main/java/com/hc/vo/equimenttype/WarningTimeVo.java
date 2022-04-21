package com.hc.vo.equimenttype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author user
 */
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WarningTimeVo {
    private Long timeBlockId;

    /** 设备id */
    private String equipmentId;

    /** 警报起始时间*/
    private Date beginTime;

    /** 警报结束时间 */
    private Date endTime;

    /** 设备类别(TYPE:设备类型, EQ:单个设备) */
    private String equipmentCategory;

    /** 医院编码 */
    private String hospitalCode;
}
