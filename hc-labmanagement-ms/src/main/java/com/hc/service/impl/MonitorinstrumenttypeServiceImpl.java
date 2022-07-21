package com.hc.service.impl;

import com.hc.dto.MonitorequipmenttypeDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.repository.MonitorinstrumenttypeRepository;
import com.hc.service.MonitorinstrumenttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorinstrumenttypeServiceImpl implements MonitorinstrumenttypeService {

    @Autowired
    private MonitorinstrumenttypeRepository monitorinstrumenttypeRepository;
    /**
     * 查询监控设备类型信息
     *
     * @param instrumenttypeid
     * @return
     */
    @Override
    public MonitorequipmenttypeDTO selectinfo(Integer instrumenttypeid) {
       return monitorinstrumenttypeRepository.selectinfoByTypeid(instrumenttypeid);
    }

    /**
     * 查询所有的监控设备类型信息
     *
     * @return
     */
    @Override
    public List<MonitorinstrumenttypeDTO> seleclAll() {
        return monitorinstrumenttypeRepository.seleclAll();
    }
}
