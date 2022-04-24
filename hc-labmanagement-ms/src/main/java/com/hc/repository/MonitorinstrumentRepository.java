package com.hc.repository;

import com.baomidou.mybatisplus.extension.service.IService;

import com.hc.dto.MonitorinstrumentDTO;
import com.hc.po.MonitorinstrumentPo;


public interface MonitorinstrumentRepository extends IService <MonitorinstrumentPo>{


    /**
     * 通过sn和hospitalCode查询sn是否被占用
     * @param monitorinstrumentDTO
     * @return
     */
    Integer selectCount(MonitorinstrumentDTO monitorinstrumentDTO);

    /**
     * 插入监控仪器信息
     * @param monitorinstrumentDTO
     */
    void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO);
}