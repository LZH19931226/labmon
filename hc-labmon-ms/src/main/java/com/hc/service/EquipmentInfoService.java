package com.hc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;

import java.util.List;

public interface EquipmentInfoService {

    /**
     * 查询设备信息
     * @param equipmentNo
     * @return
     */
    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);

    /**
     * 分页获取设备编号
     * @param page
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand);

    List<MonitorEquipmentDto> batchGetEquipmentInfo(List<String> equipmentNoList);


    List<MonitorEquipmentDto> getAll();

    void bulkUpdate(List<MonitorEquipmentDto> list);

    List<String> getEnoList(String hospitalCode,String equipmentTypeId);

    List<MonitorEquipmentDto> getEquipmentInfo(ProbeCommand probeCommand);

    String getEqTypeIdByEno(String equipmentNo);
}
