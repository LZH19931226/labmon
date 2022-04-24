package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.po.InstrumentmonitorPo;

import java.util.List;


public interface InstrumentmonitorRepository extends IService <InstrumentmonitorPo>{


    List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid);

    /**
     * 插入监控仪器信息
     * @param instrumentmonitorDTO
     */
    void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO);
}