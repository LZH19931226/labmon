package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.InstrumentParamConfigDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InstrumentParamConfigDao extends BaseMapper<InstrumentParamConfigDto> {

    @Select("select * from instrumentparamconfig t1 LEFT JOIN  instrumentconfig t2 ON t2.instrumentconfigid = t1.instrumentconfigid  where instrumentparamconfigNO = #{instrumentParamConfigNo}")
    InstrumentParamConfigDto getProbeInfo(String instrumentParamConfigNo);

    @Select("SELECT\n" +
            "\tt3.instrumentconfigName,\n" +
            "\tt1.lowlimit,\n" +
            "\tt1.highlimit\n" +
            "FROM\n" +
            "\tinstrumentparamconfig t1\n" +
            "LEFT JOIN monitorinstrument t2 ON t1.instrumentno = t2.instrumentno\n" +
            "LEFT JOIN instrumentconfig t3 ON t1.instrumentconfigid = t3.instrumentconfigid\n" +
            "WHERE t2.equipmentno = #{equipmentNo}")
    List<InstrumentParamConfigDto> getInstrumentParamConfigByENo(String equipmentNo);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByENoList(@Param("eNoList") List<String> eNoList);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByCode(String hospitalCode);

    List<InstrumentParamConfigDto> batchGetProbeInfo(@Param("configParamNoList") List<String> configParamNoList);

    List<InstrumentParamConfigDto> getInstrumentParamConfigInfo(String equipmentNo);

    void batchUpdateProbeAlarmState(@Param("warningPhone") String warningPhone,@Param("equipmentNo") String equipmentNo);

    List<InstrumentParamConfigDto> getInstrumentParamConfigByCodeAndTypeId(@Param("hospitalCode") String hospitalCode,@Param("equipmentTypeId") String equipmentTypeId);
}
