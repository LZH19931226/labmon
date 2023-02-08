package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;

import java.util.List;

public interface EquipmentInfoRepository extends IService<MonitorEquipmentDto> {


    /**
     * 查询设备信息
     *
     * @param equipmentNo
     * @return
     */
    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);

    /**
     * 分页获取设备编号
     *
     * @param page
     * @return
     */
    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page, ProbeCommand probeCommand);

    List<MonitorEquipmentDto> batchGetEquipmentInfo(List<String> equipmentNoList);

    List<MonitorEquipmentDto> getAll();

    List<MonitorEquipmentDto> getEquipmentInfoBySn(ProbeCommand probeCommand);
}
