package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hc.dto.MonitorinstrumentDTO;
import com.hc.po.MonitorinstrumentPo;

import java.util.List;


public interface MonitorinstrumentRepository extends IService <MonitorinstrumentPo>{


    /**
     * 通过sn和hospitalCode查询sn是否被占用
     * @param monitorinstrumentDTO
     * @return
     */
    Integer selectCount(MonitorinstrumentDTO monitorinstrumentDTO);


    /**
     * 查询监控信息
     * @param equipmentNo 设备id
     * @return
     */
    List<MonitorinstrumentDTO> selectMonitorByEno(String equipmentNo);

    /**
     * 更新监控信息
     * @param monitorinstrumentDTO
     */
    void updateMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO);

    /**
     * 插入监控信息
     * @param monitorinstrumentDTO
     */
    void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO);



}