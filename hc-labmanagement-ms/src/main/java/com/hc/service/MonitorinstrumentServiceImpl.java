package com.hc.service;

import com.hc.dto.MonitorinstrumentDTO;
import com.hc.repository.MonitorinstrumentRepository;
import com.hc.service.MonitorinstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorinstrumentServiceImpl implements MonitorinstrumentService {

    @Autowired
    private MonitorinstrumentRepository monitorinstrumentRepository;

    /**
     * 通过sn和hospitalCode查询sn是否被占用
     *
     * @param monitorinstrumentDTO
     * @return
     */
    @Override
    public Integer selectCount(MonitorinstrumentDTO monitorinstrumentDTO) {
        return monitorinstrumentRepository.selectCount(monitorinstrumentDTO);
    }

    /**
     * 查询监控信息
     *
     * @param equipmentNo 设备id
     * @return
     */
    @Override
    public MonitorinstrumentDTO selectMonitorByEno(String equipmentNo) {
        return monitorinstrumentRepository.selectMonitorByEno(equipmentNo);
    }

    /**
     * 更新监控仪器
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void updateMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        monitorinstrumentRepository.updateMonitorinstrumentInfo(monitorinstrumentDTO);
    }

    /**
     * 插入探头信息
     *
     * @param monitorinstrumentDTO
     */
    @Override
    public void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO) {
        monitorinstrumentRepository.insertMonitorinstrumentInfo(monitorinstrumentDTO);
    }

    /**
     * 删除监控探头信息
     *
     * @param equipmentNo
     */
    @Override
    public void deleteMonitorinstrumentInfo(String equipmentNo) {
        monitorinstrumentRepository.deleteMonitorinstrumentInfo(equipmentNo);
    }

    /**
     * 通过探头信息查询监控信息
     *
     * @param instrumentno
     * @return
     */
    @Override
    public MonitorinstrumentDTO selectMonitorByIno(String instrumentno) {
        return monitorinstrumentRepository.selectMonitorByIno(instrumentno);
    }

    /**
     * 删除探头信息
     *
     * @param instrumentno
     */
    @Override
    public void deleteMonitorinstrumentInfoByINo(String instrumentno) {
        monitorinstrumentRepository.deleteMonitorinstrumentInfoByINo(instrumentno);
    }

    /**
     * 查询探头信息
     *
     * @param equipmentNo
     * @return
     */
    @Override
    public Integer findProbeInformationByEno(String equipmentNo) {
        return monitorinstrumentRepository.findProbeInformationByEno(equipmentNo);
    }

}
