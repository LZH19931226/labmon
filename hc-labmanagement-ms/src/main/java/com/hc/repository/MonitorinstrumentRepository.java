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
     * 查询监控信息
     * @param equipmentNo 设备id
     * @return
     */
    MonitorinstrumentDTO selectMonitorByEno(String equipmentNo);

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


    /**
     * 删除监控探头信息
     * @param equipmentNo
     */
    void deleteMonitorinstrumentInfo(String equipmentNo);
}