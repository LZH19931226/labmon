package com.hc.service.impl;

import com.hc.dto.InstrumentmonitorDTO;
import com.hc.dto.MonitorinstrumenttypeDTO;
import com.hc.repository.InstrumentmonitorRepository;
import com.hc.service.InstrumentmonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstrumentmonitorServiceImpl implements InstrumentmonitorService {

    @Autowired
    private InstrumentmonitorRepository instrumentmonitorRepository;

    @Override
    public List<MonitorinstrumenttypeDTO> selectMonitorEquipmentType(String instrumenttypeid) {
        return instrumentmonitorRepository.selectMonitorEquipmentType(instrumenttypeid);
    }

    /**
     * 插入监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void insertInstrumentmonitorInfo(InstrumentmonitorDTO instrumentmonitorDTO) {
        instrumentmonitorRepository.insertInstrumentmonitorInfo(instrumentmonitorDTO);
    }

    /**
     * 更新监控仪器信息
     *
     * @param instrumentmonitorDTO
     */
    @Override
    public void updateInstrumentmonitor(InstrumentmonitorDTO instrumentmonitorDTO) {
        instrumentmonitorRepository.updateInstrumentmonitor(instrumentmonitorDTO);
    }

    /**
     * 判断插入的信息是否已存在
     *
     * @param instrumentmonitorDTO
     * @return
     */
    @Override
    public boolean selectOne(InstrumentmonitorDTO instrumentmonitorDTO) {
        return instrumentmonitorRepository.selectOne(instrumentmonitorDTO);
    }

    /**
     * 查询监控仪器信息
     *
     * @param instrumenttypeid
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentList(Integer instrumenttypeid) {
        return instrumentmonitorRepository.selectMonitorEquipmentList(instrumenttypeid);
    }

    /**
     * 查询所有探头默认配置
     *
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectMonitorEquipmentAll() {
        return instrumentmonitorRepository.selectMonitorEquipmentAll();
    }

    /**
     * 查询探头监控的信息
     *
     * @return
     */
    @Override
    public List<InstrumentmonitorDTO> selectInstrumentMonitorInfo(String hospitalCode) {
        return instrumentmonitorRepository.selectInstrumentMonitorInfo(hospitalCode);
    }
}
