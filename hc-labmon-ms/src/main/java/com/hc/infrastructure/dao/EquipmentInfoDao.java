package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.MonitorEquipmentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EquipmentInfoDao extends BaseMapper<MonitorEquipmentDto> {

    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);


    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page,@Param("probeCommand")ProbeCommand probeCommand);

    List<MonitorEquipmentDto> batchGetEquipmentInfo(@Param("equipmentNoList") List<String> equipmentNoList);

    List<MonitorEquipmentDto> getAll();

    List<MonitorEquipmentDto> getEquipmentInfoBySn(@Param("param") ProbeCommand probeCommand);

    String getEqTypeIdByEno(String equipmentNo);

    List<MonitorEquipmentDto> getEquipmentStateInfo(@Param("probeCommand") ProbeCommand probeCommand);
}
