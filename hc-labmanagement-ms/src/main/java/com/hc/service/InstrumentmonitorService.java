package com.hc.service;


import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;

import java.util.List;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface InstrumentmonitorService {


    List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid);

    /**
     * 插入监控仪器信息
     * @param instrumentmonitorDTO
     */
    void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO);
}

