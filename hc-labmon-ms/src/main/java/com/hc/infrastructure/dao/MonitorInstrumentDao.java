package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.MonitorinstrumentDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MonitorInstrumentDao extends BaseMapper {

    List<MonitorinstrumentDto> selectMonitorInstrumentByEnoList(@Param("enoList") List<String> enoList);
}
