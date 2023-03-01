package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.ProbeCommand;
import com.hc.dto.InstrumentParamConfigDto;
import com.hc.dto.MonitorEquipmentDto;
import com.hc.dto.MonitorinstrumentDto;
import com.hc.vo.labmon.model.MonitorEquipmentLastDataModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface EquipmentInfoDao extends BaseMapper<MonitorEquipmentDto> {

    MonitorEquipmentDto getEquipmentInfoByNo(String equipmentNo);


    List<MonitorEquipmentDto> getEquipmentInfoByPage(Page page,@Param("probeCommand")ProbeCommand probeCommand);

    List<MonitorEquipmentDto> batchGetEquipmentInfo(@Param("equipmentNoList") List<String> equipmentNoList);

    List<MonitorEquipmentDto> getAll();

    List<MonitorEquipmentDto> getEquipmentInfoBySn(@Param("param") ProbeCommand probeCommand);
}
