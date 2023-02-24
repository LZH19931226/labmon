package com.hc.application.command;

import com.hc.dto.MonitorinstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WiredEqCommand {

    /** 医院id */
    private String hospitalCode;

    /** 设备id */
    private String equipmentNo;

    /** 设备名称 */
    private String equipmentName;

    /** 类型id 如：环境 */
    private String equipmentTypeId;

    /** 设备类型id 如：MT100 */
    private Integer instrumentTypeId;

    /** 备注 */
    private String remark;

    /** 位置信息 */
    private String address;

    /** 排序 */
    private Integer sort;

    /** 是否启用 */
    private Long clientVisible;

    /** 设备型号 */
    private String equipmentBrand;

    private List<InstrumentparamconfigCommand> probeList;

}
