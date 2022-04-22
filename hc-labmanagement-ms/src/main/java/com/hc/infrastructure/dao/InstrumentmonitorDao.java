package com.hc.infrastructure.dao;

import com.hc.dto.InstrumentmonitorDTO;
import com.hc.po.InstrumentmonitorPo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */

public interface InstrumentmonitorDao extends BaseMapper<InstrumentmonitorPo> {

    List<InstrumentmonitorDTO> selectMonitorEquipmentType(@Param("instrumenttypeid") String instrumenttypeid);
}
