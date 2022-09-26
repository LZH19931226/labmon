package com.hc.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hc.application.command.InstrumentMonitorCommand;
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


    /**
     * 查询所有探头默认配置
     * @return
     */
    List<InstrumentmonitorDTO> selectMonitorEquipmentAll();

    /**
     * 查询探头监控的信息
     * @return
     */
    List<InstrumentmonitorDTO> selectInstrumentMonitorInfo(String hospitalCode);

    /**
     *
     * @param instrumentTypeId
     * @return
     */
    int countByInstrumentTypeId(String instrumentTypeId);

    /**
     * 删除
     * @param instrumentTypeId
     */
    void removeByTypeId(Integer instrumentTypeId);

    void add(InstrumentMonitorCommand instrumentMonitorCommand);

    List<InstrumentmonitorDTO> list(Page<InstrumentmonitorDTO> page, Integer instrumentTypeId,Integer instrumentConfigId);

    void remove(Integer instrumentTypeId, Integer instrumentConfigId);

}

