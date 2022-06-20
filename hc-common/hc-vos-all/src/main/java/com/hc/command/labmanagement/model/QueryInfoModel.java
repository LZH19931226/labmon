package com.hc.command.labmanagement.model;

import com.hc.my.common.core.redis.dto.MonitorequipmentlastdataDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryInfoModel {
    /** 设备名称 */
    private String equipmentName;

    private List<MonitorequipmentlastdataDto> monitorEquipmentLastDataDTOList;
}
