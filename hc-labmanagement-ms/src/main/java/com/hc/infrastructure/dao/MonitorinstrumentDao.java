package com.hc.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hc.dto.MonitorinstrumentDTO;
import com.hc.po.MonitorinstrumentPo;
import org.apache.ibatis.annotations.Select;

/**
 *
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface MonitorinstrumentDao extends BaseMapper<MonitorinstrumentPo> {

    @Select("select count(*) FROM instrumentparamconfig i  LEFT JOIN  monitorinstrument m  ON i.instrumentno = m.instrumentno WHERE m.equipmentno = #{equipmentNo}")
    Integer findProbeInformationByEno(String equipmentNo);

    @Select("select t1.*,t2.equipmentname from monitorinstrument t1 left join monitorequipment t2 on t1.equipmentno = t2.equipmentno where t1.instrumentno =  #{instrumentno}")
    MonitorinstrumentDTO selectMonitorInstrumentInfo(String instrumentno);
}
