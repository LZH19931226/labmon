package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.MonitorInstrumentDto;
import com.hc.po.MonitorInstrumentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author user
 */
@Mapper
public interface MonitorInstrumentDao extends BaseMapper<MonitorInstrumentPo> {


    /**
     *
     * @return
     */
    @Select("")
    List<MonitorInstrumentDto> selectMonitorInstrumentList();

}
