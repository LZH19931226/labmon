package com.hc.infrastructure.dao;

import com.hc.dto.InstrumentconfigDTO;
import com.hc.po.InstrumentparamconfigPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Mapper
public interface InstrumentparamconfigDao extends BaseMapper<InstrumentparamconfigPo> {

    @Select("SELECT\n" +
            "\tt3.instrumentconfigid,\n" +
            "\tt3.instrumentconfigname \n" +
            "FROM\n" +
            "\tmonitorinstrument t1\n" +
            "\tLEFT JOIN instrumentmonitor t2 ON t1.instrumenttypeid = t2.instrumenttypeid\n" +
            "\tLEFT JOIN instrumentconfig t3 ON t2.instrumentconfigid = t3.instrumentconfigid \n" +
            "WHERE\n" +
            "\tt1.equipmentno = #{equipmentNo}")
    List<InstrumentconfigDTO> selectInstrumentparamconfigByEqNo(@Param("equipmentNo") String equipmentNo);
}
