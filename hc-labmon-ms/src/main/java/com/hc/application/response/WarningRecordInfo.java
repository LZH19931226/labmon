package com.hc.application.response;

import com.hc.dto.MonitorEquipmentWarningTimeDTO;
import com.hc.my.common.core.redis.dto.ProbeInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WarningRecordInfo {

    /** 设备名称 */
    private String equipmentName;

    /** sn */
    private String sn;

    /** 报警上传时间 */
    private Date inputDateTime;

    /** 探头信息 */
    private List<ProbeInfoDto> probeInfoDtoList;

    /** 报警时段信息*/
    private List<MonitorEquipmentWarningTimeDTO> warningTimeDTOS;
}
