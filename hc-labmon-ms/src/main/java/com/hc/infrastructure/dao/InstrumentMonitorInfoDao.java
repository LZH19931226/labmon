package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.application.command.EquipmentDataCommand;
import com.hc.dto.InstrumentMonitorInfoDto;
import com.hc.dto.InstrumentTypeNumDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstrumentMonitorInfoDao extends BaseMapper<InstrumentMonitorInfoDto> {

    List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(@Param("equipmentNoList") List<String> equipmentNoList);

    List<InstrumentTypeNumDto> getEquipmentTypeNum(@Param("param") EquipmentDataCommand equipmentDataCommand);
}
