package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.InstrumentMonitorInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InstrumentMonitorInfoDao extends BaseMapper<InstrumentMonitorInfoDto> {

    List<InstrumentMonitorInfoDto> selectInstrumentMonitorInfoByEqNo(@Param("equipmentNoList") List<String> equipmentNoList);
}
