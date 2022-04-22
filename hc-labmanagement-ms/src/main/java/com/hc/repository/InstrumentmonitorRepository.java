package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.po.InstrumentmonitorPo;

import java.util.List;


public interface InstrumentmonitorRepository extends IService <InstrumentmonitorPo>{


    List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid);
}