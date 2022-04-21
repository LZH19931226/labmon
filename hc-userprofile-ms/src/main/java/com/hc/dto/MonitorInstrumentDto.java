package com.hc.dto;

import com.hc.po.MonitorInstrumentPo;
import lombok.*;
import lombok.experimental.Accessors;

/**
 *
 * @author hc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MonitorInstrumentDto extends MonitorInstrumentPo {

    /** 仪器编号 */
    private String  instrumentNo;

    /** 仪器名称 */
    private String  instrumentName;

    /** 设备编号 */
    private String equipmentNo;

    /** 仪器类型id */
    private String instrumentTypeId;

    private  String sn;

    /** 智能报警限制次数 */
    private Long alarmTime;

    /** 医院编码 */
    private String hospitalCode;
}
