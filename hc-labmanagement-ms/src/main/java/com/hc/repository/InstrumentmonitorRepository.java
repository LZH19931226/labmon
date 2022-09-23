package com.hc.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 更新监控仪器信息
     * @param instrumentmonitorDTO
     */
    void updateInstrumentmonitor(InstrumentmonitorDTO instrumentmonitorDTO);

    /**
     * 判断插入的信息是否已存在
     * @param instrumentmonitorDTO
     * @return
     */
    boolean selectOne(InstrumentmonitorDTO instrumentmonitorDTO);

    /**
     * 查询监控仪器信息
     * @param instrumenttypeid
     * @return
     */
    List<InstrumentmonitorDTO> selectMonitorEquipmentList(Integer instrumenttypeid);

    /** 查询所有探头默认信息 */
    List<InstrumentmonitorDTO> selectMonitorEquipmentAll();

    /**
     * 查询探头监控的信息
     * @return
     */
    List<InstrumentmonitorDTO> selectInstrumentMonitorInfo(String hospitalCode);

    /**
     * 分页查询
     * @param page
     * @param instrumentTypeId
     * @return
     */
    List<InstrumentmonitorDTO> listByPage(Page<InstrumentmonitorDTO> page, Integer instrumentTypeId);
}
