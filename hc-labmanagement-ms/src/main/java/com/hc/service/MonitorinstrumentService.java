package com.hc.service;


import com.hc.dto.MonitorinstrumentDTO;

/**
 * 
 *
 * @author liuzhihao
 * @email 1969994698@qq.com
 * @date 2022-04-18 15:27:01
 */
public interface MonitorinstrumentService{


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
     * 更新监控仪器
     * @param monitorinstrumentDTO
     */
    void updateMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO);

    /**
     * 插入探头信息
     * @param monitorinstrumentDTO
     */
    void insertMonitorinstrumentInfo(MonitorinstrumentDTO monitorinstrumentDTO);

    /**
     * 删除监控探头信息
     * @param equipmentNo
     */
    void deleteMonitorinstrumentInfo(String equipmentNo);
}

