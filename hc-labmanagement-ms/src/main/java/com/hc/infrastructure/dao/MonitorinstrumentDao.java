package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.po.MonitorinstrumentPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
@Mapper
public interface MonitorinstrumentDao extends BaseMapper<MonitorinstrumentPo> {

    @Select("select count(*) FROM instrumentparamconfig i  LEFT JOIN  monitorinstrument m  ON i.instrumentno = m.instrumentno WHERE m.equipmentno = #{equipmentNo}")
    Integer findProbeInformationByEno(String equipmentNo);
}
