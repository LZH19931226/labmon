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


    /**
     * 删除监控探头信息
     * @param equipmentNo
     */
    void deleteMonitorinstrumentInfo(String equipmentNo);

    /**
     * 通过探头信息查询监控信息
     * @param instrumentno
     * @return
     */
    MonitorinstrumentDTO selectMonitorByIno(String instrumentno);

    /**
     * 删除探头信息
     * @param instrumentno
     */
    void deleteMonitorinstrumentInfoByINo(String instrumentno);

    /**
     * 查询探头信息
     * @param equipmentNo
     * @return
     */
    Integer findProbeInformationByEno(String equipmentNo);

    void bulkUpdate(List<MonitorinstrumentDTO> monitorinstrumentDTO);

    List<MonitorinstrumentDTO> getMonitorInstrumentByEnoList(List<String> enoList);
}
