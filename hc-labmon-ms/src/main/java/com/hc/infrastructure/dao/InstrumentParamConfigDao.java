package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.InstrumentParamConfigDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InstrumentParamConfigDao extends BaseMapper<InstrumentParamConfigDto> {

    @Select("select * from instrumentparamconfig t1 LEFT JOIN  instrumentconfig t2 ON t2.instrumentconfigid = t1.instrumentconfigid  where instrumentparamconfigNO = #{instrumentParamConfigNo}")
    InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo);
}
